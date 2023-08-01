package account.security.exceptions;

public class AccountDeletionException extends RuntimeException{
	public AccountDeletionException (String msg) {
		super(msg);
	}
}
