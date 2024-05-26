package com.example.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//.anyRequest().authenticated()  // 모든 요청은 인증된 사용자만 접근 가능

@Configuration
public class SecurityConfig{
	
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // 인가(접근권한) 설정
    
    /*
    http
    	.authorizeRequests()
    		.antMatchers("/admin/**").hasRole("ADMIN")  // ADMIN 권한을 가진 사용자만 접근 가능
    		.antMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
    		.antMatchers("/user").authenticated()
    		.antMatchers("/").permitAll()
		.and()
		.formLogin()
			.loginPage("/loginForm")
			.loginProcessingUrl("/login")
			.defaultSuccessUrl("/")
			.usernameParameter("username")
			.permitAll()
		.and()
		.logout()
			.permitAll();
    */
    
    //모든 링크(사용자)에 대해 허용을 해 준 상태, 권한관리필터)	
    http.authorizeHttpRequests().antMatchers("/").permitAll(); 
    
    //authenticated() -> 인증된 사용자만
    http.authorizeHttpRequests().antMatchers("/user").authenticated();
    
    // admin 하위의 모든 자원 -> "ADMIN"에게 부여 -> hasRole("ADMIN") ADMIN 권한을 가진 사용자만 접근 가능
    http.authorizeHttpRequests().antMatchers("/admin/**").hasRole("ADMIN");
    
    // manager 하위의 모든 자원 -> "ADMIN", "MANAGER" 에게 부여 -> hasAnyRole("ADMIN", "MANAGER") 주어진 권한중 하나라도 갖고있는지 확인
    http.authorizeHttpRequests().antMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER"); 
    

    
    // GUEST는 무권한 -> 생략
    // loginSuccess 접근 -> "ADMIN"만 접근 허용
    //http.authorizeHttpRequests().antMatchers("/user2/loginSuccess").hasAnyRole("3", "4", "5");
    
    // 사이트 위변조 요청 방지
    http.csrf().disable();
    
    // 로그인 설정
    http.formLogin()
    .loginPage("/loginForm")
    .loginProcessingUrl("/login") // login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행
    .defaultSuccessUrl("/") // 성공시 url 호출 -> 하지만 특정페이지 ex)/user url 요청하면 로그인 성공시 /user 페이지 호출
    .usernameParameter("username"); // PrincipalDetailsService 클래스의 loadUserByUsername 메서드 파라미값 명 설정
		
//    // 사이트 위변조 요청 방지
//    http.csrf().disable();
//
//    // 로그인 설정
//    http.formLogin()
//    .loginPage("/user2/login")
//    .defaultSuccessUrl("/user2/loginSuccess")
//    .failureUrl("/user2/login?success=100)")
//    .usernameParameter("uid")
//    .passwordParameter("pass");
//		
//    // 로그아웃 설정
//    http.logout()
//    .invalidateHttpSession(true)
//    .logoutRequestMatcher(new AntPathRequestMatcher("/user2/logout"))
//    .logoutSuccessUrl("/user2/login?success=200");
//
//    // 사용자 인증 처리 컴포넌트 서비스 등록
//    http.userDetailsService(service);
    return http.build();
    
    }
    
    //해당 메서드의 리턴되는 오브젝트를 IoC로 등록
    @Bean
    public BCryptPasswordEncoder PasswordEncoder () {
    	//return new MessageDigestPasswordEncoder("SHA-256");
    	return new BCryptPasswordEncoder();
    }
    
}
