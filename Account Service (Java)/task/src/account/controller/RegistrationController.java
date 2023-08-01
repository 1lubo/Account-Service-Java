package account.controller;

import account.account.Account;
import account.account.NewPassword;
import account.account.UserDetailsImpl;
import account.account.UserDetailsServiceImpl;
import account.log.SecurityLog;
import account.log.SecurityLogService;
import account.security.exceptions.AccountValidationException;
import account.security.exceptions.UserAlreadyExistsException;
import account.security.exceptions.NewPasswordValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import static account.security.password.PasswordSecurity.passwordBreached;
import static account.security.password.PasswordSecurity.passwordMatch;


@RestController
public class RegistrationController {
	@Autowired
	UserDetailsServiceImpl accountDetailsService;
	@Autowired
	SecurityLogService securityLogService;

	@PostMapping("api/auth/signup")
	public ResponseEntity<Object> signup(@RequestBody @Valid Account account, BindingResult errors) {
		if (errors.hasErrors()) {
			throw new AccountValidationException(errors.getFieldErrors().get(0).getDefaultMessage());
		}
		accountDetailsService.createRoles();
		Account alreadyExists = accountDetailsService.findByUsername(account.getEmail().toLowerCase());
		if (alreadyExists == null) {
			if(passwordBreached(account.getPassword())){
				throw new NewPasswordValidationException("The password is in the hacker's database!");
			}
			account.setEmail(account.getEmail().toLowerCase());
			account.setPassword(new BCryptPasswordEncoder(13).encode(account.getPassword()));
			account.setAccountNonLocked(true);
			accountDetailsService.save(account);
			securityLogService.saveLog(
				new SecurityLog(new Date(), "CREATE_USER", "Anonymous", account.getEmail(), "/api/auth/signup")
			);
			return new ResponseEntity<>(account.convertToAccountDto(), HttpStatus.OK);
		} else {
			throw new UserAlreadyExistsException("User exist!");
		}
	}

	@PostMapping("api/auth/changepass")
	public ResponseEntity<Object> changePassword(@RequestBody @Valid NewPassword new_password, Authentication auth, BindingResult errors) {
		if(errors.hasErrors()){
			System.out.println(errors.getFieldErrors());
		}
		UserDetailsImpl details = (UserDetailsImpl) auth.getPrincipal();
		if(passwordBreached(new_password.getNew_password())){
			throw new NewPasswordValidationException("The password is in the hacker's database!");
		}
		if(passwordMatch(new_password.getNew_password(), details.getPassword())){
			throw new NewPasswordValidationException("The passwords must be different!");
		}
		Account account = accountDetailsService.findById(details.getId());
		account.setPassword(new BCryptPasswordEncoder(13).encode(new_password.getNew_password()));
		accountDetailsService.save(account);
		securityLogService.saveLog(
			new SecurityLog(new Date(), "CHANGE_PASSWORD", account.getEmail(), account.getEmail(), "/api/auth/changepass")
		);
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objectNode = mapper.createObjectNode();
		objectNode.put("email", details.getEmail());
		objectNode.put("status", "The password has been updated successfully");
		return new ResponseEntity<>(objectNode, HttpStatus.OK);
	}
}
