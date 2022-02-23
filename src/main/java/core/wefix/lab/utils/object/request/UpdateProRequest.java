package core.wefix.lab.utils.object.request;

import core.wefix.lab.utils.object.staticvalues.Category;
import core.wefix.lab.utils.object.staticvalues.CurrencyPayPal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static core.wefix.lab.utils.object.Regex.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProRequest {
	private Category category;
	private String pIva;
	private CurrencyPayPal currency;
	private Double price;
	private String identityCard;


	public static boolean validateUpdateProRequestJsonFields(UpdateProRequest updateProfileRequest) {

		if (!updateProfileRequest.getCategory().toString().matches(categoryRegex) || updateProfileRequest.getCategory() == null) {
			System.out.println("Json field \"category\" : " + updateProfileRequest.getCategory() + " is invalid");
			return false;
		}

		if (!updateProfileRequest.getPIva().matches(onlyNumberRegex) || updateProfileRequest.getPIva() == null) {
			System.out.println("Json field \"pIva\" : " + updateProfileRequest.getPIva() + " is invalid");
			return false;
		}

		if (!updateProfileRequest.getCurrency().toString().matches(currencyRegex) || updateProfileRequest.getCurrency() == null) {
			System.out.println("Json field \"currency\" : " + updateProfileRequest.getCurrency() + " is invalid");
			return false;
		}

		if (!updateProfileRequest.getPrice().toString().matches(priceRegex) || updateProfileRequest.getPrice() == null) {
			System.out.println("Json field \"price\" : " + updateProfileRequest.getPrice() + " is invalid");
			return false;
		}

		if (!updateProfileRequest.getIdentityCard().toString().matches(identityCardRegex) || updateProfileRequest.getIdentityCard() == null) {
			System.out.println("Json field \"identityCard\" : " + updateProfileRequest.getIdentityCard() + " is invalid");
			return false;
		}

		return  true;

	}

}




