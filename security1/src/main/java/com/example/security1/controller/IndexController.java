package com.example.security1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.security1.config.auth.PrincipalDetails;
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
	
	//일반로그인
	@GetMapping("/test/login")
	public @ResponseBody String testLogin(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails) { // DI 의존성 주입
		System.out.println("testLogin authentication : " + authentication.getPrincipal());
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("principalDetails ::: " + principalDetails.getUser());
        System.out.println("userDetails ::: " + userDetails.getUser());
		return "세션 정보 확인하기";
	}
	
	//구글로그인
	@GetMapping("/test/oauth/login")
	public @ResponseBody String testOauthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oauth) { // DI 의존성 주입
		System.out.println("/test/oauth/login=================");
		OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
		System.out.println(oauth2User.getAttributes());
		System.out.println("oauth2User :::" + oauth.getAttributes());
		return "OAuth 세션 정보 확인하기";
	}
	
	@GetMapping("/")
	public String index() {
		System.out.println("index...");
		// 머스테치 기본폴터 src/main/resources/
		// 뷰리졸버 설정 :  teamplates (prefix:앞), .mustache(suffix:뒤) -> 생략가능 기본경로
		return "index"; // ->index.html을 바라보는 이유 WebMvcConfig 클래스에서 설정
	}
	
	// OAuth 로그인을 해도 PrincipalDetails
	// 일반 로그인을 해도 PrincipalDetails
	@GetMapping("/user")
	public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) { //@AuthenticationPrincipal UserDetails userDetails 둘중하나로 받기가능
		System.out.println("principalDetails : " + principalDetails.getUser());
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
	
	@Secured("ROLE_ADMIN") //권한을 부여
	@GetMapping("/info")
	public @ResponseBody String info() {
		return "개인정보";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
	@GetMapping("/data")
	public @ResponseBody String data() {
		return "데이터정보";
	}
	
	
	
}
