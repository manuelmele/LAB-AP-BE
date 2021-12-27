package core.wefix.lab.service.authentication;

import core.wefix.lab.repository.AccountRepository;
import core.wefix.lab.service.jwt.JWTService;
import core.wefix.lab.utils.object.staticvalues.Role;
import core.wefix.lab.entity.Account;
import core.wefix.lab.service.jwt.JWTAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WorkerAuthenticationService {
	private final JWTService jwtService;
	private final AccountRepository accountRepository;
	private final JWTAuthenticationService authenticationService;

	/**
	 * Allows worker to log in the 'WeFix' system
	 * @param username: the username to identify admin that wants log
	 * @param password: the password to identify admin that wants log
	 * @return a String: jwt authorization to can log
	 */
	public String login(String username, String password) throws BadCredentialsException {
		if (accountRepository.findByUserRoleAndEmailAndUserPassword(Role.Worker, username, password).isPresent()) {
			// It proceeds with log in
			return accountRepository
					.findByUserRoleAndEmailAndUserPassword(Role.Worker, username, password)
					.map(user -> jwtService.create(Role.Worker, username, password))
					.orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
		} else if (accountRepository.findByUserRoleAndEmailAndResetCode(Role.Worker, username, password).isPresent()) {
			// It proceeds with BadCredentialsException
			Account account = accountRepository.findByUserRoleAndEmailAndResetCode(Role.Worker, username, password)
					.orElseThrow(() -> new BadCredentialsException("Invalid username or password."));
			// Log in with new password
			if (LocalDateTime.now().isBefore(account.getDateReset().minusHours(2))) {
				account.setUserPassword(password);
				accountRepository.save(account);
				return jwtService.create(Role.Worker, username, password);
			} else
				// Log in with new password failed (already spent 24 hours to reset password with that contained in email body)
				throw new BadCredentialsException("Change password expired. Get a new email with password");
		}
		throw new BadCredentialsException("Invalid username o password");
	}

	public void reset(String username) {
		authenticationService.reset(Role.Worker, username);
	}

}
