package account.group;

import account.account.Account;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "principle_groups")
public class Group {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(unique = true, nullable = false)
	private String code;
	private String name;

	@ManyToMany(mappedBy = "accountGroups")
	private Set<Account> accounts;

	public Group(){}
	public Group(long id, String code, String name){
		this.id = id;
		this.code = code;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public Set<Account> getAccounts() {
		return accounts;
	}

	public long getId() {
		return id;
	}
}
