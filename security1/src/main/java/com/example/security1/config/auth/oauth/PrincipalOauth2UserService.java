package com.example.security1.config.auth.oauth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.security1.config.auth.PrincipalDetails;
import com.example.security1.config.auth.oauth.provider.GoogleUserInfo;
import com.example.security1.config.auth.oauth.provider.NaverUserInfo;
import com.example.security1.config.auth.oauth.provider.OAuth2UserInfo;
import com.example.security1.model.User;
import com.example.security1.repository.UserRepository;


@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{
	
	@Autowired
	@Lazy
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	// 구글로 부터 받은 userRequest 데이터에 대한 후처리 함수
	// 함수 종료시 @AuthenticationPrincipal 어노테이션 만들어짐
	@Override 
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		
		
		System.out.println("getClientRegistration : " + userRequest.getClientRegistration()); // registrationId로 어떤 OAuth로 로그인 했는지 확인가능
		System.out.println("getAccessToken : " + userRequest.getAccessToken().getTokenValue()); //
		
		OAuth2User oauth2User = super.loadUser(userRequest); // super.loadUser(userRequest).getAttributes() 데이터로 회원가입을 강제로 진행 
		// 구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인 완료 -> code 리턴(OAuth-Client 라이브러리) -> AccessToken 요청
		// userRequest 정보 -> loadUser 함수 호출 -> 구글로부터 회원 프로필 받음
		System.out.println("getAttribute : " + super.loadUser(userRequest).getAttributes()); //
		System.out.println("getAttribute : "  + oauth2User.getAttributes());
		
		//회원가입 강제로 진행
		OAuth2UserInfo oAuth2UserInfo = null;
		if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
			System.out.println("구글 로그인 요청");
			oAuth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());
		}else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
			System.out.println("네이버 로그인 요청");
			oAuth2UserInfo = new NaverUserInfo((Map)oauth2User.getAttributes().get("response"));
		}else {
			System.out.println("구글이랑 네이버만 지원합니다...");
		}
		
		String provider   = oAuth2UserInfo.getProvider();
		String providerId = oAuth2UserInfo.getProviderId();
		String username   = provider+"_"+providerId;
		String password   = bCryptPasswordEncoder.encode("겟인데어");
		String email 	  = oAuth2UserInfo.getEmail();
		String role 	  =	"ROLE_USER";
		
		//회원가입체크
		User userEntity = userRepository.findByUsername(username);
		System.out.println("userEntity ::: " + userEntity);
		
		if(userEntity == null) {
			System.out.println("OAuth 로그인이 최초입니다.");
			userEntity =  User.builder()
					.username(username)
					.password(password)
					.email(email)
					.role(role)
					.provider(provider)
					.providerId(providerId)
					.build();
			userRepository.save(userEntity);
		}else {
			System.out.println("로그인을 이미 한적이 있습니다. 자동회원가입 완료");
		}
		
		//PrincipalDetails 클래스에서 OAuth2User 인터페이스를 임플리먼트하고 있기때문에 가능  
		return new PrincipalDetails(userEntity, oauth2User.getAttributes()); 
	}
}
