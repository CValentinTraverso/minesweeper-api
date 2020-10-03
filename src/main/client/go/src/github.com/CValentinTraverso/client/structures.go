package client

type errorResponse struct {
	Status    int    `json:"status"`
	Message string `json:"message"`
}

type CreateAccountRequest struct {
	Email    string    `json:"email"`
	Password string `json:"password"`
	Name string `json:"name"`
}

type CreateAccountResponse struct {
	UserId    int    `json:"userId"`
	Token string `json:"authToken"`
	RefreshToken string `json:"refreshToken"`
}

type CreateSessionRequest struct {
	Email    string `json:"email"`
	Password string    `json:"password"`
}

type RefreshSessionRequest struct {
	Email    string `json:"email"`
	RefreshToken string    `json:"refreshToken"`
}

type CreateGameRequest struct {
	Rows    int `json:"rows"`
	Columns int `json:"columns"`
	Mines   int `json:"mines"`
}

type Game struct {
	ID                    int `json:"id"`
	GameDurationInSeconds int `json:"gameDurationInSeconds"`
	Columns               []struct {
		Position int `json:"position"`
		Fields   []struct {
			Position       int    `json:"position"`
			FieldCondition string `json:"fieldCondition"`
			Value          int    `json:"value"`
		} `json:"fields"`
	} `json:"columns"`
	Status struct {
		ID   int    `json:"id"`
		Name string `json:"name"`
	} `json:"status"`
}

type RevealFieldRequest struct {
	Column int `json:"column"`
	Field  int `json:"field"`
}

type FlagFieldRequest struct {
	Column   int `json:"column"`
	Field    int `json:"field"`
	FlagType int `json:"flagType"`
}

type UnFlagFieldRequest struct {
	Column   int `json:"column"`
	Field    int `json:"field"`
}

type ChangePasswordRequest struct {
	Password    string `json:"password"`
	NewPassword string `json:"newPassword"`
}

type GetMyAccountResponse struct {
	Email string `json:"email"`
	Name  string `json:"name"`
	ID    int    `json:"id"`
}