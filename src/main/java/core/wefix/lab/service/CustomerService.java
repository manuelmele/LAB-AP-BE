package core.wefix.lab.service;

import core.wefix.lab.configuration.error.ErrorResponse;
import core.wefix.lab.entity.Account;
import core.wefix.lab.repository.AccountRepository;
import core.wefix.lab.service.jwt.JWTAuthenticationService;
import core.wefix.lab.service.jwt.JWTService;
import core.wefix.lab.utils.object.response.GetCustomerResponse;
import core.wefix.lab.utils.object.staticvalues.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CustomerService {
	private final AccountRepository accountRepository;
	private final JWTAuthenticationService authenticationService;
	private final PublicService publicService;

	/**
	 * Allows to retrieve all profile customer data
	 * @return a GetCustomerResponse: all information to send as response for a certain customer
	 */
	public GetCustomerResponse getProfile() {
		return new GetCustomerResponse(publicService.getWorkerOrCustomerInfo(Role.Customer).getFirstName(),
				publicService.getWorkerOrCustomerInfo(Role.Customer).getSecondName(),
				publicService.getWorkerOrCustomerInfo(Role.Customer).getEmail(),
				publicService.getWorkerOrCustomerInfo(Role.Customer).getBio(),
				publicService.getWorkerOrCustomerInfo(Role.Customer).getPhotoProfile());
	}

	/**
	 * Allows user to change password
	 * @return a String: jwt authorization to can log with the new password
	 */
	public String changePassword(String oldPassword, String newPassword) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			User user = ((User) authentication.getPrincipal());
			if (!user.getAuthorities().contains(new SimpleGrantedAuthority(Role.Customer.name()))) {
				throw new JWTService.TokenVerificationException();
			}
			Account account = accountRepository.findByUserRoleAndEmail(Role.Customer, user.getUsername())
					.orElseThrow(JWTService.TokenVerificationException::new);
			// Password validate
			String pattern = "^(?=.*?[0-9])(?=.*?[a-z])(?=.*?[A-Z])(.{8,30})$";
			if (!newPassword.matches(pattern) || newPassword.length() > 30) {
				throwIllegal("Invalid new password");
			}
			if (!account.getUserPassword().equals(DigestUtils.sha3_256Hex(oldPassword))){
				throwIllegal("Invalid old password");
			} else {
				String shaNewPass = DigestUtils.sha3_256Hex(newPassword);
				account.setUserPassword(shaNewPass);
				accountRepository.save(account);
				return authenticationService.login(account.getUserRole(), account.getEmail(), shaNewPass);
			}
		}
		throw new JWTService.TokenVerificationException();
	}

	private void throwIllegal(String msg) throws IllegalArgumentException {
		throw throwIllegalReturn(msg);
	}

	private IllegalArgumentException throwIllegalReturn(String msg) {
		return new IllegalArgumentException(msg);
	}

}
