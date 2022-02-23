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

	public static boolean validateUpdateProfileRequestJsonFields(UpdateProfileRequest updateProfileRequest) {

		if(updateProfileRequest.getFirstName() != null) {
			if (!updateProfileRequest.getFirstName().matches(firstNameRegex)) {
				System.out.println("Json field \"firstName\" : " + updateProfileRequest.getFirstName() + " is invalid");
				return false;
			}
		}

		if(updateProfileRequest.getSecondName() != null) {
			if (!updateProfileRequest.getSecondName().matches(secondNameRegex)) {
				System.out.println("Json field \"secondName\" : " + updateProfileRequest.getSecondName() + " is invalid");
				return false;
			}
		}

		if(updateProfileRequest.getBio() != null) {
			if (!updateProfileRequest.getBio().matches(bioRegex)) {
				System.out.println("Json field \"bio\" : " + updateProfileRequest.getBio() + " is invalid");
				return false;
			}
		}

		return  true;
	}
}


