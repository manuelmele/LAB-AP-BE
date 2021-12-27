package core.wefix.lab.service;

import core.wefix.lab.entity.Account;
import core.wefix.lab.repository.AccountRepository;
import core.wefix.lab.service.jwt.JWTAuthenticationService;
import core.wefix.lab.service.jwt.JWTService;
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


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CustomerService {
	private final AccountRepository accountRepository;
	private final JWTAuthenticationService authenticationService;

	/**
	 * Allows retrieving of all user data from his authentication
	 * @return an Account: all information about user logged
	 */
	public Account getUserInfo(){
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
