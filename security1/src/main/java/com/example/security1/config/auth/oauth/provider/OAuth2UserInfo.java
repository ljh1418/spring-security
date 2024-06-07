package com.example.security1.config.auth.oauth.provider;

// OAuth2.0 제공자들 마다 응답해주는 속성값이 달라서 공통으로 만듬
// ex) 구글getProviderId:sub, 네이버getProviderId:id

public interface OAuth2UserInfo {
	String getProviderId();
	String getProvider();
	String getEmail();
	String getName();
}
