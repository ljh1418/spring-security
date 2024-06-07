package com.example.security1.config.auth.oauth.provider;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo{
	
	private Map<String,Object> attributes; // oauth2User.getAttributes()
	
	
	// 생성자 만드는 이유
	// private 변수인 attributes에 접근하기위해 
	
	
	public NaverUserInfo(Map<String,Object> attributes ) {
		this.attributes = attributes;
		
	}
	
	@Override
	public String getProviderId() {
		return (String) attributes.get("id");
	}

	@Override
	public String getProvider() {
		return "naver";
	}

	@Override
	public String getEmail() {
		return (String) attributes.get("email");
	}

	@Override
	public String getName() {
		return (String) attributes.get("name");
	}

}
