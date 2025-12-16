package edu.kh.project.mypage.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.mypage.model.dto.UploadFile;
import edu.kh.project.mypage.model.service.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// @SessionAttributes({"loginMember"})

/* @SessionAttributes
 * - Model에 추가된 속성 중 key 값이 일치하는 속성을 session scope로 변경하는 어노테이션
 * - 클래스 상단에 @SessionAttributes({"loginMember"}) 작성
 * 
 * @SessionAttribute
 * - @SessionAttributes를 통해 session에 등록된 속성을 꺼내올 때 사용하는 어노테이션
 * - 메서드의 매개변수 자리에 @SessionAttribute("loginMember") Member loginMember 작성
 * */
@SessionAttributes({ "loginMember" })
@Controller
@RequestMapping("myPage")
@Slf4j
@RequiredArgsConstructor
public class MyPageController {

	private final MyPageService service;

	// 내 정보 조회로 이동
	/**
	 * @param loginMember : 세션에 존재하는 loginMember를 얻어와 Member 타입 매개변수 대입
	 * @return
	 */
	@GetMapping("info") // /myPage/info GET 방식 요청 매핑
	public String info(@SessionAttribute("loginMember") Member loginMember, Model model) {

		// 현재 로그인한 회원의 주소를 꺼내옴
		// 현재 로그인한 회원의 정보 => session scope에 등록된 상태 (loginMember)
		// loginMember(memberAddress도 포함)
		// => 만약 회원가입 당시 주소를 입력했다면 주소값 문자열 (^^^ 구분자로 만들어진 문자열)
		// => 회원가입 당시 주소를 입력하지 않았다면 null

		String memberAddress = loginMember.getMemberAddress();
		// 03189^^^서울 종로구 우정국로2길 21^^^302호
		// or null

		if (memberAddress != null) {
			// 주소가 있을 경우에만 동작
			// 구분자 "^^^"를 기준으로 memberAddress 값을 쪼개어 String[]로 반환

			String[] arr = memberAddress.split("\\^\\^\\^");
			// ["03189", "서울 종로구 우정국로2길 21", "302호"]

			model.addAttribute("postcode", arr[0]); // 우편번호
			model.addAttribute("address", arr[1]); // 도로명/지번 주소
			model.addAttribute("detailAddress", arr[2]); // 상세주소

		}

		return "myPage/myPage-info";
	}

	// 프로필 이미지 변경 화면으로 이동
	@GetMapping("profile")
	public String profile() {
		return "myPage/myPage-profile";
	}

	// 비밀번호 변경 화면 이동
	@GetMapping("changePw")
	public String changePw() {
		return "myPage/myPage-changePw";
	}

	// 회원 탈퇴 화면 이동
	@GetMapping("secession")
	public String secession() {
		return "myPage/myPage-secession";
	}

	// 파일테스트 화면 이동
	@GetMapping("fileTest")
	public String fileTest() {
		
		return "myPage/myPage-fileTest";
	}

	// 업로드 파일 목록 화면 이동
	@GetMapping("fileList")
	public String fileList(@SessionAttribute("loginMember") Member loginMember,
							Model model) {
		
		// 파일 목록 조회 서비스 호출 (현재 로그인한 회원이 올린 이미지만)
		int memberNo = loginMember.getMemberNo();
		List<UploadFile> list = service.fileList(memberNo);
		
		// model에 list를 담아서 forward
		model.addAttribute("list", list);
		
		return "myPage/myPage-fileList";
	}

	/**
	 * 회원 정보 수정
	 * 
	 * @param inputMember   : 커맨드 객체 (@ModelAttribute가 생략된 상태) => 제출된
	 *                      memberNickname, memberTel이 세팅된 상태
	 * 
	 * @param memberAddress : 주소만 따로 배열형태로 얻어옴
	 * 
	 * @param loginMember   : 현재 로그인한 회원의 정보를 가지고있는 객체 (업데이트할 행을 알기 위해서 필요) => 현재
	 *                      로그인한 회원의 회원번호(PK)를 사용!
	 * @return
	 */
	@PostMapping("info")
	public String updateInfo(Member inputMember, @RequestParam("memberAddress") String[] memberAddress,
			@SessionAttribute("loginMember") Member loginMember, RedirectAttributes ra) {

		// inputMember에 현재 로그인한 회원의 회원 번호 세팅
		inputMember.setMemberNo(loginMember.getMemberNo());
		// inputMember : 수정된 회원의 닉네임, 수정된 회원의 전화번호, [주소], 회원 번호

		// 회원 정보 수정 서비스 호출
		int result = service.updateInfo(inputMember, memberAddress);

		if (result > 0) {
			ra.addFlashAttribute("message", "회원 정보 수정 성공!");

			// loginMember에 DB상 업데이트된 내용으로 세팅
			// => loginMember는 세션에 저장된 로그인한 회원 정보가 저장되어있음 (로그인했을 당시의 기존 데이터)
			// => loginMember를 수정하면 세션에 저장된 로그인한 회원의 정보가 업데이트
			// == Session에 있는 회원 정보와 DB의 데이터를 동기화

			loginMember.setMemberNickname(inputMember.getMemberNickname());
			loginMember.setMemberTel(inputMember.getMemberTel());
			loginMember.setMemberAddress(inputMember.getMemberAddress());

		} else {
			ra.addFlashAttribute("message", "회원 정보 수정에 실패했습니다...");
		}

		return "redirect:info"; // 재요청 경로 : /myPage/info GET 방식 요청
	}

