package account.security;

import account.account.Account;
import account.account.AccountRepository;
import account.account.UserDetailsServiceImpl;
import account.log.SecurityLog;
import account.log.SecurityLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class CustomBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
	@Autowired
	SecurityLogService securityLogService;
	@Autowired
	AccountRepository accountRepository;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
											 AuthenticationException authException) throws IOException {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		String enteredCredentialsString = new String(Base64.getDecoder().decode(request.getHeader("authorization").substring(6).getBytes()));
		String email = enteredCredentialsString.substring(0, enteredCredentialsString.indexOf(":"));
		Account account = accountRepository.findByEmail(email);
		String message;
		if(account == null) {
			message = "You are not authorized to access this endpoint";
			securityLogService.saveLog(
				new SecurityLog(new Date(), "LOGIN_FAILED", email, request.getRequestURI(), request.getRequestURI())
			);
		} else {
			int newFailAttempts = account.getFailedAttempt() + 1;
			account.setFailedAttempt(newFailAttempts);
			if(!account.getAuthorities().contains("ROLE_ADMINISTRATOR") && account.getFailedAttempt() < UserDetailsServiceImpl.MAX_FAILED_ATTEMPTS) {
				securityLogService.saveLog(
					new SecurityLog(new Date(), "LOGIN_FAILED", email, request.getRequestURI(), request.getRequestURI())
				);
			} else if (!account.getAuthorities().contains("ROLE_ADMINISTRATOR") && account.getFailedAttempt() == UserDetailsServiceImpl.MAX_FAILED_ATTEMPTS){
				securityLogService.saveLog(
					new SecurityLog(new Date(), "LOGIN_FAILED", email, request.getRequestURI(), request.getRequestURI())
				);
				securityLogService.saveLog(
					new SecurityLog(new Date(), "BRUTE_FORCE", email, request.getRequestURI(), request.getRequestURI())
				);
				account.setAccountNonLocked(false);
				securityLogService.saveLog(
					new SecurityLog(new Date(), "LOCK_USER", email, "Lock user " + email, "/api/admin/user/access")
				);
			} else if(account.getAuthorities().contains("ROLE_ADMINISTRATOR")){
				securityLogService.saveLog(
					new SecurityLog(new Date(), "LOGIN_FAILED", email, request.getRequestURI(), request.getRequestURI())
				);
			}
			accountRepository.save(account);

			message = account.isAccountNonLocked()
				? "You are not authorized to access this endpoint"
				: "User account is locked";
		}

		final Map<String, Object> body = new HashMap<>();
		body.put("timestamp", String.valueOf(LocalDateTime.now()));
		body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
		body.put("error", "Unauthorized");
		body.put("message", message);
		body.put("path", request.getRequestURI());


		final ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), body);
	}

	@Override
	public void afterPropertiesSet() {
		setRealmName("Baeldung");
		super.afterPropertiesSet();
	}
}
