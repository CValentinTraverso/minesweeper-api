package com.example.minesweep.rest.controller;

import com.example.minesweep.BaseIntegrationTest;
import com.example.minesweep.MinesweepApplication;
import com.example.minesweep.domain.AccountEntity;
import com.example.minesweep.rest.request.CreateSessionRequest;
import com.example.minesweep.rest.request.ModifyAccountRequest;
import com.example.minesweep.rest.request.RefreshSessionRequest;
import com.example.minesweep.rest.response.Account;
import com.example.minesweep.rest.response.CreateAccountResponse;
import com.example.minesweep.rest.response.CreateSessionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MinesweepApplication.class)
public class AccountControllerTest extends BaseIntegrationTest {

    @Test
    public void testCreateAccount() throws JsonProcessingException {
        ResponseEntity<String> response = createAccount();

        assertThat(response.getStatusCode().is2xxSuccessful(), is(true));
        Optional<AccountEntity> firstByEmail = accountRepository.findFirstByEmail(TEST_EMAIL);
        assertThat(firstByEmail.isPresent(), is(true));
        AccountEntity accountEntity = firstByEmail.get();
        assertThat(accountEntity.getId(), is(notNullValue()));
        assertThat(accountEntity.getPassword(), is(not(equalTo(TEST_PASSWORD))));
        assertThat(accountEntity.getName(), is(equalTo(TEST_NAME)));
        CreateAccountResponse createAccountResponse = objectMapper.readValue(response.getBody(), CreateAccountResponse.class);
        assertThat(createAccountResponse.getAuthToken(), is(notNullValue()));
        assertThat(createAccountResponse.getRefreshToken(), is(notNullValue()));
        assertThat(createAccountResponse.getUserId(), is(notNullValue()));
    }

    @Test
    public void testCreateAccountWithInvalidEmail() {
        ResponseEntity<String> responseEntity = createAccount("notAnEmail", TEST_PASSWORD);

        assertThat(responseEntity.getStatusCode().is4xxClientError(), is(true));
    }

    @Test
    public void testCreateAccountWithRepeatedEmail() {
        createAccount();
        ResponseEntity<String> responseEntity = createAccount();

        assertThat(responseEntity.getStatusCode().is4xxClientError(), is(true));
    }

    @Test
    public void testCreateSession() throws JsonProcessingException {
        createAccount();
        CreateSessionRequest createSessionRequest = CreateSessionRequest
                .builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();

        ResponseEntity<String> responseEntity = testRestTemplate.exchange(
                testUrl + "/v1/account/session",
                HttpMethod.POST,
                new HttpEntity<>(createSessionRequest, null),
                String.class
        );

        assertThat(responseEntity.getStatusCode().is2xxSuccessful(), is(true));
        CreateSessionResponse createSessionResponse = objectMapper.readValue(responseEntity.getBody(), CreateSessionResponse.class);
        assertThat(createSessionResponse.getAuthToken(), is(notNullValue()));
        assertThat(createSessionResponse.getRefreshToken(), is(notNullValue()));
        assertThat(createSessionResponse.getUserId(), is(notNullValue()));
    }

    @Test
    public void testCreateSessionWithNonExistingEmail() {
        CreateSessionRequest createSessionRequest = CreateSessionRequest
                .builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();

        ResponseEntity<String> responseEntity = testRestTemplate.exchange(
                testUrl + "/v1/account/session",
                HttpMethod.POST,
                new HttpEntity<>(createSessionRequest, null),
                String.class
        );

        assertThat(responseEntity.getStatusCode().is4xxClientError(), is(true));
    }

    @Test
    public void testRefreshSessionWithToken() throws JsonProcessingException {
        ResponseEntity<String> response = createAccount();
        CreateAccountResponse createAccountResponse = objectMapper.readValue(response.getBody(), CreateAccountResponse.class);

        RefreshSessionRequest refreshSessionRequest = RefreshSessionRequest
                .builder()
                .email(TEST_EMAIL)
                .refreshToken(createAccountResponse.getRefreshToken())
                .build();

        ResponseEntity<String> responseEntity = testRestTemplate.exchange(
                testUrl + "/v1/account/session/refresh",
                HttpMethod.POST,
                new HttpEntity<>(refreshSessionRequest, null),
                String.class
        );

        assertThat(responseEntity.getStatusCode().is2xxSuccessful(), is(true));
        CreateSessionResponse createSessionResponse = objectMapper.readValue(responseEntity.getBody(), CreateSessionResponse.class);
        assertThat(createSessionResponse.getAuthToken(), is(notNullValue()));
        assertThat(createSessionResponse.getRefreshToken(), is(nullValue()));
        assertThat(createSessionResponse.getUserId(), is(notNullValue()));
    }

    @Test
    public void testRefreshSessionWithInvalidToken() throws JsonProcessingException {
        RefreshSessionRequest refreshSessionRequest = RefreshSessionRequest
                .builder()
                .email(TEST_EMAIL)
                .refreshToken("Invalid token")
                .build();

        ResponseEntity<String> responseEntity = testRestTemplate.exchange(
                testUrl + "/v1/account/session/refresh",
                HttpMethod.POST,
                new HttpEntity<>(refreshSessionRequest, null),
                String.class
        );

        assertThat(responseEntity.getStatusCode().is4xxClientError(), is(true));
    }

