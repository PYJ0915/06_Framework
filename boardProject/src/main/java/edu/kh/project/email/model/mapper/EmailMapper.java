package edu.kh.project.email.model.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.email.model.dto.Email;

@Mapper
public interface EmailMapper {

	/** 기존 이메일에 대한 인증키 update
	 * @param map (email, authKey)
	 * @return int : 행의 개수
	 */
	int updateAuthKey(Map<String, String> map);

	/** 이메일과 인증번호를 새로 insert
	 * @param map (email, authKey)
	 * @return int : 행의 개수
	 */
	int insertAuthKey(Map<String, String> map);

	int checkAuthKey(Email email);

}
