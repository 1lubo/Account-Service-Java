package account.account;

import java.util.ArrayList;

public class AccountDTO {

	private long id;
	private String name;
	private String lastname;
	private String email;

	private ArrayList<String> roles;

	public AccountDTO(){}

	public AccountDTO(long id, String name, String lastname, String email, ArrayList<String> roles){
		this.id = id;
		this.name = name;
		this.lastname = lastname;
		this.email = email;
		this.roles = roles;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getLastname() {
		return lastname;
	}

	public ArrayList<String> getRoles() {
		return roles;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setRoles(ArrayList<String> roles) {
		this.roles = roles;
	}
}
