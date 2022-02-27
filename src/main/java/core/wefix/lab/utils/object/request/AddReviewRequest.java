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
public class AddReviewRequest {
	private String email_receive;
	private String content;
	private Long star;

	public static boolean validateRegisterRequestJsonFields(AddReviewRequest addReviewRequest) {

		if (!addReviewRequest.getEmail_receive().matches(emailRegex) || addReviewRequest.getEmail_receive() == null) {
			System.out.println("Json field \"email_receive\" : " + addReviewRequest.getEmail_receive() + " is invalid");
			return false;
		}

		if (!addReviewRequest.getStar().toString().matches(starRegex) || addReviewRequest.getStar() == null) {
			System.out.println("Json field \"star\" : " + addReviewRequest.getStar() + " is invalid");
			return false;
		}

		if (addReviewRequest.getContent() != null) {
			if (!addReviewRequest.getContent().matches(contentReviewRegex)){
				System.out.println("Json field \"content\" : " + addReviewRequest.getContent() + " is invalid");
				return false;
			}
		}

		return true;
	}
}
