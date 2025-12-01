package edu.kh.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.todo.model.dto.Todo;
import edu.kh.todo.model.service.TodoService;

@Controller
@RequestMapping("todo")	// "todo"로 시작하는 모든 요청 매핑
public class TodoController {

	@Autowired	// DI : 의존성 주입(같은 타입이거나 상속관계인 Bean을 의존성 주입)
	private TodoService service;
	
	@PostMapping("add")	// /todo/add로 Post 방식 요청 매핑
	public String addTodo(@RequestParam("todoTitle") String todoTitle, 
						@RequestParam("todoContent") String todoContent,
						RedirectAttributes ra ) {
		// 1. HttpServletRequest req를 통해 getParameter() 하여 얻어오기
		// 2. @RequestParam()를 이용하여 얻어오기
		// 3. @ModelAttribute와 DTO를 이용하여 얻어오기
		
		// -------------------------------------------------------------------
		
		// RedirectAttributes : redirect 시 값을 1회성으로 전달하는 객체
		// RedirectAttributes.addFlashAttribute("key", value);
		// 응답 전: request scope
		// redirect 시 : session scope로 변환 후 사용
		// 응답 후 : request scope로 복귀
		
		// 서비스 메서드 호출 후 결과 반환 받기
		int result = service.addTodo(todoTitle, todoContent);
		
		// 결과에 따라 message 값 지정
		String message = null;
		
		if(result > 0) {
			
			message = "할 일 추가 성공!";
			
		} else {
			
			message = "할 일 추가 실패 ㅠㅠ";
			
		}
		
		// redirect 시 1회성으로 사용할 데이터를 속성으로 추가
		// req -> session -> req
		ra.addFlashAttribute("message", message);
		
		return "redirect:/"; // 메인페이지로 재요청
	}
	
	@GetMapping("detail") // /todo/detail로 Get방식 요청 매핑
	public String todoDetail(@RequestParam("todoNo") int todoNo, Model model, RedirectAttributes ra) {
		
		Todo todo = service.todoDetail(todoNo);
		
		String path = null;
		
		if(todo != null) { // 조회 결과가 있을 경우 detail.html로 forward
			
			path = "todo/detail";
			model.addAttribute("todo", todo); // request scope 값 세팅
			
		} else { // 조회결과가 없을 경우 메인페이지로 redirect
			
			path = "redirect:/";
			ra.addFlashAttribute("message", "해당 할 일이 존재하지 않습니다.");
			
		}
		
		return path;
	}
	
	// 삭제 요청/응답 메서드 todoDelete
	// 삭제 성공 시 메인페이지 redirect, 메세지 : 삭제 성공
	// 삭제 실패 시 해당 상세 페이지 redirect, 메세지 : 삭제 실패
	@GetMapping("delete")
	public String todoDelete(@RequestParam("todoNo") int todoNo, RedirectAttributes ra) {
		
		int result = service.todoDelete(todoNo);
		
		String message = null;
		String path = null;
		
		if(result > 0) {
			
			message = "삭제 성공";
			path = "redirect:/";
			
		} else {
			
			message = "삭제 실패";
			path = "redirect:/todo/detail?todoNo=" + todoNo;
			
		}
		
		ra.addFlashAttribute("message", message);
		
		return path;
		
	}
	
}
