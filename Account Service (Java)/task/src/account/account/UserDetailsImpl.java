package account.account;

import account.group.Group;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class UserDetailsImpl implements UserDetails {

	private final long id;
	private final String name;
	private final String lastname;
	private final String email;
	private final String password;
	private final boolean isAccountNonLocked;
	private final Collection<GrantedAuthority> authorities;

	public UserDetailsImpl(Account account){
		id = account.getId();
		email = account.getEmail();
		name = account.getName();
		lastname = account.getLastname();
		password = account.getPassword();
		isAccountNonLocked = account.isAccountNonLocked();
		authorities = getAuthoritiesCollection(account);
	}


	public Collection<GrantedAuthority> getAuthoritiesCollection(Account account) {
		Set<Group> accountGroups = account.getAccountGroups();
		Collection<GrantedAuthority> authorities = new ArrayList<>(accountGroups.size());
		for(Group accountGroup: accountGroups){
			authorities.add(new SimpleGrantedAuthority(accountGroup.getCode().toUpperCase()));
		}
		return authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return name + "::" + lastname;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return isAccountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public String getEmail() {
		return email;
	}

	public long getId() {
		return id;
	}
}
