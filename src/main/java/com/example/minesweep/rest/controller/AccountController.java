package com.example.minesweep.rest.controller;

import com.example.minesweep.rest.request.CreateAccountRequest;
import com.example.minesweep.rest.request.CreateSessionRequest;
import com.example.minesweep.rest.request.ModifyAccountRequest;
import com.example.minesweep.rest.request.RefreshSessionRequest;
import com.example.minesweep.rest.response.Account;
import com.example.minesweep.rest.response.CreateSessionResponse;
import com.example.minesweep.service.AccountService;
import com.example.minesweep.util.Constants;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController("AccountController")
@RequestMapping(Constants.VERSION_ONE)
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @RequestMapping(value = {"/account"}, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Creates an account")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Account created"),
            @ApiResponse(code = 409, message = "Account with that email already exists")})
    public CreateSessionResponse createAccount(@Validated @RequestBody CreateAccountRequest createAccountRequest) {
        return accountService.createAccount(createAccountRequest);
    }

    @RequestMapping(value = {"/account/session"}, method = RequestMethod.POST)
    @ApiOperation("Creates a user token to use in authenticated requests using a password")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Session created"),
            @ApiResponse(code = 403, message = "Invalid credentials")})
    public CreateSessionResponse createSession(@Validated @RequestBody CreateSessionRequest createSessionRequest) {
        return accountService.createSession(createSessionRequest);
    }

    @RequestMapping(value = {"/account/session/refresh"}, method = RequestMethod.POST)
    @ApiOperation("Creates a user token to use in authenticated requests using a refresh token")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Session created"),
            @ApiResponse(code = 403, message = "Invalid credentials")})
    public CreateSessionResponse refreshSession(@Validated @RequestBody RefreshSessionRequest refreshSessionRequest) {
        return accountService.refreshSession(refreshSessionRequest);
    }

    @RequestMapping(value = {"/me/account"}, method = RequestMethod.PUT)
    @ApiOperation("Changes user password or name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Password changed"),
            @ApiResponse(code = 403, message = "Invalid credentials")})
    public void changePassword(@Validated @RequestBody ModifyAccountRequest modifyAccountRequest) {
        accountService.modifyAccount(modifyAccountRequest);
    }

    @RequestMapping(value = {"/me/account"}, method = RequestMethod.GET)
    @ApiOperation("Retrieves user account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Password changed")})
    public Account getMyAccount() {
        return accountService.getMyAccount();
    }

}
