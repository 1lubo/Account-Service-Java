package account.security;

import account.account.AccountRepository;
import account.log.SecurityLog;
import account.log.SecurityLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
	@Autowired
	SecurityLogService securityLogService;
	@Autowired
	AccountRepository accountRepository;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);

		final Map<String, Object> body = new HashMap<>();
		body.put("timestamp", String.valueOf(LocalDateTime.now()));
		body.put("status", HttpServletResponse.SC_FORBIDDEN);
		body.put("error", "Forbidden");
		body.put("message", "Access Denied!");
		body.put("path", request.getRequestURI());


		final ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), body);

		String enteredCredentialsString = new String(Base64.getDecoder().decode(request.getHeader("authorization").substring(6).getBytes()));
		String email = enteredCredentialsString.substring(0, enteredCredentialsString.indexOf(":"));
		securityLogService.saveLog(
			new SecurityLog(new Date(), "ACCESS_DENIED", email, request.getRequestURI(), request.getRequestURI())
		);
	}
}
