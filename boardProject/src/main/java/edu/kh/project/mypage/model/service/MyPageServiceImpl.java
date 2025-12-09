package edu.kh.project.mypage.model.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.mypage.model.mapper.MyPageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService{
	
	private final MyPageMapper mapper;
	
	private final BCryptPasswordEncoder bcrypt;

	/** 회원 정보 수정 서비스
	 *
	 */
	@Override
	public int updateInfo(Member inputMember, String[] memberAddress) {
		
		// 입력된 주소가 있을 경우
		
		if(!inputMember.getMemberAddress().equals(",,")) {
			String address = String.join("^^^", memberAddress);
			inputMember.setMemberAddress(address);
		} else {
			inputMember.setMemberAddress(null);
		}
		
		return mapper.updateInfo(inputMember);
	}

	/** 비밀번호 변경 서비스
	 *
	 */
	@Override
	public int changePw(Map<String, String> pwMap, Member loginMember) {
		
		String currentEncPw = mapper.getPw(loginMember.getMemberNo());
		
		// 로그인한 정보의 비밀번호와 현재 비밀번호가 같은지 체크
		if( !bcrypt.matches(pwMap.get("currentPw"), currentEncPw) ) {
			return 0;
		}
		
		// 같은 경우 비밀번호 암호화 진행 후 비밀번호 업데이트
		String encPw = bcrypt.encode(pwMap.get("newPw"));	
		
		loginMember.setMemberPw(encPw);
		
		return mapper.changePw(loginMember);
	}



}
