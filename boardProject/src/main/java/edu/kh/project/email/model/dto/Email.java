package edu.kh.project.email.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Email {

	private int keyNo;
	private String email;
	private String authKey;
	private String createTime;
	
}
