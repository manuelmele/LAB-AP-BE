package core.wefix.lab.utils.object.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetProductResponse {
	private byte[] image;
	private Double price;
	private String description;
	private String title;
}
