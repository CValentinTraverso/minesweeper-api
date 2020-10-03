package com.example.minesweep.rest.controller;

import com.example.minesweep.exception.BadRequestException;
import com.example.minesweep.rest.request.CreateGameRequest;
import com.example.minesweep.rest.request.FlagPositionRequest;
import com.example.minesweep.rest.request.RevealPositionRequest;
import com.example.minesweep.rest.request.UnflagPositionRequest;
import com.example.minesweep.rest.response.MinesweeperGame;
import com.example.minesweep.service.MinesweeperService;
import com.example.minesweep.util.Constants;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController("MinesweeperController")
@RequestMapping(Constants.VERSION_ONE)
@RequiredArgsConstructor
public class MinesweeperController {

    private final MinesweeperService minesweeperService;

    @RequestMapping(value = {"/minesweeper"}, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Creates a new game")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Game created"),
            @ApiResponse(code = 400, message = "Invalid input"),
            @ApiResponse(code = 401, message = "Unauthorized")})
    public MinesweeperGame createGame(@Validated @RequestBody CreateGameRequest createGameRequest) {
        if (createGameRequest.getColumns() * createGameRequest.getRows() <= createGameRequest.getMines()) {
            throw new BadRequestException();
        }
        return minesweeperService.createGame(createGameRequest);
    }

    @RequestMapping(value = {"/minesweeper/{id}"}, method = RequestMethod.GET)
    @ApiOperation("Retrieves a game")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Game retrieved"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden")})
    public MinesweeperGame getGame(@PathVariable(name = "id") Long id) {
        return minesweeperService.getGame(id);
    }

    @RequestMapping(value = {"/minesweeper/{id}/reveal"}, method = RequestMethod.PUT)
    @ApiOperation("Reveals one field")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Field revealed"),
            @ApiResponse(code = 400, message = "Invalid input"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden")})
    public MinesweeperGame revealPosition(@PathVariable(name = "id") Long id,
                                          @Validated @RequestBody RevealPositionRequest revealPositionRequest) {

        return minesweeperService.revealPosition(id, revealPositionRequest);
    }

    @RequestMapping(value = {"/minesweeper/{id}/flag"}, method = RequestMethod.PUT)
    @ApiOperation("Flags one field")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Flag created"),
            @ApiResponse(code = 400, message = "Invalid input"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden")})
    public MinesweeperGame flagPosition(@PathVariable(name = "id") Long id,
                                          @Validated @RequestBody FlagPositionRequest flagPositionRequest) {

        return minesweeperService.flagPosition(id, flagPositionRequest);
    }

    @RequestMapping(value = {"/minesweeper/{id}/unflag"}, method = RequestMethod.PUT)
    @ApiOperation("Removes flag from one field")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Flag removed"),
            @ApiResponse(code = 400, message = "Invalid input"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden")})
    public MinesweeperGame unflagPosition(@PathVariable(name = "id") Long id,
                                          @Validated @RequestBody UnflagPositionRequest unflagPositionRequest) {

        return minesweeperService.unflagPosition(id, unflagPositionRequest);
    }
}
