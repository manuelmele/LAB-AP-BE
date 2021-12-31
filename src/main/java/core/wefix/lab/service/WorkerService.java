package core.wefix.lab.service;

import core.wefix.lab.entity.Account;
import core.wefix.lab.repository.AccountRepository;
import core.wefix.lab.service.jwt.JWTService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class WorkerService {
    private final PublicService publicService;
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

    /**
     * Allows to retrieve all profile worker data
     * @return a GetWorkerResponse: all information to send as response for a certain worker
     */
    public GetWorkerResponse getProfile() {
        return new GetWorkerResponse(getWorkerInfo().getFirstName(),
                getWorkerInfo().getSecondName(),
                getWorkerInfo().getEmail(),
                getWorkerInfo().getBio(),
                getWorkerInfo().getPhotoProfile(),
                getWorkerInfo().getPIva(),
                getWorkerInfo().getIdentityCardNumber());
    }

    /**
     * Allows worker completing sign up with bio and photo profile
     * @param bio: the bio that worker wants to be set
     * @param photoProfile: the photo profile that worker wants to be set
     */
    public void completeSignUp(String bio, MultipartFile photoProfile) {
        Account account = getWorkerInfo();
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
}
