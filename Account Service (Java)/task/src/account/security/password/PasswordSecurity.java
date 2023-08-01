package account.security.password;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public class PasswordSecurity {
	private static final List<String> BREACHED_PASSWORDS = new ArrayList<>(List.of("PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
		"PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
		"PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember"));


	public static boolean passwordBreached(String password) {
		return BREACHED_PASSWORDS.contains(password);
	}

	public static boolean passwordMatch(String newPassword, String oldPasswordHash) {
		return new BCryptPasswordEncoder().matches(newPassword, oldPasswordHash);
	}
}