    @Test
    public void testChangePassword() throws JsonProcessingException {
        ResponseEntity<String> response = createAccount();
        CreateAccountResponse createAccountResponse = objectMapper.readValue(response.getBody(), CreateAccountResponse.class);

        ModifyAccountRequest modifyAccountRequest = ModifyAccountRequest
                .builder()
                .password(TEST_PASSWORD)
                .newPassword("new password")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + createAccountResponse.getAuthToken());
        ResponseEntity<String> responseEntity = testRestTemplate.exchange(
                testUrl + "/v1/me/account",
                HttpMethod.PUT,
                new HttpEntity<>(modifyAccountRequest, headers),
                String.class
        );

        assertThat(responseEntity.getStatusCode().is2xxSuccessful(), is(true));

        //verify that can create session with new password
        CreateSessionRequest createSessionRequest = CreateSessionRequest
                .builder()
                .email(TEST_EMAIL)
                .password("new password")
                .build();

        responseEntity = testRestTemplate.exchange(
                testUrl + "/v1/account/session",
                HttpMethod.POST,
                new HttpEntity<>(createSessionRequest, null),
                String.class
        );

        assertThat(responseEntity.getStatusCode().is2xxSuccessful(), is(true));
    }

    @Test
    public void testChangePasswordDoesntAllowNewSessionsWithOldPassword() throws JsonProcessingException {
        ResponseEntity<String> response = createAccount();
        CreateAccountResponse createAccountResponse = objectMapper.readValue(response.getBody(), CreateAccountResponse.class);

        ModifyAccountRequest modifyAccountRequest = ModifyAccountRequest
                .builder()
                .password(TEST_PASSWORD)
                .newPassword("new password")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + createAccountResponse.getAuthToken());
        ResponseEntity<String> responseEntity = testRestTemplate.exchange(
                testUrl + "/v1/me/account",
                HttpMethod.PUT,
                new HttpEntity<>(modifyAccountRequest, headers),
                String.class
        );

        assertThat(responseEntity.getStatusCode().is2xxSuccessful(), is(true));

        //verify that cannot create session with new password
        CreateSessionRequest createSessionRequest = CreateSessionRequest
                .builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();

        responseEntity = testRestTemplate.exchange(
                testUrl + "/v1/account/session",
                HttpMethod.POST,
                new HttpEntity<>(createSessionRequest, null),
                String.class
        );

        assertThat(responseEntity.getStatusCode().is4xxClientError(), is(true));
    }

    @Test
    public void testChangePasswordDoesntAllowWrongPassword() throws JsonProcessingException {
        ResponseEntity<String> response = createAccount();
        CreateAccountResponse createAccountResponse = objectMapper.readValue(response.getBody(), CreateAccountResponse.class);

        ModifyAccountRequest modifyAccountRequest = ModifyAccountRequest
                .builder()
                .password("wrong password")
                .newPassword("new password")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + createAccountResponse.getAuthToken());
        ResponseEntity<String> responseEntity = testRestTemplate.exchange(
                testUrl + "/v1/me/account",
                HttpMethod.PUT,
                new HttpEntity<>(modifyAccountRequest, headers),
                String.class
        );

        assertThat(responseEntity.getStatusCode().is4xxClientError(), is(true));

        //verify that cannot create session with old password
        CreateSessionRequest createSessionRequest = CreateSessionRequest
                .builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();

        responseEntity = testRestTemplate.exchange(
                testUrl + "/v1/account/session",
                HttpMethod.POST,
                new HttpEntity<>(createSessionRequest, null),
                String.class
        );

        assertThat(responseEntity.getStatusCode().is2xxSuccessful(), is(true));
    }

    @Test
    public void testGetMyAccount() throws JsonProcessingException {
        ResponseEntity<String> response = createAccount();
        CreateAccountResponse createAccountResponse = objectMapper.readValue(response.getBody(), CreateAccountResponse.class);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + createAccountResponse.getAuthToken());

        ResponseEntity<String> responseEntity = testRestTemplate.exchange(
                testUrl + "/v1/me/account",
                HttpMethod.GET,
                new HttpEntity<>(null, headers),
                String.class
        );

        assertThat(responseEntity.getStatusCode().is2xxSuccessful(), is(true));
        Account account = objectMapper.readValue(responseEntity.getBody(), Account.class);
        assertThat(account.getEmail(), is(equalTo(TEST_EMAIL)));
        assertThat(account.getName(), is(equalTo(TEST_NAME)));
    }

    @Test
    public void testGetMyAccountDoesntWorkWithoutAuthentication() throws JsonProcessingException {
        ResponseEntity<String> responseEntity = testRestTemplate.exchange(
                testUrl + "/v1/me/account",
                HttpMethod.GET,
                new HttpEntity<>(null, null),
                String.class
        );

        assertThat(responseEntity.getStatusCode().is4xxClientError(), is(true));
    }
}