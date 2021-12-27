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

	public static boolean validateRegisterRequestJsonFields(RegisterRequest registerRequest) {

		if (!registerRequest.getFirstName().matches(firstNameRegex) || registerRequest.getFirstName() == null) {
			System.out.println("Json field \"firstName\" : " + registerRequest.getFirstName() + " is invalid");
			return false;
		}

		if (!registerRequest.getSecondName().matches(secondNameRegex) || registerRequest.getSecondName() == null) {
			System.out.println("Json field \"secondName\" : " + registerRequest.getSecondName() + " is invalid");
			return false;
		}

		if (!registerRequest.getEmail().matches(emailRegex) || registerRequest.getEmail() == null) {
			System.out.println("Json field \"email\" : " + registerRequest.getEmail() + " is invalid");
			return false;
		}

		if (!registerRequest.getUserPassword().matches(passwordRegex) || registerRequest.getUserPassword() == null) {
			System.out.println("Json field \"password\" : " + registerRequest.getUserPassword() + " is invalid");
			return false;
		}

		if (!registerRequest.getUserConfirmPassword().matches(passwordRegex) || registerRequest.getUserConfirmPassword() == null) {
			System.out.println("Json field \"confirmPassword\" : " + registerRequest.getUserConfirmPassword() + " is invalid");
			return false;
		}

		if(!registerRequest.getUserConfirmPassword().equals(registerRequest.getUserPassword())) {
			System.out.println("Json field \"password and confirmPassword\" : " + registerRequest.getUserPassword() + " and " + registerRequest.getUserConfirmPassword() + " not match");
			return false;
		}

		return  true;
	}
}
