package core.wefix.lab.service;

import core.wefix.lab.entity.Account;
import core.wefix.lab.repository.AccountRepository;
import core.wefix.lab.service.jwt.JWTAuthenticationService;
import core.wefix.lab.service.jwt.JWTService;
import core.wefix.lab.utils.object.request.RegisterRequest;
import core.wefix.lab.utils.object.response.JWTResponse;
import core.wefix.lab.utils.object.staticvalues.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class PublicService {
	private final JWTService jwtService;
	private final AccountRepository accountRepository;
	private final JWTAuthenticationService authenticationService;

	/**
	 * Allows user to register into 'WeFix' system
	 * @param registerRequest: json data retrieved from body to complete request
	 * @return a String: jwt authorization to can log
	 */
	public JWTResponse signUp(RegisterRequest registerRequest) {
		// Validate json body
		if (!RegisterRequest.validateRegisterRequestJsonFields(registerRequest))
			throw new IllegalArgumentException("Invalid json body");
		// Existing account validate
		accountRepository.findByEmail(registerRequest.getEmail()).ifPresent((a) -> {
			throw new DuplicateKeyException("Email already used");});
		// Create new account
		Role role = Role.Customer;
		String shaPassword = DigestUtils.sha3_256Hex(registerRequest.getUserPassword());
		accountRepository.save(
				new Account(registerRequest.getFirstName(),
						registerRequest.getSecondName(),
						registerRequest.getEmail(),
						shaPassword,
						role));
		return new JWTResponse(authenticationService.login(role, registerRequest.getEmail(), shaPassword));
	}

	/**
	 * Allows customer or worker to log in the 'WeFix' system
	 * @param email: the username to identify user that wants log
	 * @param password: the password to identify user that wants log
	 * @return a String: jwt authorization to can log
	 */
	public JWTResponse login(String email, String password) {
		Optional<Account> retrieveAccountThroughtPasssword = accountRepository.findByEmailAndUserPassword(email, password);
		Optional<Account> retrieveAccountThroughtResetCode = accountRepository.findByEmailAndResetCode(email, password);
		if (retrieveAccountThroughtPasssword.isPresent()) {
			// It proceeds with log in
			return accountRepository
					.findByUserRoleAndEmailAndUserPassword(retrieveAccountThroughtPasssword.get().getUserRole(), email, password)
					.map(user -> new JWTResponse(jwtService.create(retrieveAccountThroughtPasssword.get().getUserRole(), email, password)))
					.orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
		} else if (retrieveAccountThroughtResetCode.isPresent()) {
			// It proceeds with BadCredentialsException
			Account account = accountRepository.findByUserRoleAndEmailAndResetCode(retrieveAccountThroughtResetCode.get().getUserRole(), email, password)
					.orElseThrow(() -> new BadCredentialsException("Invalid username or password."));
			// Log in with new password
			if (LocalDateTime.now().isBefore(account.getDateReset().minusHours(2))) {
				account.setUserPassword(password);
				accountRepository.save(account);
				return new JWTResponse(jwtService.create(retrieveAccountThroughtResetCode.get().getUserRole(), 	email, password));
			} else
				// Log in with new password failed (already spent 24 hours to reset password with that contained in email body)
				throw new BadCredentialsException("Change password expired. Get a new email with password");
		}
		// It proceeds with BadCredentialsException
		throw new BadCredentialsException("Invalid username o password");
	}

	public void resetPassword(String email) {
		Optional<Account> retrieveAccountThroughtPasssword = accountRepository.findByEmail(email);
		if (retrieveAccountThroughtPasssword.isPresent()) {
			// It proceeds with reset
			authenticationService.reset(retrieveAccountThroughtPasssword.get().getUserRole(), email);
		} else throw new BadCredentialsException("Invalid username");
	}

}
