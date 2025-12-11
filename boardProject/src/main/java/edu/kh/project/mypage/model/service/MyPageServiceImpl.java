package edu.kh.project.mypage.model.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.common.util.Utility;
import edu.kh.project.member.model.dto.Member;
import edu.kh.project.mypage.model.dto.UploadFile;
import edu.kh.project.mypage.model.mapper.MyPageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
@RequiredArgsConstructor
@PropertySource("classpath:/config.properties")
public class MyPageServiceImpl implements MyPageService{
	
	private final MyPageMapper mapper;
	
	private final BCryptPasswordEncoder bcrypt;

	@Value("${my.profile.web-path}")
	private String profileWebPath; // /myPage/profile/
	
	@Value("${my.profile.folder-path}")
	private String profileFolderPath; // C:/uploadFiles/profile/
	
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

	/** 회원 탈퇴 서비스
	 *
	 */
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

	
	/** 파일 업로드 테스트 1 
	 *
	 */
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

	/** 파일 업로드 테스트 2 서비스
	 *
	 */
	@Override
	public int fileUpload2(MultipartFile uploadFile, int memberNo) throws Exception {
		
		// MultipartFile이 제공하는 메서드
		// - isEmpty() : 업로드된 파일이 없을 경우 true / 없으면 false
		// - getSize() : 파일 크기
		// - getOriginalFileName() : 원본 파일명
		// - transferTo(경로) : 메모리 또는 임시 저장 경로에 업로드된 파일을 원하는 경로에 실제로 저장
		//						(서버의 어떤 폴더에 저장할 것인지 지정)
		
		//  업로드된 파일이 없다면
		if(uploadFile.isEmpty()) {
			return 0;
		}
		
		// 업로드된 파일이 있다면
		
		// 1. 서버에 저장될 서버 폴더 경로 만들기
		
		// 파일이 저장될 서버 폴더 경로
		String folderPath = "C:/uploadFiles/test/";
		
		// 클라이언트가 파일이 저장된 폴더에 접근할 수 있는 주소 (요청 주소)
		String webPath = "/myPage/file/";
		
		// 2. DB에 전달할 데이터를 DTO로 묶어 INSERT
		// webPath, memberNo, 원본 파일명, 변경된 상태의 파일명
		String fileRename = Utility.fileRename(uploadFile.getOriginalFilename());
		
		// Builder 패턴을 이용하여 UploadFile 객체 생성
		// 장점 1) 반복되는 참조변수명, set 구문 생략 가능
		// 장점 2) method chaining을 이용한 한 줄 작성 가능
		UploadFile uf = UploadFile.builder()
						.memberNo(memberNo)
						.filePath(webPath)
						.fileOriginalName(uploadFile.getOriginalFilename())
						.fileRename(fileRename)
						.build();
		
		int result =  mapper.insertUploadFile(uf);
		
		// 3. DB에 INSERT 성공 시 파일을 지정된 서버 폴더에 저장
		
		// INSERT 실패 시
		if (result == 0) return 0;
		
		// INSERT 성공 시
		uploadFile.transferTo(new File(folderPath + fileRename));
		return result; // 1
		
	}

	/** 파일 목록 조회 서비스
	 *
	 */
	@Override
	public List<UploadFile> fileList(int memberNo) {
		return mapper.fileList(memberNo);
	}

	/** 여러 파일 업로드 서비스
	 *
	 */
	@Override
	public int fileUpload3(List<MultipartFile> aaaList, List<MultipartFile> bbbList, int memberNo) throws Exception{
		
		// 1. aaaList 처리
		int result1 = 0;
		
		// 업로드된 파일이 없을 경우를 제외하고 업로드
		for(MultipartFile file : aaaList) {
			
			if(file.isEmpty()) {
				// 파일이 없으면 다음 파일
				continue; // 아래코드 수행하지 않고 다음 반복으로 넘어감
			}
			
			// 파일이 있는 경우
			// fileUpload2() 메서드 호출(재활용)
			// => 파일 하나 업로드 + DB INSERT
			result1 += fileUpload2(file, memberNo); 
		}
		
		// 2. bbbList 처리
		int result2 = 0;
		
		for(MultipartFile file : bbbList) {
			
			if(file.isEmpty()) continue;
			
			result2 += fileUpload2(file, memberNo);
			
		}
		
		return result1 + result2;
	}

	/** 프로필 이미지 변경 서비스
	 *
	 */
	@Override
	public int profile(MultipartFile profileImg, Member loginMember) throws Exception {
		
		// 프로필 이미지 경로 (수정할 경로)
		String updatePath = null;
		
		// 변경명 저장
		String rename = null;
		
		// 업로드할 이미지가 있을 경우
		if( !profileImg.isEmpty() ) {
			// updatePath 경로 조합
			
			// 1. 파일명 변경
			rename = Utility.fileRename(profileImg.getOriginalFilename());
			
			// 2. /myPage/profile/변경된파일명
			updatePath = profileWebPath + rename;
		}
		
		// 수정된 프로필 이미지 경로 + 회원 번호를 저장할 DTO 객체
		Member member = Member.builder()
						.memberNo(loginMember.getMemberNo())
						.profileImg(updatePath).build();
		
		// UPDATE 수행
		int result = mapper.profile(member);
		
		if(result > 0) { // DB에 업데이트 성공
			
			// 프로필 이미지를 없앤 경우(NULL로 수정)를 제외
			if(!profileImg.isEmpty()) {
				
				// 파일을 서버 지정된 폴더에 저장
				profileImg.transferTo(new File(profileFolderPath + rename));
									// C:/uploadFiles/profile/변경한이름
				
			}
			
			// 세션에 등록된 현재 로그인한 회원 정보에서
			// 프로필 이미지 경로를 DB에 업데이트한 경로로 변경
			loginMember.setProfileImg(updatePath);
			return result;
			
		}
		
		return 0;
	}



}
