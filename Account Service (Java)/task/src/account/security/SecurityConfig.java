package account.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired private CustomBasicAuthenticationEntryPoint authenticationEntryPoint;
	@Bean
	public CustomAccessDeniedHandler accessDeniedHandler(){
		return new CustomAccessDeniedHandler();
	}
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests()
				.and().httpBasic()
				.authenticationEntryPoint(authenticationEntryPoint);
		http
			.csrf().disable().headers().frameOptions().disable()
			.and()
			.authorizeHttpRequests(auth -> {
				auth.requestMatchers(HttpMethod.POST, "/actuator/**").permitAll();
				auth.requestMatchers(HttpMethod.POST,"/api/auth/signup").permitAll();
				auth.requestMatchers(HttpMethod.POST, "/api/auth/changepass").hasAnyAuthority("USER", "ACCOUNTANT", "ADMINISTRATOR");
				auth.requestMatchers(HttpMethod.GET, "/api/empl/payment").hasAnyAuthority("USER", "ACCOUNTANT");
				auth.requestMatchers(HttpMethod.POST, "/api/acct/payments").hasAuthority("ACCOUNTANT");
				auth.requestMatchers(HttpMethod.PUT, "/api/acct/payments").hasAuthority("ACCOUNTANT");
				auth.requestMatchers(HttpMethod.GET,"/api/admin/user/").hasAuthority("ADMINISTRATOR");
				auth.requestMatchers(HttpMethod.PUT,"/api/admin/user/role").hasAuthority("ADMINISTRATOR");
				auth.requestMatchers(HttpMethod.DELETE,"/api/admin/user/**").hasAuthority("ADMINISTRATOR");
				auth.requestMatchers(HttpMethod.PUT, "/api/admin/user/access/").hasAuthority("ADMINISTRATOR");
				auth.requestMatchers(HttpMethod.GET, "/api/security/events/").hasAuthority("AUDITOR");
				auth.anyRequest().authenticated();
			})
			.exceptionHandling(configurer -> {
//				configurer.authenticationEntryPoint(authenticationEntryPoint);
				configurer.accessDeniedHandler(accessDeniedHandler());

			})
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		return http.build();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.debug(false)
			.ignoring().requestMatchers(new AntPathRequestMatcher("/h2-console/**"));
	}
}
