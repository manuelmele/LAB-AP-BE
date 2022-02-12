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
			String validationError = "Json field \"firstName\" : " + registerRequest.getFirstName() + " is invalid";
			System.out.println(validationError);
			return validationError;
		}

		if (!registerRequest.getSecondName().matches(secondNameRegex) || registerRequest.getSecondName() == null) {
			String validationError = "Json field \"secondName\" : " + registerRequest.getSecondName() + " is invalid";
			System.out.println(validationError);
			return validationError;
		}

		if (!registerRequest.getEmail().matches(emailRegex) || registerRequest.getEmail() == null) {
			String validationError = "Json field \"email\" : " + registerRequest.getEmail() + " is invalid";
			System.out.println(validationError);
			return validationError;
		}

		if (!registerRequest.getUserPassword().matches(passwordRegex) || registerRequest.getUserPassword() == null) {
			String validationError = "Json field \"password\" : " + registerRequest.getUserPassword() + " is invalid";
			System.out.println(validationError);
			return validationError;
		}

		if (!registerRequest.getUserConfirmPassword().matches(passwordRegex) || registerRequest.getUserConfirmPassword() == null) {
			String validationError = "Json field \"confirmPassword\" : " + registerRequest.getUserConfirmPassword() + " is invalid";
			System.out.println(validationError);
			return validationError;
		}

		if(!registerRequest.getUserConfirmPassword().equals(registerRequest.getUserPassword())) {
			String validationError = "Json field \"password and confirmPassword\" : " + registerRequest.getUserPassword() + " and " + registerRequest.getUserConfirmPassword() + " not match";
			System.out.println(validationError);
			return validationError;
		}

		return "";
	}
}
