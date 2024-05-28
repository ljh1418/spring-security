package com.example.security1.config.auth.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{
	
	// 구글로 부터 받은 userRequest 데이터에 대한 후처리 함수
	@Override 
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("getClientRegistration : " + userRequest.getClientRegistration()); // registrationId로 어떤 OAuth로 로그인 했는지 확인가능
		System.out.println("getAccessToken : " + userRequest.getAccessToken().getTokenValue()); //
		// 구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인 완료 -> code 리턴(OAuth-Client 라이브러리) -> AccessToken 요청
		// userRequest 정보 -> loadUser 함수 호출 -> 구글로부터 회원 프로필 받음
		System.out.println("getAttribute : " + super.loadUser(userRequest).getAttributes()); //
		
		OAuth2User oauth2User = super.loadUser(userRequest);
		
		// super.loadUser(userRequest).getAttributes() 데이터로 회원가입을 강제로 진행
		
		
		return super.loadUser(userRequest);
	}
}
