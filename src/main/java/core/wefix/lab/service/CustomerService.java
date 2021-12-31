package core.wefix.lab.service;

import core.wefix.lab.entity.Account;
import core.wefix.lab.repository.AccountRepository;
import core.wefix.lab.service.jwt.JWTAuthenticationService;
import core.wefix.lab.service.jwt.JWTService;
import core.wefix.lab.utils.object.response.GetCustomerResponse;
import core.wefix.lab.utils.object.response.JWTResponse;
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

import static core.wefix.lab.utils.object.Regex.passwordRegex;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CustomerService {
	private final AccountRepository accountRepository;
	private final JWTAuthenticationService authenticationService;

	/**
	 * Allows retrieving of all customer data from his authentication
	 * @return an Account: all information about customer logged
	 */
	public Account getCustomerInfo(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			User user = ((User) authentication.getPrincipal());
			if (!user.getAuthorities().contains(new SimpleGrantedAuthority(Role.Customer.name()))) {
				throw new JWTService.TokenVerificationException();
			}
			return accountRepository.findByUserRoleAndEmail(Role.Customer, user.getUsername())
					.orElseThrow(JWTService.TokenVerificationException::new);
		}
		throw new JWTService.TokenVerificationException();
	}

	/**
	 * Allows to retrieve all profile customer data
	 * @return a GetCustomerResponse: all information to send as response for a certain customer
	 */
	public GetCustomerResponse getProfile() {
		return new GetCustomerResponse(getCustomerInfo().getFirstName(),
				getCustomerInfo().getSecondName(),
				getCustomerInfo().getEmail(),
				getCustomerInfo().getBio(),
				getCustomerInfo().getPhotoProfile());
	}

	/**
	 * Allows customer completing sign up with bio and photo profile
	 * @param bio: the bio that customer wants to be set
	 * @param photoProfile: the photo profile that customer wants to be set
	 */
	public void completeSignUp(String bio, MultipartFile photoProfile) {
		Account account = getCustomerInfo();
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
	 * Allows customer to change password
	 * @return a : jwt authorization to can log with the new password
	 */
	public JWTResponse changePassword(String oldPassword, String newPassword) {
		Account account = getCustomerInfo();
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

}
