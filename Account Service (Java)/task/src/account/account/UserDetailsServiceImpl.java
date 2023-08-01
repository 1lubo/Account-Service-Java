package account.account;

import account.group.Group;
import account.group.GroupRepository;
import account.log.SecurityLog;
import account.log.SecurityLogService;
import account.security.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	public static final int MAX_FAILED_ATTEMPTS = 5;
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	GroupRepository groupRepository;

	@Autowired
	SecurityLogService securityLogService;

	public void createRoles(){
		groupRepository.save(new Group(1, "ADMINISTRATOR","Administrator Group"));
		groupRepository.save(new Group(2, "USER", "Business Group"));
		groupRepository.save(new Group(3, "ACCOUNTANT", "Business Group"));
		groupRepository.save(new Group(4, "AUDITOR", "Business Group"));
	}


	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Account account = accountRepository.findByEmail(email.toLowerCase());

		if(account == null){
			throw new UsernameNotFoundException("User not found: " + email);
		}
		return new UserDetailsImpl(account);
	}

	public Account findByUsername(String email) {
		return accountRepository.findByEmail(email);
	}
	public Account findById(long accountId) { return accountRepository.findById(accountId);}
	public void save(Account account){
		if(account.getAccountGroups().isEmpty()) {
			updateAccountGroup(account);
		}
		accountRepository.save(account);
	}

	public List<AccountDTO> findAll() {
		List<AccountDTO> allAccountsDTO = new ArrayList<>();
		for(Account acc : accountRepository.findAll()){
			allAccountsDTO.add(acc.convertToAccountDto());
		}
		return allAccountsDTO;
	}

	public boolean doesNotExistByEmail(String email) { return !accountRepository.existsByEmail(email);}
	private void updateAccountGroup(Account account) {
		if(accountRepository.findAll().size() < 1) {
			account.addAccountGroup(groupRepository.findByCode("ADMINISTRATOR"));
		} else {
			account.addAccountGroup(groupRepository.findByCode("USER"));
		}
	}

	public void deleteAccount(String email, Authentication authentication){
		Account account = accountRepository.findByEmail(email.toLowerCase());
		if(account == null){
			throw new AccountNotFoundException("User not found!");
		}
		UserDetailsImpl details = (UserDetailsImpl) authentication.getPrincipal();
		if(details.getEmail().matches(account.getEmail())) {
			throw new AccountDeletionException("Can't remove ADMINISTRATOR role!");
		}
		accountRepository.delete(account);
		securityLogService.saveLog(
			new SecurityLog(new Date(), "DELETE_USER", details.getEmail(), account.getEmail(), "/api/admin/user")
		);
	}

	public AccountDTO changeAccountRole(String email, String role, String operation, Authentication authentication){
		UserDetailsImpl details = (UserDetailsImpl) authentication.getPrincipal();
		Account account = accountRepository.findByEmail(email.toLowerCase());
		if(account == null){
			throw new AccountNotFoundException("User not found!");
		}
		Group group = groupRepository.findByCode(role);
		if(group == null){
			throw new RoleNotFoundException("Role not found!");
		}
		if(!account.getAccountGroups().contains(group) && operation.matches("REMOVE")){
			throw new AccountRoleUpdateException("The user does not have a role!");
		}
		if(role.matches("ADMINISTRATOR") && operation.matches("REMOVE")){
			throw new AccountRoleUpdateException("Can't remove ADMINISTRATOR role!");
		}
		if(account.getAccountGroups().contains(group) && account.getAccountGroups().size() == 1 && operation.matches("REMOVE")){
			throw new AccountRoleUpdateException("The user must have at least one role!");
		}
		if(operation.matches("GRANT") && !((Group) account.getAccountGroups().toArray()[0]).getName().matches(group.getName())){
			throw new AccountRoleUpdateException("The user cannot combine administrative and business roles!");
		}
		if(operation.matches("GRANT")) {
			account.addAccountGroup(group);
			securityLogService.saveLog(
				new SecurityLog(new Date(), "GRANT_ROLE",details.getEmail() , "Grant role " + role + " to " + account.getEmail(), "/api/admin/user/role")
			);
		} else {
			account.removeAccountGroup(group);
			securityLogService.saveLog(
				new SecurityLog(new Date(), "REMOVE_ROLE",details.getEmail() , "Remove role " + role + " from " + account.getEmail(), "/api/admin/user/role")
			);
		}

		accountRepository.save(account);

		return account.convertToAccountDto();
	}

	public void changeAccountLock(ChangeAccountLockDTO accountLockDTO, Authentication authentication){
		HashMap<String, Boolean> operationBooleanHash = new HashMap<>() {{
			put("LOCK", false);
			put("UNLOCK", true);
		}};
		Account account = accountRepository.findByEmail(accountLockDTO.getUser().toLowerCase());
		if(account == null){
			throw new AccountNotFoundException("User not found!");
		}
		if(!operationBooleanHash.containsKey(accountLockDTO.getOperation())){
			throw new AccountLockException("Unknown operation: " + accountLockDTO.getOperation());
		}
		if(account.getAccountGroups().contains(groupRepository.findByCode("ADMINISTRATOR")) &&
		accountLockDTO.getOperation().matches("LOCK")) {
			throw new AccountLockException("Can't lock the ADMINISTRATOR!");
		}
		if(account.isAccountNonLocked() == operationBooleanHash.get(accountLockDTO.getOperation())){
			throw new AccountLockException("User is already " + accountLockDTO.getOperation().toLowerCase() + "ed");
		}
		UserDetailsImpl details = (UserDetailsImpl) authentication.getPrincipal();
		account.setAccountNonLocked(operationBooleanHash.get(accountLockDTO.getOperation()));
		if(accountLockDTO.getOperation().matches("LOCK")){
			account.setLockTime(new Date());
			securityLogService.saveLog(
				new SecurityLog(new Date(), "LOCK_USER", details.getEmail() , "Lock user " + account.getEmail(), "/api/admin/user/access")
			);
		} else {
			account.setLockTime(null);
			account.setFailedAttempt(0);
			securityLogService.saveLog(
				new SecurityLog(new Date(), "UNLOCK_USER", details.getEmail() , "Unlock user " + account.getEmail(), "/api/admin/user/access")
			);
		}
		accountRepository.save(account);
	}

}
