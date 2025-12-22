package edu.kh.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling // Spring Scheduler를 이용하기 위한 활성화 어노테이션
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class}) // Spring Security에서 기본 제공하는 로그인 페이지 이용 X
public class BoardProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardProjectApplication.class, args);
	}

}
