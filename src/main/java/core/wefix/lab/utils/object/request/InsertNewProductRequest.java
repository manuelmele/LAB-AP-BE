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

		if (!insertNewProductRequest.getDescription().matches(descriptionRegex) || insertNewProductRequest.getDescription() == null) {
			System.out.println("Json field \"description\" : " + insertNewProductRequest.getDescription() + " is invalid");
			return false;
		}

		if (!insertNewProductRequest.getTitle().matches(titleRegex) || insertNewProductRequest.getTitle() == null) {
			System.out.println("Json field \"title\" : " + insertNewProductRequest.getTitle() + " is invalid");
			return false;
		}

		if (!insertNewProductRequest.getPrice().toString().matches(priceRegex) || insertNewProductRequest.getPrice() == null) {
			System.out.println("Json field \"price\" : " + insertNewProductRequest.getPrice() + " is invalid");
			return false;
		}

		return  true;
	}
}
