package edu.kh.project.mypage.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.mypage.model.dto.UploadFile;

@Mapper
public interface MyPageMapper {

	/** 회원 정보 수정 SQL 실행
	 * @param inputMember
	 * @return
	 */
	int updateInfo(Member inputMember);

	/** 암호화된 비밀번호 얻어오는 SQL 실행
	 * @param memberNo
	 * @return
	 */
	String getEncPw(int memberNo);
	
	/** 비밀번호 변경 SQL 실행
	 * @param loginMember
	 * @return
	 */
	int changePw(Member loginMember);

	/** 회원 탈퇴 SQL 실행
	 * @param memberNo
	 * @return
	 */
	int secession(int memberNo);

	/** 파일 정보를 DB에 삽입 SQL 실행
	 * @param uf
	 * @return
	 */
	int insertUploadFile(UploadFile uf);

	/** 파일 목록 조회 SQL 실행
	 * @param memberNo
	 * @return
	 */
	List<UploadFile> fileList(int memberNo);

	/** 프로필 이미지 변경 SQL 실행
	 * @param member
	 * @return
	 */
	int profile(Member member);

}
