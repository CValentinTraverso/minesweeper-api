package com.example.minesweep;

import com.example.minesweep.domain.AccountEntity;
import com.example.minesweep.repository.AccountRepository;
import com.example.minesweep.rest.request.CreateAccountRequest;
import com.example.minesweep.rest.response.CreateAccountResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Optional;


public abstract class BaseIntegrationTest {
    protected final String TEST_EMAIL = "testEmail@testDomain.com";
    protected final String TEST_PASSWORD = "testPassword";
    protected final String TEST_NAME = "testName";
    protected TestRestTemplate testRestTemplate;
    protected String testUrl;

    @LocalServerPort
    private int port;
    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected AccountRepository accountRepository;

    @Before
    public void setUp() throws Exception {
        testRestTemplate = new TestRestTemplate();
        testUrl = String.format("http://localhost:%d/", port);
    }

    @After
    public void tearDown() throws Exception {
        Optional<AccountEntity> firstByEmail = accountRepository.findFirstByEmail(TEST_EMAIL);
        firstByEmail.ifPresent(accountEntity -> accountRepository.delete(accountEntity));
    }

    protected HttpHeaders getAuthHeaders() throws JsonProcessingException {
        ResponseEntity<String> responseEntity = this.createAccount();
        CreateAccountResponse createAccountResponse = objectMapper.readValue(responseEntity.getBody(), CreateAccountResponse.class);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + createAccountResponse.getAuthToken());
        return headers;
    }

    protected ResponseEntity<String> createAccount() {
        return this.createAccount(TEST_EMAIL, TEST_PASSWORD);
    }

    protected ResponseEntity<String> createAccount(String email, String password) {
        CreateAccountRequest createAccountRequest = CreateAccountRequest.builder()
                .email(email).password(password).name(TEST_NAME).build();
        return testRestTemplate.exchange(
                testUrl + "/v1/account",
                HttpMethod.POST,
                new HttpEntity<>(createAccountRequest, null),
                String.class
        );

    }
}
