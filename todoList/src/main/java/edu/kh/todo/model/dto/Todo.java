package edu.kh.todo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Todo {
	
	private int todoNo; // 할 일 번호(TODO_NO)
	private String todoTitle; // 할 일 제목(TODO_TITLE)
	private String todoContent; // 할 일 내용(TODO_CONTENT)
	private String complete; // 할 일 완료 여부(COMPLETE "Y" / "N")
	private String regDate; // 할 일 등록일(REG_DATE)

}
