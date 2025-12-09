package edu.kh.project.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/* @SessionAttributes({"key", "key", "key"...})
 * => Model에 추가된 속성 중 key값이 일치하는 속성을 session scope로 변경
 * */

@RequestMapping("member")
@Controller
@Slf4j
@SessionAttributes({"loginMember"})
public class MemberController {
	
	@Autowired // 의존성 주입 (DI)
	private MemberService service;
	
	/** [로그인]
	 * - 특정 사이트에 아이디(이메일), 비밀번호 등을 입력하여 해당 정보가 있으면 조회/서비스 이용
	 * 
	 * - 로그인한 회원의 정보를 session에 기록하여 로그아웃 또는 브라우저 종료 시까지 해당 정보를 계속 이용할 수 있게함
	 * 
	 * @param inputMember : 커맨드 객체 (@ModelAttribute 생략 가능)
	 * 						memberEmail, memberPw가 세팅된 상태
	 * @return
	 */
	@PostMapping("login")	// /member/login 요청 POST 방식 매핑
	public String login(@ModelAttribute Member inputMember, 
						RedirectAttributes ra, 
						Model model, 
						@RequestParam(value="saveId", required = false) String saveId,
						HttpServletResponse resp ) {
		
		// 로그인 서비스 호출
		try {
			Member loginMember = service.login(inputMember);
			
			log.debug("loginMember" + loginMember);
			
			if(loginMember == null) { 
				// 로그인 실패 시
				ra.addFlashAttribute("message", "아이디 또는 비밀번호가 일치하지 않습니다");
				
			} else { 
				// 로그인 성공 시
				model.addAttribute("loginMember", loginMember);
				// 1단계 : request scope에 세팅 됨
				// 2단계 : 클래스 위에 @SessionAttributes() 어노테이션 작성하여 session scope로 이동
				
				// ------------------ Cookie ------------------
				// 이메일 저장
				
				// 쿠키 객체 생성 (K:V)
				Cookie cookie = new Cookie("saveId", loginMember.getMemberEmail());
				// saveId=user01@kh.or.kr
				
				// 쿠키가 적용될 경로 설정
				// => 클라이언트가 어떤 요청을 할 때 쿠키가 첨부될 지 지정
				cookie.setPath("/");
				// "/" => IP 또는 도메인 또는 localhost
				// 	   => 메인페이지 + 그 하위 주소 모두
				
				// 쿠키의 만료 기간 지정
				if(saveId != null) { 
					// 아이디 저장 체크한 경우
					cookie.setMaxAge(60 * 60 * 24 * 31); // 초 단위 지정
				} else {
					// 아이디 저장 체크하지 않은 경우
					cookie.setMaxAge(0); // 0초 => (클라이언트 쿠키 삭제)
				}
				
				// 응답 객체에 쿠키 추가 => 클라이언트 전달
				resp.addCookie(cookie);
				
			}
			
		} catch (Exception e) {
			log.info("로그인 중 예외 발생해버림..");
			e.printStackTrace();
		}
		
		return "redirect:/"; // 메인페이지 재요청
	}
	
	/** 로그아웃 : session에 저장된 로그인된 회원 정보를 없애주어야함
	 * @param SessionStatus : @SessionAttributes로 지정된 특정 속성을 세션에서 제거할 수 있는 기능을 제공하는 객체
	 * @return
	 */
	@GetMapping("logout")
	public String logout(SessionStatus status) {
		
		status.setComplete(); // 세션을 완료 시킴
		
		return "redirect:/";
	}
	
	/** 회원가입 페이지로 이동 (forward)
	 * @return
	 */
	@GetMapping("signup")
	public String signupPage() {
		return "member/signup";
	}
	
	/** 이메일 중복검사 (비동기 요청)
	 * @return
	 */
	@ResponseBody // 응답 본문으로 응답값을 돌려보냄
	@GetMapping("checkEmail")	// GET 방식 /member/checkEmail 요청
	public int checkEmail(@RequestParam("memberEmail") String memberEmail) {
		return service.checkEmail(memberEmail);
	}
	
	/** 닉네임 중복 검사
	 * @param memberNickname
	 * @return 중복이면 1, 아니면 0
	 */
	@ResponseBody
	@GetMapping("checkNickname")
	public int checkNickname(@RequestParam("memberNickname") String memberNickname) {
		return service.checkNickname(memberNickname);
	}
	
	/** 회원가입
	 * @param inputMember : 커맨드 객체 (입력된 회원 정보)
	 * 					=> memberEmail, memberPw, memberNickname, memberTel
	 * 					  (memberAddress 우편번호 => 필요 X)
	 * 
	 * @param memberAddress : 입력한 주소 input 3개의 값을 배열로 전달 
	 * 		 	[우편번호, 도로명/지번주소, 상세주소]
	 * 
	 * @param ra : RedirectAttributes로 리다이렉트 시 1회성으로 req -> session -> req로 전달되는 객체
	 * 
	 * @return
	 */
	@PostMapping("signup")
	public String signup(@ModelAttribute Member inputMember, 
						@RequestParam("memberAddress") String[] memberAddress,
						RedirectAttributes ra) {
		
		// 회원가입 서비스 호출
		int result = service.signup(inputMember, memberAddress);
		
		String path = null;
		String message = null;
		
		if(result > 0) {
			message = inputMember.getMemberNickname() + "님의 가입을 환영합니다!";
			path = "/";
		} else {
			message = "회원가입 실패...";
			path = "signup";
		}
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:" + path;
		// 성공 시 => redirect:/ (메인페이지 재요청)
		// 실패 시 => redirect:signup (상대 경로)
		// 현재 주소 : member/signup
		// 목표 주소 : member/signup (GET 방식 요청)
	}
	
}
