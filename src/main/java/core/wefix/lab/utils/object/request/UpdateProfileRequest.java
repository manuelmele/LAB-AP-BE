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
public class UpdateProfileRequest {
	private String firstName;
	private String secondName;
	private String bio;
	private byte[] photoProfile;

	public static boolean validateUpdateProfileRequestJsonFields(UpdateProfileRequest updateProfileRequest) {

		if (!updateProfileRequest.getFirstName().matches(firstNameRegex) || updateProfileRequest.getFirstName() == null) {
			System.out.println("Json field \"firstName\" : " + updateProfileRequest.getFirstName() + " is invalid");
			return false;
		}

		if (!updateProfileRequest.getSecondName().matches(secondNameRegex) || updateProfileRequest.getSecondName() == null) {
			System.out.println("Json field \"secondName\" : " + updateProfileRequest.getSecondName() + " is invalid");
			return false;
		}

		if (!updateProfileRequest.getBio().matches(bioRegex) || updateProfileRequest.getBio() == null) {
			System.out.println("Json field \"bio\" : " + updateProfileRequest.getBio() + " is invalid");
			return false;
		}

		return  true;
	}
}


