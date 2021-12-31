package core.wefix.lab.service;

import core.wefix.lab.entity.Account;
import core.wefix.lab.repository.AccountRepository;
import core.wefix.lab.service.jwt.JWTService;
import core.wefix.lab.utils.object.response.GetProfileResponse;
import core.wefix.lab.utils.object.staticvalues.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class WorkerService {
    private final AccountRepository accountRepository;

    /**
     * Allows retrieving of all worker data from his authentication
     * @return an Account: all information about worker logged
     */
    public Account getWorkerInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            User user = ((User) authentication.getPrincipal());
            if (!user.getAuthorities().contains(new SimpleGrantedAuthority(Role.Worker.name()))) {
                throw new JWTService.TokenVerificationException();
            }
            return accountRepository.findByUserRoleAndEmail(Role.Worker, user.getUsername())
                    .orElseThrow(JWTService.TokenVerificationException::new);
        }
        throw new JWTService.TokenVerificationException();
    }

}
