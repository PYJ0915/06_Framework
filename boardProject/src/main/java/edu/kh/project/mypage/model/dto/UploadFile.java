package edu.kh.project.mypage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadFile {
	private int fileNo;
	private String filePath;
	private String fileOriginalName;
	private String fileRename;
	private String fileUploadDate;
	private int memberNo;
	
	// DTO를 만들 때 반드시 테이블 컬럼과 동일하게 만들어야하는 것은 아니다
	// 필요에 의해 필드를 더 추가해도 되고, 또는 삭제해도 된다
	private String memberNickname;
}
