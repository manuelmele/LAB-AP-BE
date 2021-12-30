package core.wefix.lab.service;

import core.wefix.lab.entity.Account;
import core.wefix.lab.repository.AccountRepository;
import core.wefix.lab.utils.object.request.RegisterRequest;
import core.wefix.lab.utils.object.staticvalues.Role;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.transaction.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@Transactional
@SpringJUnitConfig
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = NONE)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ComponentScan({"core.wefix.lab.service", "core.wefix.lab.configuration.mail"})
public class PublicServiceTest {
    @Autowired
    public PublicService publicService;
    @Autowired
    private AccountRepository accountRepository;

    private final String customerMail = "customer.wefix@mail.com";
    private final String workerMail = "worker.wefix@mail.com";
    private final String defaultPassword = "TestPassword123";

    @BeforeEach
    public void beforeEach() {
        accountRepository.save(new Account("CustomerName",
                "CustomerSurname",
                customerMail,
                DigestUtils.sha3_256Hex(defaultPassword),
                Role.Customer));
        accountRepository.save(new Account("WorkerName",
                "WorkerSurname",
                workerMail,
                DigestUtils.sha3_256Hex(defaultPassword),
                Role.Worker));
    }

    @Test
    void signup() {
        String newCustomerMail = "newcustomer.wefix@mail.com";
        RegisterRequest registerRequest = new RegisterRequest("CustomerName",
                "CustomerSurname",
                newCustomerMail,
                defaultPassword,
                defaultPassword);
        publicService.signUp(registerRequest);
        assertEquals(3, accountRepository.findAll().size());
        HashSet<String> accountEmails = accountRepository.findAll().stream().map(Account::getEmail).collect(Collectors.toCollection(HashSet::new));

        assertEquals(Set.of(customerMail, workerMail, newCustomerMail), accountEmails);
    }
}
