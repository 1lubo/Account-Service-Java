package account.security.exceptions;

public class RoleNotFoundException extends RuntimeException{
		public RoleNotFoundException(String msg) {
			super(msg);
		}
}
