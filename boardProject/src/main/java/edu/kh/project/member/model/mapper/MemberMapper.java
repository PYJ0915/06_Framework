package edu.kh.project.member.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.member.model.dto.Member;

@Mapper
public interface MemberMapper {

	/** 로그인 SQL 실행
	 * @param memberEmail
	 * @return loginMember
	 */
	Member login(String memberEmail) throws Exception;

	/** 이메일 중복 검사 SQL 실행
	 * @param memberEmail
	 * @return count
	 */
	int checkEmail(String memberEmail);

	/** 닉네임 중복 검사 SQL 실행
	 * @param memberNickname
	 * @return
	 */
	int checkNickname(String memberNickname);

	/** 회원가입 SQL 실행
	 * @param inputMember
	 * @return
	 */
	int signup(Member inputMember);

	/** 회원 조회 SQL 실행
	 * @return
	 */
	List<Member> selectMember();

	/** 비밀번호 리셋 SQL 실행
	 * @param map
	 * @return
	 */
	int resetPw(Map<String, Object> map);

	/** 회원 복구 SQL 실행
	 * @param memberNo
	 * @return
	 */
	int restorationMember(int memberNo);

	/** 탈퇴 여부 확인
	 * @param memberNo
	 * @return
	 */
	int checkDelFl(int memberNo);

}
