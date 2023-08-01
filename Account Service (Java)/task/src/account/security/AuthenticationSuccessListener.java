package account.security;

import account.account.Account;
import account.account.AccountRepository;
import account.account.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessListener implements ApplicationListener<ApplicationEvent> {

	@Autowired
	AccountRepository accountRepository;

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if(event instanceof AuthenticationSuccessEvent) {
			UserDetailsImpl details = (UserDetailsImpl)((AuthenticationSuccessEvent) event).getAuthentication().getPrincipal();
			String email = details.getEmail();
			Account account = accountRepository.findByEmail(email);
			account.setFailedAttempt(0);
			accountRepository.save(account);
		}
	}
}
