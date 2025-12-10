package edu.kh.project.mypage.model.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
		
		String currentEncPw = mapper.getEncPw(loginMember.getMemberNo());
		
		// 로그인한 정보의 비밀번호와 현재 비밀번호가 같은지 체크
		if( !bcrypt.matches(pwMap.get("currentPw"), currentEncPw) ) {
			return 0;
		}
		
		// 같은 경우 비밀번호 암호화 진행 후 비밀번호 업데이트
		String encPw = bcrypt.encode(pwMap.get("newPw"));	
		
		loginMember.setMemberPw(encPw);
		
		return mapper.changePw(loginMember);
	}

	@Override
	public int secession(String memberPw, int memberNo) {
		
		// 1. 현재 로그인한 회원의 암호화된 비밀번호를 DB에서 조회
		String encPw = mapper.getEncPw(memberNo);
		
		// 2. 입력받은 비밀번호와 암호화된 DB 비밀번호가 같은지 비교
		if(!bcrypt.matches(memberPw, encPw)) {
			// 다른 경우
			return 0;
		}
		
		// 같은 경우
		return mapper.secession(memberNo);
	}

	// 파일 업로드 테스트 1 
	@Override
	public String fileUpload1(MultipartFile uploadFile) throws Exception{
		
		if(uploadFile.isEmpty()) {
			// 업로드한 파일이 없을 경우
			return null;
		}
		
		// 업로드한 파일이 있을 경우
		// C://uploadFiles/test/파일명 으로 서버에 저장
		uploadFile.transferTo(new File("C:/uploadFiles/test/" 
								+ uploadFile.getOriginalFilename()));
		
		// C:/uploadFiles/test/하루.jpg
		
		// 웹에서 해당 파일에 접근할 수 있는 경로를 만들어 반환
		
		// 이미지가 최종 저장된 서버 컴퓨터상의 경로
		// C:/uploadFiles/test/파일명.jpg
		
		// 클라이언트가 브라우저에 해당 이미지를 보기위해 요청하는 경로
		// ex) <img src="경로">
		// /mtPage/file/파일명.jpg -> <img src="/myPage/file/파일명.jpg">
		
		return "/myPage/file/" + uploadFile.getOriginalFilename();
	}



}
