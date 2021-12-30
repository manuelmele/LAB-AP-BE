package core.wefix.lab.utils.object.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetCustomerResponse {
	private String firstName;
	private String secondName;
	private String email;
	private String bio;
	private byte[] photo_profile;
}
