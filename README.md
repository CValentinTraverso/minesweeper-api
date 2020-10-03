# INTRODUCTION

Minesweep is a playable RESTful api for the classic minesweeper game

## Requirements

To be able to run this project you need to have maven, docker and java8, make is optional

## Compilation

To compile and create an executable, run make package

## Usage

This api contains swagger documentation, once running, go to [swagger online tool](https://petstore.swagger.io/?_ga=2.137711664.1210155368.1601735400-1698795516.1601735400#/) 
and put http://localhost:9000/v2/api-docs in the explore bar

### Curl samples

#### Create an acccount

```
curl --request POST \
  --url http://localhost:9000/v1/account \
  --header 'content-type: application/json' \
  --data '{
	"email": "sampleEmail@emailProvider.com",
	"password": 123456,
	"name": "Account display name"
}'
```
#### Create session

```
curl --request POST \
  --url http://localhost:9000/v1/account/session \
  --header 'content-type: application/json' \
  --data '{
	"email": "sampleEmail@emailProvider.com",
	"password": 123456
}'
```
#### Refresh session

```
curl --request POST \
  --url http://localhost:9000/v1/account/session/refresh \
  --header 'content-type: application/json' \
  --data '{
	"email": "sampleEmail@emailProvider.com",
	"refreshToken": "c41ee9100065ecfc4d6225c8af216dc6e980529279f67f4209491c851d583a07"
}'
```
#### Change password

```
curl --request PUT \
  --url http://localhost:9000/v1/me/account \
  --header 'content-type: application/json' \
  --header 'authorization: Bearer {token}' \
  --data '{
	"password": "123456",
	"newPassword": "654321"
}'
```
#### Get my account

```
curl --request GET \
  --url http://localhost:9000/v1/me/account \
  --header 'authorization: Bearer {token}' \
```
#### Create new game

```
curl --request POST \
  --url http://localhost:9000/v1/minesweeper \
  --header 'authorization: Bearer {token}' \
  --header 'content-type: application/json' \
  --data '{
	"rows": 10,
	"columns": 10,
	"mines": 10
}'
```
#### Get game by id

```
curl --request GET \
  --url http://localhost:9000/v1/minesweeper/15 \
  --header 'authorization: Bearer {token}' \
```
#### Reveal one field

```
curl --request PUT \
  --url http://localhost:9000/v1/minesweeper/15/reveal \
  --header 'authorization: Bearer {token}' \
  --header 'content-type: application/json' \
  --data '{
	"column": 0,
	"field": 0
}'
```
#### Create a flag

```
curl --request PUT \
  --url http://localhost:9000/v1/minesweeper/15/flag \
  --header 'authorization: Bearer {token}' \
  --header 'content-type: application/json' \
  --data '{
	"column": 0,
	"field": 0,
	"flagType": 1
}'
```
#### Remove a flag

```
curl --request PUT \
  --url http://localhost:9000/v1/minesweeper/15/unflag \
  --header 'authorization: Bearer {token}' \
  --header 'content-type: application/json' \
  --data '{
	"column": 0,
	"field": 0
}'
```