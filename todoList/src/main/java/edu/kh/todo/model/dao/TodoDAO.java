package edu.kh.todo.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.todo.model.mapper.TodoMapper;

@Repository // DAO 계층 역할 명시 + Bean 등록
public class TodoDAO {
	
	@Autowired
	private TodoMapper mapper;
	// mapper에는 TodoMapper 인터페이스 구현체가 의존성 주입됨
	// => 해당 구현체가 sqlSessionTemplate을 이용!

	public String testTitle() {
		return mapper.testTitle();
	}

}
