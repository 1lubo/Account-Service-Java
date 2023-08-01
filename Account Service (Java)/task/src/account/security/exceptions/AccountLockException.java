package account.security.exceptions;

public class AccountLockException extends RuntimeException{
	public AccountLockException(String msg) {
		super(msg);
	}
}
