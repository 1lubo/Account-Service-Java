package account.account;

import account.group.Group;
import account.payroll.Payroll;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

@Entity
public class Account {

	static final String ACME_EMAIL_REGEX = "^[\\w\\-\\.]+@acme.com";
//	static final String EMAIL_REGEX = "^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,}$";
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@NotBlank(message = "Name must not be blank")
	@NotNull
	private String name;
	@NotBlank(message = "Lastname must not be blank")
	@NotNull
	private String lastname;
	@NotBlank(message = "Email must not be blank")
	@Email(message = "Valid Acme email required", regexp = ACME_EMAIL_REGEX)
	private String email;

	@NotBlank
	@Size(min = 12, message = "Password must be at least 12 characters")
	private String password;

	@Column(name = "account_non_locked")
	private boolean accountNonLocked;

	@Column(name = "failed_attempt")
	private int failedAttempt;

	@Column(name = "lock_time")
	private Date lockTime;
	@OneToMany(mappedBy="account")
	private List<Payroll> payrolls = new ArrayList<>();

	@ManyToMany(cascade = {
		CascadeType.PERSIST,
		CascadeType.MERGE
	}, fetch = FetchType.EAGER)
	@JoinTable( name = "account_groups",
	joinColumns = @JoinColumn(name = "account_id"),
	inverseJoinColumns = @JoinColumn(name = "group_id"))
	private Set<Group> accountGroups = new HashSet<>();

	public Account(){}
	public Account(long id, String firstName, String lastname, String email, String password){
		this.id = id;
		this.name = firstName;
		this.lastname = lastname;
		this.email = email;
		this.password = password;
		this.accountNonLocked = true;
		this.failedAttempt = 0;
		this.lockTime = null;
	}

	public long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	public String getLastname() {
		return lastname;
	}

	public String getPassword(){
		return password;
	}

	public void setPassword(String password) {this.password = password;}


	public void setEmail(String email) {
		this.email = email;
	}

	public List<Payroll> getPayrolls() {
		return payrolls;
	}

	public Set<Group> getAccountGroups() {
		return accountGroups;
	}

	public void setAccountGroups(Set<Group> accountGroups) {
		this.accountGroups = accountGroups;
	}

	public ArrayList<String> getGroupsInArray(){
		ArrayList<String> groupsArray = new ArrayList<>();
		for(Group group : accountGroups){
			groupsArray.add(group.getName());
		}
		return groupsArray;
	}

	public void addAccountGroup(Group group) {
		this.accountGroups.add(group);
	}

	public void removeAccountGroup(Group group) { this.accountGroups.remove(group);}

	public ArrayList<String> getAuthorities (){
		Set<Group> accountGroups = getAccountGroups();
		ArrayList<String> roles = new ArrayList<>();
		Collection<GrantedAuthority> authorities = new ArrayList<>(accountGroups.size());
		for(Group accountGroup: accountGroups){
			authorities.add(new SimpleGrantedAuthority(accountGroup.getCode().toUpperCase()));
		}
		for(GrantedAuthority auth : authorities){
			roles.add("ROLE_"+auth.getAuthority());
		}
		Collections.sort(roles);
		return roles;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public Date getLockTime() {
		return lockTime;
	}

	public int getFailedAttempt() {
		return failedAttempt;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public void setFailedAttempt(int failedAttempt) {
		this.failedAttempt = failedAttempt;
	}

	public void setLockTime(Date lockTime) {
		this.lockTime = lockTime;
	}

	public AccountDTO convertToAccountDto() { return new AccountDTO(this.id, this.name, this.lastname, this.email, getAuthorities());}

}
