package core.wefix.lab.utils.object.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetProfileResponse {
	private String firstName;
	private String secondName;
	private String email;
	private String bio;
	private byte[] photoProfile;
	private String pIva;
	private String identityCardNumber;
}
