package account.account;

public class ChangeAccountLockDTO {
	private String user;
	private String operation;

	public ChangeAccountLockDTO(){}

	public String getOperation() {
		return operation;
	}

	public String getUser() {
		return user;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public void setUser(String user) {
		this.user = user;
	}
}
