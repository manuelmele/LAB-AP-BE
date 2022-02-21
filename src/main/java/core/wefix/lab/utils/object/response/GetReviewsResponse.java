package core.wefix.lab.utils.object.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetReviewsResponse {
	private String contentReview;
	private Long star;
	private String firstNameReviewer;
	private String lastNameReviewer;
}
