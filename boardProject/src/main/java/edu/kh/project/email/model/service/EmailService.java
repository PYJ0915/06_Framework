package edu.kh.project.email.model.service;

import edu.kh.project.email.model.dto.Email;

public interface EmailService {

	/** 이메일 전송 서비스
	 * @param type : 무슨 이메일을 발송할 것인지 구분할 key로 쓰임
	 * @param email
	 * @return
	 */
	String sendEmail(String type, String email);

	/** 인증번호 확인 서비스
	 * @param email
	 * @return
	 */
	int checkAuthKey(Email email);

}
