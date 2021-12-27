package core.wefix.lab.service.authentication;

import com.paypal.base.HttpStatusCodes;
import core.wefix.lab.entity.Account;
import core.wefix.lab.repository.AccountRepository;
import core.wefix.lab.service.jwt.JWTAuthenticationService;
import core.wefix.lab.service.jwt.JWTService;
import core.wefix.lab.utils.object.request.RegisterRequest;
import core.wefix.lab.utils.object.staticvalues.Role;
import javassist.bytecode.DuplicateMemberException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;


@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CustomerAuthenticationService {
	private final JWTService jwtService;
	private final AccountRepository accountRepository;
	private final JWTAuthenticationService authenticationService;

	/**
	 * Allows customer to log in the 'WeFix' system
	 * @param email: the username to identify user that wants log
	 * @param password: the password to identify user that wants log
	 * @return a String: jwt authorization to can log
	 */
	public String login(String email, String password) throws BadCredentialsException {
		if (accountRepository.findByUserRoleAndEmailAndUserPassword(Role.Customer, email, password).isPresent()) {
			// It proceeds with log in
			return accountRepository
					.findByUserRoleAndEmailAndUserPassword(Role.Customer, email, password)
					.map(user -> jwtService.create(Role.Customer, email, password))
					.orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
		} else if (accountRepository.findByUserRoleAndEmailAndResetCode(Role.Customer, email, password).isPresent()) {
			// It proceeds with BadCredentialsException
			Account account = accountRepository.findByUserRoleAndEmailAndResetCode(Role.Customer, email, password)
					.orElseThrow(() -> new BadCredentialsException("Invalid username or password."));
			// Log in with new password
			if (LocalDateTime.now().isBefore(account.getDateReset().minusHours(2))) {
				account.setUserPassword(password);
				accountRepository.save(account);
				return jwtService.create(Role.Customer, email, password);
			} else
				// Log in with new password failed (already spent 24 hours to reset password with that contained in email body)
				throw new BadCredentialsException("Change password expired. Get a new email with password");
		}
		throw new BadCredentialsException("Invalid username o password");
	}

	public void reset(String username) {
		authenticationService.reset(Role.Customer, username);
	}

	/**
	 * Allows user to register into 'WeFix' system
	 * @param registerRequest: json data retrieved from body to complete request
	 * @return a String: jwt authorization to can log
	 */
	public String signUp(RegisterRequest registerRequest) {
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
		return authenticationService.login(role, registerRequest.getEmail(), shaPassword);
	}
}
