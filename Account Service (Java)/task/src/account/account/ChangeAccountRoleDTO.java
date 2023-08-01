package account.account;

public class ChangeAccountRoleDTO {
	private String user;
	private String role;
	private String operation;

	public ChangeAccountRoleDTO(){}

	public void setRole(String role) {
		this.role = role;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getRole() {
		return role;
	}

	public String getUser() {
		return user;
	}

	public String getOperation() {
		return operation;
	}
}
