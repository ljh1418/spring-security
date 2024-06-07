package com.example.security1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.security1.config.auth.oauth.PrincipalOauth2UserService;

//.anyRequest().authenticated()  // 모든 요청은 인증된 사용자만 접근 가능

// @EnableWebSecurity ->  WebSecurityConfigurerAdapter:지원중단 클래스를 상속한 구성 클래스에서 사용됨 
// prefix:앞, suffix:뒤
// Authentication:인증 -> 유저가 누구인지 확인하는 절차
// Authorization:인가 -> 권한이 있는 유저저인지 확인하는 절차
// OAuth:Open Authorization -> 외부 서비으세도 인증을 가능하게 하는 서비스 ex)구글, 네이버 로그인

@Configuration
//@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)//securedEnabled : secured 어노테이션 활성화 -> IndexController info 메소드에 작성, prePostEnabled : PreAuthorize,PostAuthorize 어노테이션 활성화 -> 특정한 url 권한을 걸어야 할 때 
public class SecurityConfig{
	
	@Autowired
	@Lazy
	private PrincipalOauth2UserService principalOauth2UserService;
	
    //해당 메서드의 리턴되는 오브젝트를 IoC로 등록
    @Bean
    public BCryptPasswordEncoder PasswordEncoder () {
    	return new BCryptPasswordEncoder();
    }
    
    
    //WebSecurityConfigurerAdapter 사용 x -> Spring Security 5.4부터는 더이상 사용권장 하지 않음 
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	// http 접근 권한 설정
    	// authorizeRequests() : HttpServletRequest 요청 URL에 따라 접근 권한을 설정합니다.
    	// antMatchers : 요청 URL 경로 패턴을 지정합니다.
    	// authenticated : 인증된 유저만 접근을 허용합니다.
    	// hasRole, hasAnyRole : 특정 권한을 가지는 사용자만 접근할 수 있습니다.
    	// permitAll() : 모든 유저에게 접근을 허용합니다.
    	
    	// http 로그인 설정
    	// formLogin() : 로그인 설정을 진행 합니다.
    	// loginPage() : 커스텀 로그인 페이지 경로 
    	// loginProcessingUrl() : 로그인 요청을 처리할 url 지정 스프링 시큐리티는 /login url사용
    	
    	//스프링시큐리티 동작방식
//    	<form action="/login" method="post">
//        <input type="text" name="username" placeholder="Username">
//        <input type="password" name="password" placeholder="Password">
//        <button type="submit">Login</button>
//        </form>
    	
    	// AuthenticationFilter 
    	// UsernamePasswordAuthenticationFilter 클래스 -> /login, post 요청이면
    	// username, password 파라미터 읽음
    	// AuthenticationManager 호출 
    	// AuthenticationManager -> ProviderManager 상속
    	// ProviderManager -> public Authentication authenticate(Authentication authentication) throws AuthenticationException {|
    	
    	
    	// defaultSuccessUrl() : 로그인 성공 이후 리다이렉트 url
    	// usernameParameter() : 로그인 폼에서 사용자 이름을 받을 파라미터명 지정
    	
    	
	    http
	    	.csrf().disable() // disable 비활성화
	    	.authorizeHttpRequests() 
	    		.antMatchers("/").permitAll() //antMatchers
	    		.antMatchers("/user").authenticated()
	    		.antMatchers("/admin/**").hasRole("ADMIN")
	    		.antMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
	    	.and()
	    		.formLogin()
	    			.loginPage("/loginForm")
	    			.loginProcessingUrl("/login") // login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행
	    			.defaultSuccessUrl("/") // 성공시 url 호출 -> 하지만 특정페이지 ex)/user url 요청하면 로그인 성공시 /user 페이지 호출
	    			.usernameParameter("username") // loginForm name명 일치해야함
		    .and()
		    	.oauth2Login()
		    		.loginPage("/loginForm")
		    		.userInfoEndpoint()
		    		.userService(principalOauth2UserService); // 구글 로그인이 완료된 뒤의 후처리가 필요 -> 엑세스토큰 + 사용자프로필정보 받음
	    return http.build();
    
    }
    

    
}
