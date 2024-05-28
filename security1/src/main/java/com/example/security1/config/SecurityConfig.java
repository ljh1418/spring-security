package com.example.security1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.security1.config.auth.oauth.PrincipalOauth2UserService;

//.anyRequest().authenticated()  // 모든 요청은 인증된 사용자만 접근 가능

// @EnableWebSecurity ->  WebSecurityConfigurerAdapter:지원중단 클래스를 상속한 구성 클래스에서 사용됨 
// prefix:앞, suffix:뒤
// Authentication:인증 -> 유저가 누구인지 확인하는 절차
// Authorization:인가 -> 권한이 있는 유저저인지 확인하는 절차
// OAuth:Open Authorization -> 외부 서비으세도 인증을 가능하게 하는 서비스 ex)네이버 로그인
@Configuration
//securedEnabled : secured 어노테이션 활성화 -> IndexController info 메소드에 작성, prePostEnabled : PreAuthorize,PostAuthorize 어노테이션 활성화 -> 특정한 url 권한을 걸어야 할 때
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) 
public class SecurityConfig{
	
	@Autowired
	private PrincipalOauth2UserService principalOauth2UserService;
	
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

    http
    	.csrf().disable()
    	.authorizeHttpRequests()
    		.antMatchers("/").permitAll()
    		.antMatchers("/user").authenticated()
    		.antMatchers("/admin/**").hasRole("ADMIN")
    		.antMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
    	.and()
    		.formLogin()
    			.loginPage("/loginForm")
    			.loginProcessingUrl("/login") // login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행
    			.defaultSuccessUrl("/") // 성공시 url 호출 -> 하지만 특정페이지 ex)/user url 요청하면 로그인 성공시 /user 페이지 호출
    			.usernameParameter("username") // PrincipalDetailsService 클래스의 loadUserByUsername 메서드 파라미값 명 설정
	    .and()
	    	.oauth2Login()
	    		.loginPage("/loginForm")
	    		.userInfoEndpoint()
	    		.userService(principalOauth2UserService);
	    		// 구글 로그인이 완료된 뒤의 후처리가 필요 -> 엑세스토큰 + 사용자프로필정보 받음
	    		
	    		//카카오 로그인
	    		// 1. 코드받기(인증) 
	    		// 2. 엑세스토큰(권한) 
	    		// 3. 사용자 프로필정보 가져옴 
	    		// 4. 그 정보를 토대로 회원가입을 자동으로 진행 
	    		
	    		
    	    ;
    
   
    /*
    //모든 링크(사용자)에 대해 허용을 해 준 상태, 권한관리필터)	
    http.authorizeHttpRequests().antMatchers("/").permitAll(); 
    
    //authenticated() -> 인증된 사용자만
    http.authorizeHttpRequests().antMatchers("/user").authenticated();
    
    // admin 하위의 모든 자원 -> "ADMIN"에게 부여 -> hasRole("ADMIN") ADMIN 권한을 가진 사용자만 접근 가능
    http.authorizeHttpRequests().antMatchers("/admin/**").hasRole("ADMIN");
    
    // manager 하위의 모든 자원 -> "ADMIN", "MANAGER" 에게 부여 -> hasAnyRole("ADMIN", "MANAGER") 주어진 권한중 하나라도 갖고있는지 확인
    http.authorizeHttpRequests().antMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER"); 
    
    // 사이트 위변조 요청 방지
    http.csrf().disable();
    
    // 로그인 설정
    http.formLogin()
    .loginPage("/loginForm")
    .loginProcessingUrl("/login") // login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행
    .defaultSuccessUrl("/") // 성공시 url 호출 -> 하지만 특정페이지 ex)/user url 요청하면 로그인 성공시 /user 페이지 호출
    .usernameParameter("username"); // PrincipalDetailsService 클래스의 loadUserByUsername 메서드 파라미값 명 설정
	*/	
		
		
		
		
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
