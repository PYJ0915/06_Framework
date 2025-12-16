package edu.kh.project.member.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.mapper.MemberMapper;
import lombok.extern.slf4j.Slf4j;

@Transactional(rollbackFor = Exception.class)
@Service
@Slf4j
public class MemberServiceImpl implements MemberService{
	
	@Autowired
	private MemberMapper mapper;
	
	// Bcrypt 암호화 객체 의존성 주입 (SecurityConfig 참고)
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	// 로그인 서비스
	@Override
	public Member login(Member inputMember) throws Exception {
		// 암호화 진행
		// BCryptPasswordEncoder.encode(문자열) : 문자열을 암호화하여 반환
		// String bcryptPassword = bcrypt.encode(inputMember.getMemberPw());
		// log.debug("bcryptPassword : " + bcryptPassword);
		
		// 1. 이메일이 일치하면서 탈퇴하지 않은 회원 (+ 비밀번호) 조회
		Member loginMember = mapper.login(inputMember.getMemberEmail());
		
		// 2. 만약 일치하는 이메일이 없거나 탈퇴했다면 loginMember == null
		if (loginMember == null) return null;
		
		// 3. 입력 받은 비밀번호(평문 : inputMember.getMemberPw())
		// 	  암호화된 비밀번호 (loginMember.getMemberPw())
		// 	  두 비밀번호가 일치하는지 확인!

		// BCryptPasswordEncoder.matches(평문, 암호화)
		// => 평문과 암호화가 일치한다고 판단되면 true, 아니면 false
		
		// 일치하지 않는 경우
		if( !bcrypt.matches(inputMember.getMemberPw(), loginMember.getMemberPw()) ) return null;
			
		
		// 일치하는 경우
		
		// 로그인한 회원 정보에서 비밀번호 제거
		// => 보안을 위해서 클라이언트에게 보내지지 않게끔
		loginMember.setMemberPw(null); 
		
		return loginMember;
	}

	// 이메일 중복 검사 서비스
	@Override
	public int checkEmail(String memberEmail) {
		return mapper.checkEmail(memberEmail);
	}

	// 닉네임 중복 검사 서비스
	@Override
	public int checkNickname(String memberNickname) {
		return mapper.checkNickname(memberNickname);
	}

	// 회원가입 서비스
	@Override
	public int signup(Member inputMember, String[] memberAddress) {
		
		// 1. 주소 배열 => 하나의 문자열로 가공
		// 주소가 입력되지 않았다면
		// inputMember.getMemberAddress() => ",,"
		// memberAddress => [,,]
		
		
		if(!inputMember.getMemberAddress().equals(",,")) {
			// 주소가 입력된 경우
			// String.join("구분자", 배열)
			// => 배열의 모든 요소 사이에 "구분자"를 추가하여 하나의 문자열로 만들어 반환하는 메서드
			
			String address = String.join("^^^", memberAddress);
			// "12345^^^서울시중구^^^3층,302호"
			
			inputMember.setMemberAddress(address);
			
		} else {
			// 주소가 입력되지 않은 경우
			inputMember.setMemberAddress(null); // null 저장
		}
		
		// 2. 비밀번호 암호화
		// inputMember의 memberPw => 평문 상태
		// 평문 상태의 비밀번호를 암호화 후 다시 inputMember에 세팅
		
		String encPw = bcrypt.encode(inputMember.getMemberPw());
		inputMember.setMemberPw(encPw);
		
		return mapper.signup(inputMember);
	}

	@Override
	public List<Member> selectMember() {
		return mapper.selectMember();
	}

	@Override
	public int resetPw(int memberNo) {
		
		// 1. pass01! 암호화
		String encPw = bcrypt.encode("pass01!");
		
		// memberNo와 암호화한 Pw map으로 묶어서 전달
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("memberNo", memberNo);
		map.put("encPw", encPw);
		
		return mapper.resetPw(map);
	}

	@Override
	public int restorationMember(int memberNo) {
		
		// 전달받은 memberNo의 회원이 탈퇴했는지 확인
		int count = mapper.checkDelFl(memberNo);
		
		if (count == 0) return 0;
		
		return mapper.restorationMember(memberNo);
	}

}