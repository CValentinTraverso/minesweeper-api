package client

import (
	"bytes"
	"context"
	"encoding/json"
	"fmt"
	"net/http"
)

func (c *Client) CreateAccount(ctx context.Context, request CreateAccountRequest) (*CreateAccountResponse, error) {
	requestByte, _ := json.Marshal(request)
	requestReader := bytes.NewReader(requestByte)
	req, err := http.NewRequest("POST", fmt.Sprintf("%s/v1/account", c.BaseURL), requestReader)
	if err != nil {
		return nil, err
	}
	req = req.WithContext(ctx)
	res := CreateAccountResponse{}
	if err := c.sendRequest(req, &res); err != nil {
		return nil, err
	}
	c.Token = res.Token
	return &res, nil
}

func (c *Client) CreateSession(ctx context.Context, request CreateSessionRequest) (*CreateAccountResponse, error) {
	requestByte, _ := json.Marshal(request)
	requestReader := bytes.NewReader(requestByte)
	req, err := http.NewRequest("POST", fmt.Sprintf("%s/v1/account/session", c.BaseURL), requestReader)
	if err != nil {
		return nil, err
	}
	req = req.WithContext(ctx)
	res := CreateAccountResponse{}
	if err := c.sendRequest(req, &res); err != nil {
		return nil, err
	}
	c.Token = res.Token
	return &res, nil
}

func (c *Client) RefreshSession(ctx context.Context, request RefreshSessionRequest) (*CreateAccountResponse, error) {
	requestByte, _ := json.Marshal(request)
	requestReader := bytes.NewReader(requestByte)
	req, err := http.NewRequest("POST", fmt.Sprintf("%s/v1/account/session/refresh", c.BaseURL), requestReader)
	if err != nil {
		return nil, err
	}
	req = req.WithContext(ctx)
	res := CreateAccountResponse{}
	if err := c.sendRequest(req, &res); err != nil {
		return nil, err
	}
	c.Token = res.Token
	return &res, nil
}

func (c *Client) GetMyAccount(ctx context.Context) (*GetMyAccountResponse, error) {
	req, err := http.NewRequest("GET", fmt.Sprintf("%s/v1/me/account", c.BaseURL), nil)
	if err != nil {
		return nil, err
	}
	req = req.WithContext(ctx)
	res := GetMyAccountResponse{}
	if err := c.sendAuthRequest(req, &res); err != nil {
		return nil, err
	}
	return &res, nil
}

func (c *Client) ChangePassword(ctx context.Context, request ChangePasswordRequest) (error) {
	requestByte, _ := json.Marshal(request)
	requestReader := bytes.NewReader(requestByte)
	req, err := http.NewRequest("PUT", fmt.Sprintf("%s/v1/me/account", c.BaseURL), requestReader)
	if err != nil {
		return err
	}
	req = req.WithContext(ctx)
	res := CreateAccountResponse{}
	if err := c.sendAuthRequest(req, &res); err != nil {
		return err
	}
	c.Token = res.Token
	return nil
}