	/**
	 * 비밀번호 변경
	 * 
	 * @param pwMap       : 제출받은(사용자가 입력한) 비밀번호, 바꿀 비밀번호, 비밀번호 확인이 저장된 Map
	 * @param loginMember : 로그인한 회원 정보 저장 객체
	 * @return
	 */
	@PostMapping("changePw")
	public String changePw(@RequestParam Map<String, String> pwMap, @SessionAttribute("loginMember") Member loginMember,
			RedirectAttributes ra) {

		int result = service.changePw(pwMap, loginMember);

		String message = null;
		String path = null;

		if (result > 0) {
			message = "비밀번호가 변경되었습니다.";
			path = "info";
		} else {
			message = "현재 비밀번호가 일치하지 않습니다.";
			path = "changePw";
		}

		loginMember.setMemberPw(null);
		ra.addFlashAttribute("message", message);

		return "redirect:" + path;
	}

	/**
	 * 회원 탈퇴
	 * 
	 * @param memberPw    : 제출받은(사용자가 입력한) 비밀번호
	 * @param loginMember : 로그인한 회원 정보 저장 객체
	 * @return
	 */
	@PostMapping("secession") // myPage/secession POST요청 매핑
	public String secession(@RequestParam("memberPw") String memberPw,
			@SessionAttribute("loginMember") Member loginMember, SessionStatus status, RedirectAttributes ra) {

		// 로그인한 회원의 회원 번호 꺼내오기
		int memberNo = loginMember.getMemberNo();

		// 서비스 호출
		int result = service.secession(memberPw, memberNo);

		String message = null;
		String path = null;

		if (result > 0) {

			// 탈퇴 성공 시 메인페이지 재요청
			message = "탈퇴 되었습니다.";
			path = "/";

			status.setComplete(); // 세션 비우기(로그아웃 상태 변경)

		} else {

			// 탈퇴 실패 시 탈퇴페이지 재요청
			message = "비밀번호가 일치하지 않습니다";
			path = "secession";

		}

		ra.addFlashAttribute("message", message);

		return "redirect:" + path;
	}

	/*
	 * Spring에서 파일을 처리하는 방법
	 * 
	 * - entype="multipart/form-data"로 클라이언트의 요청을 받으면 (문자, 숫자, 파일 등이 섞여있는 요청)
	 * 
	 * 이를 MultipartResolver(FileConfig에 정의)를 이용하여 섞여있는 파라미터 분리 작업을 함
	 * 
	 * 문자열, 숫자 => String 파일 => MultipartFile
	 * 
	 */

	@PostMapping("file/test1") // /myPage/file/test1
	public String fileUpload1(@RequestParam("uploadFile") MultipartFile uploadFile, RedirectAttributes ra) {

		try {

			String path = service.fileUpload1(uploadFile);
			// /myPage/file/파일명.jpg

			// 파일이 실제로 서버 컴퓨터에 저장이 되어 웹에서 접근할 수 있는 경로가 반환되었을 때
			if (path != null) {
				ra.addFlashAttribute("path", path);
			}

		} catch (Exception e) {

			e.printStackTrace();
			log.info("파일 업로드 예제1 중 예외 발생");

		}

		return "redirect:/myPage/fileTest";
	}

	@PostMapping("file/test2")
	public String fileUpload2( @RequestParam("uploadFile") MultipartFile uploadFile,
							@SessionAttribute("loginMember") Member logMember,
							RedirectAttributes ra ) {
		
		try {
			
			// 로그인한 회원의 회원번호
			int memberNo = logMember.getMemberNo();
			
			// 업로드된 파일
			int result = service.fileUpload2(uploadFile, memberNo);
			
			String message = null;
			
			if(result > 0) {
				message = "파일 업로드 성공!";
			} else {
				message = "파일 업로드 실패...";
			}
			
			ra.addFlashAttribute("message", message);
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("파일 업로드 테스트2 중 예외 발생");
		}
		
		return "redirect:/myPage/fileTest";
	}
	
	@PostMapping("file/test3")
	public String fileUpload3(@RequestParam("aaa") List<MultipartFile> aaaList, 
							@RequestParam("bbb") List<MultipartFile> bbbList,
							@SessionAttribute("loginMember") Member logMember,
							RedirectAttributes ra ) throws Exception {
		
		// aaa 파일 미제출 시
		// 0번, 1번 인덱스로 구성 => 파일은 모두 비어있음
		log.debug("aaaList : " + aaaList); // [요소, 요소]
		
		// bbb 파일 미제출 시
		// 0번 인덱스로 구성 => 파일은 모두 비어있음
		log.debug("bbbList : " + bbbList); // [요소]
		
		// 여러 파일 업로드 서비스 호출
		int result = service.fileUpload3(aaaList, bbbList, logMember.getMemberNo());
		
		// result == aaaList와 bbbList에 업로드된 파일 개수
		String message = null;
		
		if(result == 0) {
			message = "업로드된 파일이 존재하지 않습니다.";
		} else {
			message = result + "개의 파일이 업로드되었습니다!";
		}
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:/myPage/fileTest";
	}

	@PostMapping("profile")
	public String profile(@RequestParam("profileImg") MultipartFile profileImg, 
						@SessionAttribute("loginMember") Member loginMember, 
						RedirectAttributes ra ) throws Exception {
		
		// 서비스 호출
		int result = service.profile(profileImg, loginMember);
		
		String message = null;
		
		if(result > 0) {
			message = "변경 성공!";
		} else {
			message = "변경 실패 ㅜㅜ";
		}
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:profile"; // 리다이렉트 => /myPage/profile Get 요청
	}
	
}
