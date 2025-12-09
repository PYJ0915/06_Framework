package edu.kh.project.common.config;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.kh.project.common.filter.LoginFilter;

@Configuration // 서버가 켜질 때 해당 클래스 내 모든 메서드가 실행됨 & Bean 등록
public class FilterConfig {
	
	// 만들어놓은 LoginFilter가 언제 적용될 지 설정
	@Bean // 반환된 객체를 Bean으로 등록  => LoginFilter로 타입을 제한한 객체를 Bean으로 등록
	public FilterRegistrationBean<LoginFilter> loginFilter() {
		FilterRegistrationBean<LoginFilter> filter = new FilterRegistrationBean<>();
		
		// 사용할 필터 객체 세팅
		filter.setFilter(new LoginFilter());
		
		// 필터가 동작할 URL 세팅
		// /myPage/* : /myPage로 시작하는 모든 요청
		String[] filteringURL = {"/myPage/*", "/chatting/*", "/editBoard/*"};
		
		// String[]을 List로 변환
		// Arrays.asList(배열) => List 변환
		filter.setUrlPatterns( Arrays.asList(filteringURL) );
		
		// 필터 이름 지정
		filter.setName("loginFilter");
		
		// 필터 순서 지정
		filter.setOrder(1);
		
		return filter; // 반환된 객체가 Bean으로 등록
	}
}
