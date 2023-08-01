package account.security.exceptions;

public class NewPasswordValidationException extends RuntimeException{
	public NewPasswordValidationException(String msg) {
		super(msg);
	}
}
