package com.example.minesweep.rest.controller;

import com.example.minesweep.rest.request.CreateAccountRequest;
import com.example.minesweep.rest.request.CreateSessionRequest;
import com.example.minesweep.rest.request.ModifyAccountRequest;
import com.example.minesweep.rest.request.RefreshSessionRequest;
import com.example.minesweep.rest.response.Account;
import com.example.minesweep.rest.response.CreateSessionResponse;
import com.example.minesweep.service.AccountService;
import com.example.minesweep.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController("AccountController")
@RequestMapping(Constants.VERSION_ONE)
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @RequestMapping(value = {"/account"}, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public CreateSessionResponse createAccount(@Validated @RequestBody CreateAccountRequest createAccountRequest) {
        return accountService.createAccount(createAccountRequest);
    }

    @RequestMapping(value = {"/account/session"}, method = RequestMethod.POST)
    public CreateSessionResponse createSession(@Validated @RequestBody CreateSessionRequest createSessionRequest) {
        return accountService.createSession(createSessionRequest);
    }

    @RequestMapping(value = {"/account/session/refresh"}, method = RequestMethod.POST)
    public CreateSessionResponse refreshSession(@Validated @RequestBody RefreshSessionRequest refreshSessionRequest) {
        return accountService.refreshSession(refreshSessionRequest);
    }

    @RequestMapping(value = {"/me/account"}, method = RequestMethod.PUT)
    public void changePassword(@Validated @RequestBody ModifyAccountRequest modifyAccountRequest) {
        accountService.modifyAccount(modifyAccountRequest);
    }

    @RequestMapping(value = {"/me/account"}, method = RequestMethod.GET)
    public Account getMyAccount() {
        return accountService.getMyAccount();
    }

}
