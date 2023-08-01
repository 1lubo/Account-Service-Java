package account.controller;

import account.account.AccountDTO;
import account.account.ChangeAccountLockDTO;
import account.account.ChangeAccountRoleDTO;
import account.account.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AdministratorController {

	@Autowired
	UserDetailsServiceImpl accountDetailsService;

	@GetMapping("api/admin/user/")
	public ResponseEntity<List<AccountDTO>> getAccountsInformation() {
		return new ResponseEntity<>(accountDetailsService.findAll(), HttpStatus.OK);
	}

	@DeleteMapping("api/admin/user/{email}")
	public ResponseEntity<Object> deleteAccount(@PathVariable("email") String userEmail, Authentication authentication){
		accountDetailsService.deleteAccount(userEmail, authentication);
		final Map<String, Object> body = new HashMap<>();
		body.put("user", userEmail);
		body.put("status", "Deleted successfully!");
		return new ResponseEntity<>(body, HttpStatus.OK);
	}

	@PutMapping("api/admin/user/role")
	public ResponseEntity<Object> changeAccountRole(@RequestBody ChangeAccountRoleDTO roleDTO, Authentication authentication){
		AccountDTO account = accountDetailsService.changeAccountRole(roleDTO.getUser(), roleDTO.getRole(), roleDTO.getOperation(), authentication);
		return new ResponseEntity<>(account, HttpStatus.OK);
	}

	@PutMapping("api/admin/user/access")
	public ResponseEntity<Object> changeAccountLock(@RequestBody ChangeAccountLockDTO accountLockDTO, Authentication authentication) {
		accountDetailsService.changeAccountLock(accountLockDTO, authentication);
		String operationResult = accountLockDTO.getOperation().toLowerCase() + "ed!";
		final Map<String, Object> body = new HashMap<>();
		body.put("status", "User " + accountLockDTO.getUser().toLowerCase() + " " + operationResult);
		return new ResponseEntity<>(body, HttpStatus.OK);
	}
}
