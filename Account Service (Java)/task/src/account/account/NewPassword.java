package account.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NewPassword {

	@NotBlank
	@Size(min = 12, message = "Password length must be 12 chars minimum!")
	private String new_password;

	public NewPassword(){}

	public NewPassword(String new_password){
		this.new_password = new_password;
	}

	public String getNew_password() {
		return new_password;
	}
}
