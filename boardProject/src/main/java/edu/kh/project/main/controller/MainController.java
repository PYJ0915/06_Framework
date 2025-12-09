package edu.kh.project.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MainController {
	
	@RequestMapping("/")
	public String mainPage() {
		
		// 접두사, 접미사 제외
		// 접두사 : classpath:/templates/
		// 접미사 : .html
		return "common/main";
	}
	
	// LoginFilter에서 로그인하지 않았을 때 발생하는 redirect 요청
	@GetMapping("loginError")
	public String loginError(RedirectAttributes ra) {
		ra.addFlashAttribute("message", "로그인 후 이용바랍니다!");
		return "redirect:/";
	}
	
}
