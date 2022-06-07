package core.wefix.lab.controller;

import core.wefix.lab.service.PublicService;
import core.wefix.lab.utils.object.request.RegisterRequest;
import core.wefix.lab.utils.object.request.UpdateProfileRequest;
import core.wefix.lab.utils.object.response.GetProfileResponse;
import core.wefix.lab.utils.object.response.JWTResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountApiControllerTest {

    /*
    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private PublicService publicService;

    private final String userMail = "userPublicController@mail.com";
    private final String defaultPassword = "TestPassword123";
    private final String userBio = "Hi there, I'm an electrician";

    @Test
    @Disabled
    void getProfile() {
        ResponseEntity<GetProfileResponse> response =
                restTemplate.exchange(
                        "/wefix/account/profile", HttpMethod.GET, null,
                        GetProfileResponse.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @SneakyThrows
    @Test
    @Disabled
    void completeSignUp() {
        FileInputStream fis = new FileInputStream("src/test/resources/images/myProfilePicture.png");
        MockMultipartFile userPhoto = new MockMultipartFile("file", fis);

        ResponseEntity<String> response =
                restTemplate.exchange(
                        "/wefix/account/complete/signup?bio={bio}&photoProfile={userPhoto}",  HttpMethod.PUT, null,
                        String.class, userBio, userPhoto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Disabled
    void changePassword() {
        JWTResponse token = new JWTResponse("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwYXNzd29yZCI6IjM2Mzk5OWY3OTE4YmI4NDI2MGY0ODFjY2VhZWQzOTZmYjA0NmU4ZGMyNTc1MGM1YzNhZTBlODA4OGFlMTdiMjIiLCJyb2xlIjoiQ3VzdG9tZXIiLCJpYXQiOjE2NDA4NjU4MzUsImVtYWlsIjoicHJvdmFlbWFpbEBnbWFpbDEuY29tIn0.uCKtDQcTI6O0hTXhGzX0ZkUz9gAuKEtOQRC97MZHZhw");
        given(publicService.login(any(), any())).willReturn(token);

        String oldPassword = "bla4321password";
        String newPassword = "bla1234password";

        ResponseEntity<JWTResponse> response =
                restTemplate.exchange(
                        "/wefix/account/change-password?oldPassword={oldPassword}&newPassword={newPassword}",  HttpMethod.POST, null,
                        JWTResponse.class, oldPassword, newPassword);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(token.getJwt(), response.getBody().getJwt());
    }

    @Test
    @Disabled
    void updateProfile() {
        UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest();

        ResponseEntity<String> response =
                restTemplate.exchange(
                        "/wefix/account/update-profile", HttpMethod.PUT, new HttpEntity<>(updateProfileRequest),
                        String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
     */
}


