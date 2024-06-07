Spring Security



2024-06-07
┌─────┐
|  securityConfig (field private com.example.security1.config.auth.oauth.PrincipalOauth2UserService com.example.security1.config.SecurityConfig.principalOauth2UserService)
↑     ↓
|  principalOauth2UserService (field private org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder com.example.security1.config.auth.oauth.PrincipalOauth2UserService.bCryptPasswordEncoder)
└─────┘

원인 : 순환 의존성(Circular Dependency)이 발생
오류내용 : SecurityConfig 클래스가 PrincipalOauth2UserService를 필요로 하고, PrincipalOauth2UserService 클래스가 BCryptPasswordEncoder를 필요로 하는 상황, 이러한 순환 의존성은 Spring 컨텍스트가 빈을 생성할 때 서로를 참조하여 무한 루프에 빠지게 됩니다.

@Lazy 어노테이션
@Lazy 어노테이션을 사용하여 필요한 빈을 지연 초기화(Lazy Initialization)할 수 있습니다. 이렇게 하면 빈이 실제로 필요할 때까지 빈의 초기화를 지연시켜 순환 의존성을 피할 수 있습니다.


public class SecurityConfig{

	@Autowired
	@Lazy
	private PrincipalOauth2UserService principalOauth2UserService;
 
}
