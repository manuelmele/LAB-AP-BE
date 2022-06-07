package core.wefix.lab.controller;

import core.wefix.lab.service.PublicService;
import core.wefix.lab.utils.object.request.RegisterRequest;
import core.wefix.lab.utils.object.response.JWTResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PublicApiControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private PublicService publicService;

    private final String userMail = "userPublicController@mail.com";
    private final String defaultPassword = "TestPassword123";

    @Test
    void signup() {
        JWTResponse token = new JWTResponse("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwYXNzd29yZCI6IjM2Mzk5OWY3OTE4YmI4NDI2MGY0ODFjY2VhZWQzOTZmYjA0NmU4ZGMyNTc1MGM1YzNhZTBlODA4OGFlMTdiMjIiLCJyb2xlIjoiQ3VzdG9tZXIiLCJpYXQiOjE2NDA4NjU4MzUsImVtYWlsIjoicHJvdmFlbWFpbEBnbWFpbDEuY29tIn0.uCKtDQcTI6O0hTXhGzX0ZkUz9gAuKEtOQRC97MZHZhw");
        given(publicService.signUp(any())).willReturn(token);
        RegisterRequest registerRequest = new RegisterRequest();

        ResponseEntity<JWTResponse> response =
                restTemplate.exchange(
                        "/wefix/public/signup", HttpMethod.POST, new HttpEntity<>(registerRequest),
                        JWTResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(token.getJwt(), response.getBody().getJwt());
    }

    @Test
    void login() {
        JWTResponse token = new JWTResponse("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwYXNzd29yZCI6IjM2Mzk5OWY3OTE4YmI4NDI2MGY0ODFjY2VhZWQzOTZmYjA0NmU4ZGMyNTc1MGM1YzNhZTBlODA4OGFlMTdiMjIiLCJyb2xlIjoiQ3VzdG9tZXIiLCJpYXQiOjE2NDA4NjU4MzUsImVtYWlsIjoicHJvdmFlbWFpbEBnbWFpbDEuY29tIn0.uCKtDQcTI6O0hTXhGzX0ZkUz9gAuKEtOQRC97MZHZhw");
        given(publicService.login(any(), any())).willReturn(token);
        RegisterRequest registerRequest = new RegisterRequest();

        ResponseEntity<JWTResponse> response =
                restTemplate.exchange(
                        "/wefix/public/login?email={email}&password={password}",  HttpMethod.POST, null,
                        JWTResponse.class, userMail, defaultPassword);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(token.getJwt(), response.getBody().getJwt());
    }

    @Test
    void reset() {
        ResponseEntity<String> response =
                restTemplate.exchange(
                        "/wefix/public/reset?email={email}",  HttpMethod.POST, null,
                        String.class, userMail);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
