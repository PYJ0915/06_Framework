package edu.kh.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // 요청/응답 제어 역할 명치 + Bean 등록
public class ExampleController {

	// 1) @RequestMapping("주소")
	
	// 2) @GetMapping("주소") : Get (조회) 방식 요청 매핑 
	// 	  @PostMapping("주소") : Post (삽입) 방식 요청 매핑 
	// 	  @PutMapping("주소") : Put (수정) 방식 요청 매핑 (form, a태그 요청 불가)
	// 	  @DeleteMapping("주소") : Delete (삭제) 방식 요청 매핑 (form, a태그 요청 불가)
	
	// "/" 를 앞에 안붙이는 이유 
	/*
	 *  1) Spring Boot에서는 요청 주소 앞에 "/" 가 없어도 요청처리 가능 (오류 발생 X)
	 * 
	 *  2) 프로젝트를 AWS같은 호스팅 서비스를 이용하여 배포 시
	 *  	=> 만약 리눅스 os를 이용하면 build 과정에서 경로상 오류가 발생함!
	 * */
	
	@GetMapping("example")  // 	/example GET 방식 요청 매핑
	public String exampleMethod() {
		
		// forward하려는 html 파일 경로 return 작성
		// 단, 타임리프의 접두사, 접미사는 제외하고 작성 (ViewResolver가 접두사, 접미사 제공)
		
		// 접두사 : classpath:/templates/
		// 접미사 : .html
		
		return "example"; 
		// src/main/resources/templates/example.html
		
	}
	
}
