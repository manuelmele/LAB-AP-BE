package core.wefix.lab.service;

import core.wefix.lab.entity.Account;
import core.wefix.lab.repository.AccountRepository;
import core.wefix.lab.service.jwt.JWTAuthenticationService;
import core.wefix.lab.service.jwt.JWTService;
import core.wefix.lab.utils.object.response.GetCustomerResponse;
import core.wefix.lab.utils.object.response.GetWorkerResponse;
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
    private final PublicService publicService;

    /**
     * Allows to retrieve all profile worker data
     * @return a GetWorkerResponse: all information to send as response for a certain worker
     */
    public GetWorkerResponse getProfile() {
        return new GetWorkerResponse(publicService.getWorkerOrCustomerInfo(Role.Worker).getFirstName(),
                publicService.getWorkerOrCustomerInfo(Role.Worker).getSecondName(),
                publicService.getWorkerOrCustomerInfo(Role.Worker).getEmail(),
                publicService.getWorkerOrCustomerInfo(Role.Worker).getBio(),
                publicService.getWorkerOrCustomerInfo(Role.Worker).getPhotoProfile(),
                publicService.getWorkerOrCustomerInfo(Role.Worker).getPIva(),
                publicService.getWorkerOrCustomerInfo(Role.Worker).getIdentityCardNumber());
    }

}
