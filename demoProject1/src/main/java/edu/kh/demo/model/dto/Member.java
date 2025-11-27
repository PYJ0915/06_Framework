package edu.kh.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor // 모든 필드 초기화용 매개변수 생성자
@NoArgsConstructor //기본생성자
@Data	// getter + setter + toString
public class Member {

	private String memberId;
	private String memberPw;
	private String memberName;
	private int memberAge;
	
}
