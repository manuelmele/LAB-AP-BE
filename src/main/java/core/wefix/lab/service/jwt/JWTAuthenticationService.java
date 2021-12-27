package core.wefix.lab.service.jwt;

import core.wefix.lab.configuration.mail.CustomMailSender;
import core.wefix.lab.repository.AccountRepository;
import core.wefix.lab.utils.object.staticvalues.Role;
import core.wefix.lab.entity.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JWTAuthenticationService {
	private final JWTService jwtService;
	private final CustomMailSender customMailSender;
	private final AccountRepository accountRepository;

	public String login(Role role, String email, String password) throws BadCredentialsException {
		if (accountRepository.findByUserRoleAndEmailAndUserPassword(role, email, password).isPresent()) {
			// It proceeds with log in
			return accountRepository
					.findByUserRoleAndEmailAndUserPassword(role, email, password)
					.map(user -> jwtService.create(role, email, password))
					.orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
		} else if (accountRepository.findByUserRoleAndEmailAndResetCode(role, email, password).isPresent()) {
			// It proceeds with BadCredentialsException
			Account account = accountRepository.findByUserRoleAndEmailAndResetCode(role, email, password)
					.orElseThrow(() -> new BadCredentialsException("Invalid username or password."));
			// Log in with new password
			if (LocalDateTime.now().isBefore(account.getDateReset())) {
				account.setUserPassword(password);
				accountRepository.save(account);
				return jwtService.create(role, email, password);
			} else
				// Log in with new password failed (already spent 24 hours to reset password with that contained in email body)
				throw new BadCredentialsException("Change password expired. Get a new email with password");
		}
		throw new BadCredentialsException("Invalid username o password");
	}

	/**
	 * Allows admin/user to reset their password
	 * @param role: identifies 'Admin' or 'User' role
	 * @param email: the email to identify admin/user that wants to be proceeded with reset
	 */
	public void reset(Role role, String email) throws BadCredentialsException {
		accountRepository
				.findByUserRoleAndEmail(role, email)
				.ifPresentOrElse(
						account -> {
							String newPassword = RandomString.make(10);
							switch (role) {
								case Customer: {
									if (!customMailSender.sendResetUser(email, Map.of("password", newPassword))) {
										throw new RuntimeException("Something went wrong with email");
									}else{
										account.setDateReset(LocalDateTime.now().plusHours(26));
										account.setResetCode(DigestUtils.sha3_256Hex(newPassword));
										accountRepository.save(account);
									}
									break;
								}
								case Worker: {
									if (!customMailSender.sendResetAdmin(email, Map.of("password", newPassword))) {
										throw new RuntimeException("Something went wrong with email");
									}else{
										account.setDateReset(LocalDateTime.now().plusHours(26));
										account.setResetCode(DigestUtils.sha3_256Hex(newPassword));
										accountRepository.save(account);
									}
									break;
								}
								default:
									throw new RuntimeException("Something went wrong with reset");
							}
						},
						() -> {
							throw new BadCredentialsException("Invalid email");
						});
	}

	public Account authenticateByToken(String token) {
		try {
			Map<String, Object> data = jwtService.verify(token);
			Role role = Role.valueOf(String.valueOf(data.get("role")));
			String email = String.valueOf(data.get("email"));
			String password = String.valueOf(data.get("password"));
			return accountRepository.findByUserRoleAndEmailAndUserPassword(role, email, password)
					.orElseThrow(() -> new UsernameNotFoundException("Authentication fail"));
		} catch (Exception e) {
			throw new BadCredentialsException("Invalid token");
		}
	}
}
