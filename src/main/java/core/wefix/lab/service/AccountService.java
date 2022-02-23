package core.wefix.lab.service;

import core.wefix.lab.entity.Account;
import core.wefix.lab.entity.Meeting;
import core.wefix.lab.entity.Product;
import core.wefix.lab.entity.Review;
import core.wefix.lab.repository.AccountRepository;
import core.wefix.lab.repository.MeetingRepository;
import core.wefix.lab.repository.ProductRepository;
import core.wefix.lab.repository.ReviewRepository;
import core.wefix.lab.service.jwt.JWTAuthenticationService;
import core.wefix.lab.service.jwt.JWTService;
import core.wefix.lab.utils.object.Regex;
import core.wefix.lab.utils.object.request.InsertNewMeetingRequest;
import core.wefix.lab.utils.object.request.UpdateProfileRequest;
import core.wefix.lab.utils.object.response.*;
import core.wefix.lab.utils.object.staticvalues.Category;
import core.wefix.lab.utils.object.staticvalues.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static core.wefix.lab.utils.object.Regex.*;
import static core.wefix.lab.utils.object.staticvalues.StaticObject.photoProfileBase;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AccountService {
	private final AccountRepository accountRepository;
	private final ReviewRepository reviewRepository;
	private final MeetingRepository meetingRepository;
	private final ProductRepository productRepository;
	private final JWTAuthenticationService authenticationService;

	/**
	 * Allows retrieving of all user data from his authentication
	 * @return an Account: all information about user logged
	 */
	public Account getCustomerOrWorkerInfo(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			User user = ((User) authentication.getPrincipal());
			if (!user.getAuthorities().contains(new SimpleGrantedAuthority(Role.Customer.name())) &&
					!user.getAuthorities().contains(new SimpleGrantedAuthority(Role.Worker.name())))
				throw new JWTService.TokenVerificationException();
			else if (user.getAuthorities().contains(new SimpleGrantedAuthority(Role.Customer.name())))
			return accountRepository.findByUserRoleAndEmail(Role.Customer, user.getUsername())
					.orElseThrow(JWTService.TokenVerificationException::new);
			else if (user.getAuthorities().contains(new SimpleGrantedAuthority(Role.Worker.name())))
				return accountRepository.findByUserRoleAndEmail(Role.Worker, user.getUsername())
						.orElseThrow(JWTService.TokenVerificationException::new);
		}
		throw new JWTService.TokenVerificationException();
	}


	/**
	 * Allows to retrieve all user profile data
	 * @return a GetCustomerResponse: all information to send as response for a certain user
	 */
	public GetProfileResponse getProfile(Long accountId) {
		getCustomerOrWorkerInfo();
		Double avgStar = reviewRepository.avgStar(accountId);
		Account account = accountRepository.findByAccountId(accountId);
		return new GetProfileResponse(account.getFirstName(),
				account.getSecondName(),
				account.getEmail(),
				account.getBio(),
				account.getPhotoProfile(),
				account.getPIva(),
				account.getIdentityCardNumber(),
				account.getUserRole(),
				account.getUserCategory(),
				avgStar);
	}

	public GetProfileResponse getWorkerProfile(String emailWorker) {
		// emailWorker validate
		if (!emailWorker.matches(emailRegex))
			throw new IllegalArgumentException("Invalid emailWorker");
		Account workerAccount = accountRepository.findByEmailAndUserRole(emailWorker, Role.Worker);
		if (workerAccount != null)
			return getProfile(workerAccount.getAccountId());
		else
			return new GetProfileResponse();
	}

	public List<GetReviewsResponse> getWorkerReviews(String emailWorker) {
		getCustomerOrWorkerInfo();
		// emailWorker validate
		if (!emailWorker.matches(emailRegex))
			throw new IllegalArgumentException("Invalid emailWorker");
		Account workerAccount = accountRepository.findByEmailAndUserRole(emailWorker, Role.Worker);
		List<Review> reviewsRetrieved;
		List<GetReviewsResponse> getReviewsResponse = new ArrayList<>();
		if (workerAccount != null) {
			reviewsRetrieved = reviewRepository.findByUserIdReceiveReview(workerAccount.getAccountId());
			for (Review review : reviewsRetrieved)
				getReviewsResponse.add(new GetReviewsResponse(
						review.getContent(),
						review.getStar(),
						workerAccount.getFirstName(),
						workerAccount.getSecondName()
				));
		}
		return getReviewsResponse;
	}

	public List<GetReviewsResponse> getCustomerReviews(String emailCustomer) {
		getCustomerOrWorkerInfo();
		// emailCustomer validate
		if (!emailCustomer.matches(emailRegex))
			throw new IllegalArgumentException("Invalid emailCustomer");
		Account account = accountRepository.findByEmailAndUserRole(emailCustomer, Role.Customer);
		List<Review> reviewsRetrieved;
		List<GetReviewsResponse> getReviewsResponse = new ArrayList<>();
		if (account != null) {
			reviewsRetrieved = reviewRepository.findByUserIdReceiveReview(account.getAccountId());
			for (Review review : reviewsRetrieved)
				getReviewsResponse.add(new GetReviewsResponse(
						review.getContent(),
						review.getStar(),
						account.getFirstName(),
						account.getSecondName()
				));
		}
		return getReviewsResponse;
	}

	public AvgReviewsResponse getWorkerAvgReviews(String emailWorker) {
		getCustomerOrWorkerInfo();
		// emailWorker validate
		if (!emailWorker.matches(emailRegex))
			throw new IllegalArgumentException("Invalid emailWorker");
		Account account = accountRepository.findByEmailAndUserRole(emailWorker, Role.Worker);
		Double avgStar;
		if (account != null)
			avgStar = reviewRepository.avgStar(account.getAccountId());
		else
			throw new IllegalArgumentException("Invalid emailWorker");
		return new AvgReviewsResponse(avgStar);
	}


	public List<GetMeetingResponse> getWorkerMeetings(String emailWorker) {
		getCustomerOrWorkerInfo();
		// emailWorker validate
		if (!emailWorker.matches(emailRegex))
			throw new IllegalArgumentException("Invalid emailWorker");
		Account workerAccount = accountRepository.findByEmailAndUserRole(emailWorker, Role.Worker);
		//System.out.println("worker account = " + workerAccount.getAccountId() + " " + workerAccount.getUserCategory());
		List<Meeting> meetingsRetrieved;
		List<GetMeetingResponse> getMeetingsResponse = new ArrayList<>();
		if (workerAccount != null) {
			meetingsRetrieved = meetingRepository.findByUserIdWorker(workerAccount.getAccountId());
			for (Meeting meeting : meetingsRetrieved){
				Account customerAccount = accountRepository.findByAccountId(meeting.getUserIdCustomerMeeting());
				getMeetingsResponse.add(new GetMeetingResponse(
						customerAccount.getFirstName(),
						customerAccount.getSecondName(),
						customerAccount.getEmail(),
						customerAccount.getPhotoProfile(),
						null, // category
						meeting.getDescriptionMeeting(),
						meeting.getDateMeeting(),
						meeting.getSlotTime(),
						meeting.getAcceptedMeeting(),
						meeting.getStartedMeeting(),
						meeting.getLatPosition(),
						meeting.getLngPosition()));
		}
		}
		return getMeetingsResponse;
	}

	/**
	 * Allows user to insert a new meeting
	 * @param newMeeting: json data retrieved from body to complete request
	 */
	public void insertNewMeeting(InsertNewMeetingRequest newMeeting) {
		getCustomerOrWorkerInfo();
		// newMeeting validate
		if (!InsertNewMeetingRequest.validateInsertNewMeetingRequestJsonFields(newMeeting)) {
			throw new IllegalArgumentException("Invalid json body");}
		Account workerAccount = accountRepository.findByEmailAndUserRole(newMeeting.getEmailWorker(), Role.Worker);
		Account customerAccount = accountRepository.findByEmailAndUserRole(newMeeting.getEmailCustomer(), Role.Customer);

		String dateString = newMeeting.getDate();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDateTime dateTime = LocalDate.parse(dateString, formatter).atStartOfDay();

		Meeting meeting = new Meeting(
				workerAccount.getAccountId(),
				customerAccount.getAccountId(),
				newMeeting.getDescription(),
				dateTime,
				newMeeting.getSlot_time());

		meetingRepository.save(meeting);
	}

	/**
	 * Allows user completing sign up with bio and photo profile
	 * @param bio: the bio that user wants to be set
	 * @param photoProfile: the photo profile that user wants to be set
	 */
	public void completeSignUp(String bio, MultipartFile photoProfile) {
		Account account = getCustomerOrWorkerInfo();
		if (photoProfile == null){
			if (!bio.matches(bioRegex))
				throw new IllegalArgumentException("Invalid bio");
			accountRepository.findByUserRoleAndEmail(account.getUserRole(), account.getEmail())
					.orElseThrow(IllegalArgumentException::new);
			account.setBio(bio);
			account.setPhotoProfile(photoProfileBase);
		} else if (bio == null) {
			account.setBio("");
			try {
				account.setPhotoProfile(photoProfile.getBytes());
			} catch (IOException e) {
				throw new IllegalArgumentException("Invalid photoProfile");
			}
		}else if (bio != null && !photoProfile.isEmpty()) {
			// Bio validate
			if (!bio.matches(bioRegex))
				throw new IllegalArgumentException("Invalid bio");
			accountRepository.findByUserRoleAndEmail(account.getUserRole(), account.getEmail())
					.orElseThrow(IllegalArgumentException::new);
			account.setBio(bio);
			try {
				account.setPhotoProfile(photoProfile.getBytes());
			} catch (IOException e) {
				throw new IllegalArgumentException("Invalid photoProfile");
			}
		} else if (bio == null && photoProfile.isEmpty()){
			account.setBio("");
			account.setPhotoProfile(photoProfileBase);
		}
		accountRepository.save(account);
	}

	/**
	 * Allows user to change password
	 * @param oldPassword: the old password of user
	 * @param newPassword: the new password of user
	 * @return a : jwt authorization to can log with the new password
	 */
	public JWTResponse changePassword(String oldPassword, String newPassword) {
		Account account = getCustomerOrWorkerInfo();
		// Password validate
		if (!newPassword.matches(passwordRegex))
			throw new IllegalArgumentException("Invalid new password");
		else if (!account.getUserPassword().equals(DigestUtils.sha3_256Hex(oldPassword)))
			throw new IllegalArgumentException("Invalid old password");
		else {
			String shaNewPass = DigestUtils.sha3_256Hex(newPassword);
			account.setUserPassword(shaNewPass);
			accountRepository.save(account);
			return new JWTResponse(authenticationService.login(account.getUserRole(), account.getEmail(), shaNewPass));
		}
	}

	public void updatePhotoProfile(MultipartFile photoProfile) {
		Account account = getCustomerOrWorkerInfo();
		if (photoProfile != null) {
			try {
				account.setPhotoProfile(photoProfile.getBytes());
			} catch (IOException e) {
				throw new IllegalArgumentException("Invalid photoProfile");
			}
		}
		accountRepository.save(account);
	}

	/**
	 * Allows user to change his data
	 * @param updateProfileRequest: json data retrieved from body to complete request
	 */
	public void updateProfile(UpdateProfileRequest updateProfileRequest) {
		Account account = getCustomerOrWorkerInfo();
		// Validate json body
		if (!UpdateProfileRequest.validateUpdateProfileRequestJsonFields(updateProfileRequest))
			throw new IllegalArgumentException("Invalid json body");
		if (updateProfileRequest.getFirstName() != null)
			account.setFirstName(updateProfileRequest.getFirstName());
		if (updateProfileRequest.getSecondName() != null)
			account.setSecondName(updateProfileRequest.getSecondName());
		if (updateProfileRequest.getBio() != null)
			account.setBio(updateProfileRequest.getBio());
		accountRepository.save(account);
	}



	public List<GetProfileResponse> getWorkersOfCategory(String categoryString) {
		getCustomerOrWorkerInfo();
		// Validate category param
		if (!categoryString.matches(Regex.categoryRegex) || categoryString == null)
			throw new IllegalArgumentException("Invalid category param");
		Category category = Category.valueOf(categoryString);
		List<Account> workersRetrieved = accountRepository.findByUserCategoryAndUserRole(category, Role.Worker);
		List<GetProfileResponse> getProfileResponse = new ArrayList<>();
		for (Account worker : workersRetrieved)
			getProfileResponse.add(getProfile(worker.getAccountId()));

		return getProfileResponse;
	}

	public List<GetProfileResponse> getWorkersByFirstNameOrSecondNameOrEmailOrBio(String value, String categoryString) {
		getCustomerOrWorkerInfo();
		// Check if value param is null
		if(value == null)
			return getWorkersOfCategory(categoryString);
		// Validate value param
		if(!value.matches(Regex.firstNameRegex) && !value.matches(Regex.secondNameRegex) && !value.matches(Regex.emailRegex) && !value.matches(bioRegex))
			throw new IllegalArgumentException("Invalid value param");
		// Validate category param
		if (!categoryString.matches(Regex.categoryRegex) || categoryString.isEmpty())
			throw new IllegalArgumentException("Invalid category param");
		Category category = Category.valueOf(categoryString);
		List<Account> workersRetrieved = accountRepository.findByFirstNameOrSecondNameOrEmailOrBioAndUserCategoryAndUserRole(value, value, value, value, category, Role.Worker);
		List<GetProfileResponse> getProfileResponse = new ArrayList<>();
		for (Account worker : workersRetrieved)
			getProfileResponse.add(getProfile(worker.getAccountId()));
		return getProfileResponse;
	}

	public List<GetProductResponse> getWorkerProducts(String emailWorker) {
		getCustomerOrWorkerInfo();
		// emailWorker validate
		if (!emailWorker.matches(emailRegex))
			throw new IllegalArgumentException("Invalid emailWorker");
		Account workerAccount = accountRepository.findByEmailAndUserRole(emailWorker, Role.Worker);
		List<Product> productsRetrieved;
		List<GetProductResponse> getProductResponse = new ArrayList<>();
		if (workerAccount != null) {
			productsRetrieved = productRepository.findByUserIdAndDeletedProductFalse(workerAccount.getAccountId());
			for (Product product : productsRetrieved) {
				getProductResponse.add(new GetProductResponse(
						product.getProductId(),
						product.getProductImage(),
						product.getPrice(),
						product.getDescription(),
						product.getTitle()));
			}
		}
		return getProductResponse;
	}

	public List<GetReviewsResponse> getReviews() {
		Account workerAccount = accountRepository.findByEmailAndUserRole(getCustomerOrWorkerInfo().getEmail(),getCustomerOrWorkerInfo().getUserRole());
		List<Review> reviewsRetrieved;
		List<GetReviewsResponse> getReviewsResponse = new ArrayList<>();
		if (workerAccount != null) {
			reviewsRetrieved = reviewRepository.findByUserIdReceiveReview(workerAccount.getAccountId());
			for (Review review : reviewsRetrieved)
				getReviewsResponse.add(new GetReviewsResponse(
						review.getContent(),
						review.getStar(),
						workerAccount.getFirstName(),
						workerAccount.getSecondName()
				));
		}
		return getReviewsResponse;
	}
}
