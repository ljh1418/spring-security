package com.example.security1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.security1.model.User;
import com.example.security1.repository.UserRepository;

/*
https://this-circle-jeong.tistory.com/162
https://velog.io/@shon5544/Spring-Security-3.-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EA%B5%AC%ED%98%84
*/


@Controller
public class IndexController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/")
	public String index() {
		System.out.println("index...");
		// 머스테치 기본폴터 src/main/resources/
		// 뷰리졸버 설정 :  teamplates (prefix:앞), .mustache(suffix:뒤) -> 생략가능 기본경로
		return "index"; // ->index.html을 바라보는 이유 WebMvcConfig 클래스에서 설정
	}
	
	@GetMapping("/user")
	public @ResponseBody String user() {
		return "user";
	}
	
	@GetMapping("/admin")
	public @ResponseBody String admin() {
		return "admin";
	}
	
	@GetMapping("/manager")
	public @ResponseBody String manager() {
		return "manager";
	}
	
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}
	
	@PostMapping("/join")
	public String join(User user) {
		System.out.println("user :" + user);
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		user.setRole("ROLE_USER");
		user.setPassword(encPassword);
		userRepository.save(user); // 회원가입 잘됨 비밀번호 1234 -> 시큐리티 로그인 불가능 -> 암호화가 안되었기 때문에
		return "redirect:/loginForm";
	}
	
}
