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
public class InsertNewProductRequest {
	private Double price;
	private String description;
	private String title;

	public static boolean validateInsertNewProductRequestJsonFields(InsertNewProductRequest insertNewProductRequest) {

		if (insertNewProductRequest.getDescription() != null) {
			if (!insertNewProductRequest.getDescription().matches(descriptionRegex)) {
				System.out.println("Json field \"description\" : " + insertNewProductRequest.getDescription() + " is invalid");
				return false;
			}
		}

		if (insertNewProductRequest.getTitle() != null) {
			if (!insertNewProductRequest.getTitle().matches(titleRegex)) {
				System.out.println("Json field \"title\" : " + insertNewProductRequest.getTitle() + " is invalid");
				return false;
			}
		}

		if (insertNewProductRequest.getPrice() != null) {
			if (!insertNewProductRequest.getPrice().toString().matches(priceRegex)) {
				System.out.println("Json field \"price\" : " + insertNewProductRequest.getPrice() + " is invalid");
				return false;
			}
		}

		return  true;
	}
}
