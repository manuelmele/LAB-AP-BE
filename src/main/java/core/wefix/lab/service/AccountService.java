package core.wefix.lab.service;

import core.wefix.lab.entity.Account;
import core.wefix.lab.repository.AccountRepository;
import core.wefix.lab.service.jwt.JWTAuthenticationService;
import core.wefix.lab.service.jwt.JWTService;
import core.wefix.lab.utils.object.Regex;
import core.wefix.lab.utils.object.request.UpdateProfileRequest;
import core.wefix.lab.utils.object.response.GetProfileResponse;
import core.wefix.lab.utils.object.response.JWTResponse;
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
import java.util.ArrayList;
import java.util.List;

import static core.wefix.lab.utils.object.Regex.bioRegex;
import static core.wefix.lab.utils.object.Regex.passwordRegex;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AccountService {
	private final AccountRepository accountRepository;
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
	public GetProfileResponse getProfile() {
		return new GetProfileResponse(getCustomerOrWorkerInfo().getFirstName(),
				getCustomerOrWorkerInfo().getSecondName(),
				getCustomerOrWorkerInfo().getEmail(),
				getCustomerOrWorkerInfo().getBio(),
				getCustomerOrWorkerInfo().getPhotoProfile(),
				getCustomerOrWorkerInfo().getPIva(),
				getCustomerOrWorkerInfo().getIdentityCardNumber(),
				getCustomerOrWorkerInfo().getUserRole(),
				getCustomerOrWorkerInfo().getUserCategory());
	}

	/**
	 * Allows user completing sign up with bio and photo profile
	 * @param bio: the bio that user wants to be set
	 * @param photoProfile: the photo profile that user wants to be set
	 */
	public void completeSignUp(String bio, MultipartFile photoProfile) {
		Account account = getCustomerOrWorkerInfo();
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

	/**
	 * Allows user to change his data
	 * @param updateProfileRequest: json data retrieved from body to complete request
	 */
	public void updateProfile(UpdateProfileRequest updateProfileRequest) {
		Account account = getCustomerOrWorkerInfo();
		// Validate json body
		if (!UpdateProfileRequest.validateUpdateProfileRequestJsonFields(updateProfileRequest))
			throw new IllegalArgumentException("Invalid json body");
		account.setFirstName(updateProfileRequest.getFirstName());
		account.setSecondName(updateProfileRequest.getSecondName());
		account.setPhotoProfile(updateProfileRequest.getPhotoProfile());
		account.setBio(updateProfileRequest.getBio());
		accountRepository.save(account);
	}


	public List<GetProfileResponse> getWorkersOfCategory(String categoryString) {
		Account account = getCustomerOrWorkerInfo();
		// Validate category param
		if (!categoryString.matches(Regex.categoryRegex))
			throw new IllegalArgumentException("Invalid category param");
		Category category = Category.valueOf(categoryString);
		List<Account> workersRetrieved = accountRepository.findByUserCategoryAndUserRole(category, Role.Worker);
		List<GetProfileResponse> getProfileResponse = new ArrayList<>();
		for (Account worker : workersRetrieved) {
			getProfileResponse.add(new GetProfileResponse(
					worker.getFirstName(),
					worker.getSecondName(),
					worker.getEmail(),
					worker.getBio(),
					worker.getPhotoProfile(),
					worker.getPIva(),
					worker.getIdentityCardNumber(),
					worker.getUserRole(),
					worker.getUserCategory()));
		}
		return getProfileResponse;
	}

	public List<GetProfileResponse> getWorkersByFirstNameOrSecondNameOrEmail(String value) {
		Account account = getCustomerOrWorkerInfo();
		// Validate value param
		if(!value.matches(Regex.firstNameRegex) && !value.matches(Regex.secondNameRegex) && !value.matches(Regex.emailRegex))
			throw new IllegalArgumentException("Invalid value param");
		List<Account> workersRetrieved = accountRepository.findByFirstNameOrSecondNameOrEmail(value, value, value);
		List<GetProfileResponse> getProfileResponse = new ArrayList<>();
		for (Account worker : workersRetrieved) {
			getProfileResponse.add(new GetProfileResponse(
					worker.getFirstName(),
					worker.getSecondName(),
					worker.getEmail(),
					worker.getBio(),
					worker.getPhotoProfile(),
					worker.getPIva(),
					worker.getIdentityCardNumber(),
					worker.getUserRole(),
					worker.getUserCategory()));
		}
		return getProfileResponse;
	}

}
