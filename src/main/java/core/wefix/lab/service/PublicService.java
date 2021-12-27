package core.wefix.lab.service;

import core.wefix.lab.service.authentication.WorkerAuthenticationService;
import core.wefix.lab.service.authentication.CustomerAuthenticationService;
import core.wefix.lab.utils.object.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class PublicService {
	private final CustomerAuthenticationService customerAuthenticationService;
	private final WorkerAuthenticationService workerAuthenticationService;

	public String signUp(RegisterRequest data) {
		return customerAuthenticationService.signUp(data);
	}

	public String loginCustomer(String username, String password) {
		return customerAuthenticationService.login(username, DigestUtils.sha3_256Hex(password));
	}

	public String loginWorker(String username, String password) {
		return workerAuthenticationService.login(username, DigestUtils.sha3_256Hex(password));
	}

	public void resetUser(String username) {
		customerAuthenticationService.reset(username);
	}

	public void resetAdmin(String username) {
		workerAuthenticationService.reset(username);
	}
}
