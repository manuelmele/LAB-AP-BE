package core.wefix.lab.utils.object.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static core.wefix.lab.utils.object.Regex.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InsertNewMeetingRequest {
	private String emailWorker;
	private String emailCustomer;
	private String description;
	private String date;
	private String slot_time;

	public static boolean validateInsertNewMeetingRequestJsonFields(InsertNewMeetingRequest insertNewMeetingRequest) {

		if (!insertNewMeetingRequest.getDescription().matches(descriptionRegex) || insertNewMeetingRequest.getDescription() == null) {
			System.out.println("Json field \"description\" : " + insertNewMeetingRequest.getDescription() + " is invalid");
			return false;
		}

		if (!insertNewMeetingRequest.getDate().matches(dateRegex) || insertNewMeetingRequest.getDate() == null) {
			System.out.println("Json field \"date\" should be sent in this format dd/MM/yyyy");
			return false;
		}

		if (!insertNewMeetingRequest.getEmailWorker().matches(emailRegex) || insertNewMeetingRequest.getEmailWorker() == null) {
			System.out.println("Json field \"email worker\" : " + insertNewMeetingRequest.getEmailWorker() + " is invalid");
			return false;
		}

		if (!insertNewMeetingRequest.getEmailCustomer().matches(emailRegex) || insertNewMeetingRequest.getEmailCustomer() == null) {
			System.out.println("Json field \"email customer\" : " + insertNewMeetingRequest.getEmailCustomer() + " is invalid");
			return false;
		}

		return  true;
	}
}
