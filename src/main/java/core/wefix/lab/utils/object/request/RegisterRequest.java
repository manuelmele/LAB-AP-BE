package core.wefix.lab.utils.object.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static core.wefix.lab.utils.object.Regex.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
	private String firstName;
	private String secondName;
	private String email;
	private String userPassword;
	private String userConfirmPassword;

	public static String validateRegisterRequestJsonFields(RegisterRequest registerRequest) {

		if (!registerRequest.getFirstName().matches(firstNameRegex) || registerRequest.getFirstName() == null) {
			String validationError = "Json field 'firstName' is invalid";
			System.out.println(validationError);
			return validationError;
		}

		if (!registerRequest.getSecondName().matches(secondNameRegex) || registerRequest.getSecondName() == null) {
			String validationError = "Json field 'surname' is invalid";
			System.out.println(validationError);
			return validationError;
		}

		if (!registerRequest.getEmail().matches(emailRegex) || registerRequest.getEmail() == null) {
			String validationError = "Invalid email";
			System.out.println(validationError);
			return validationError;
		}

		if (!registerRequest.getUserPassword().matches(passwordRegex) || registerRequest.getUserPassword() == null) {
			String validationError = "Weak password";
			System.out.println(validationError);
			return validationError;
		}

		if(!registerRequest.getUserConfirmPassword().equals(registerRequest.getUserPassword())) {
			String validationError = "Passwords don't match";
			System.out.println(validationError);
			return validationError;
		}

		return "";
	}
}
