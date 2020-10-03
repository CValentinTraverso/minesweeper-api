package client

import (
	"bytes"
	"context"
	"encoding/json"
	"fmt"
	"net/http"
)

func (c *Client) CreateGame(ctx context.Context, request CreateGameRequest) (*Game, error) {
	requestByte, _ := json.Marshal(request)
	requestReader := bytes.NewReader(requestByte)
	req, err := http.NewRequest("POST", fmt.Sprintf("%s/v1/minesweeper", c.BaseURL), requestReader)
	if err != nil {
		return nil, err
	}
	req = req.WithContext(ctx)
	res := Game{}
	if err := c.sendAuthRequest(req, &res); err != nil {
		return nil, err
	}
	return &res, nil
}
func (c *Client) GetGame(ctx context.Context, gameId int) (*Game, error) {
	req, err := http.NewRequest("GET", fmt.Sprintf("%s/v1/minesweeper/%d", c.BaseURL, gameId), nil)
	if err != nil {
		return nil, err
	}
	req = req.WithContext(ctx)
	res := Game{}
	if err := c.sendAuthRequest(req, &res); err != nil {
		return nil, err
	}
	return &res, nil
}

func (c *Client) RevealField(ctx context.Context, request RevealFieldRequest, gameId int) (*Game, error) {
	requestByte, _ := json.Marshal(request)
	requestReader := bytes.NewReader(requestByte)
	req, err := http.NewRequest("PUT", fmt.Sprintf("%s/v1/minesweeper/%d/reveal", c.BaseURL, gameId), requestReader)
	if err != nil {
		return nil, err
	}
	req = req.WithContext(ctx)
	res := Game{}
	if err := c.sendAuthRequest(req, &res); err != nil {
		return nil, err
	}
	return &res, nil
}

func (c *Client) FlagField(ctx context.Context, request FlagFieldRequest, gameId int) (*Game, error) {
	requestByte, _ := json.Marshal(request)
	requestReader := bytes.NewReader(requestByte)
	req, err := http.NewRequest("PUT", fmt.Sprintf("%s/v1/minesweeper/%d/flag", c.BaseURL, gameId), requestReader)
	if err != nil {
		return nil, err
	}
	req = req.WithContext(ctx)
	res := Game{}
	if err := c.sendAuthRequest(req, &res); err != nil {
		return nil, err
	}
	return &res, nil
}

func (c *Client) UnFlagField(ctx context.Context, request UnFlagFieldRequest, gameId int) (*Game, error) {
	requestByte, _ := json.Marshal(request)
	requestReader := bytes.NewReader(requestByte)
	req, err := http.NewRequest("PUT", fmt.Sprintf("%s/v1/minesweeper/%d/unflag", c.BaseURL, gameId), requestReader)
	if err != nil {
		return nil, err
	}
	req = req.WithContext(ctx)
	res := Game{}
	if err := c.sendAuthRequest(req, &res); err != nil {
		return nil, err
	}
	return &res, nil
}