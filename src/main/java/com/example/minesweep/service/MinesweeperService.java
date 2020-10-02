package com.example.minesweep.service;

import com.example.minesweep.domain.AccountEntity;
import com.example.minesweep.domain.GameColumn;
import com.example.minesweep.domain.GameEntity;
import com.example.minesweep.domain.GameEntity.GameEntityBuilder;
import com.example.minesweep.domain.GameField;
import com.example.minesweep.domain.GameField.GameFieldBuilder;
import com.example.minesweep.exception.BadRequestException;
import com.example.minesweep.exception.ForbiddenException;
import com.example.minesweep.exception.NotFoundException;
import com.example.minesweep.function.MinesweeperConverter;
import com.example.minesweep.repository.GameRepository;
import com.example.minesweep.rest.request.CreateGameRequest;
import com.example.minesweep.rest.request.RevealPositionRequest;
import com.example.minesweep.rest.response.MinesweeperGame;
import com.example.minesweep.util.DeleteMeUtils;
import com.example.minesweep.util.GameStatus;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinesweeperService {

    private final GameRepository gameRepository;
    private final AuthService authService;
    private final MinesweeperConverter minesweeperConverter;
    private final GameService gameService;

    @Transactional
    public MinesweeperGame createGame(CreateGameRequest createGameRequest) {
        Long owner = authService.getCurrentUser().getUserId();
        GameEntityBuilder emptyGameBuilder = gameService.createEmptyGame(createGameRequest.getColumns(), createGameRequest.getRows());
        emptyGameBuilder.accountEntity(AccountEntity.builder().id(owner).build());
        GameEntity gameEntity = gameService.addMines(emptyGameBuilder.build(), createGameRequest.getMines());
        gameEntity.setStartTime(Instant.now());
        gameEntity.setMines(createGameRequest.getMines());
        GameEntity savedEntity = gameRepository.save(gameEntity);

        return minesweeperConverter.toMinesweeper(savedEntity);
    }

    public MinesweeperGame getGame(Long id) {
        GameEntity gameEntity = retrieveGame(id);
        return minesweeperConverter.toMinesweeper(gameEntity);
    }

    public MinesweeperGame revealPosition(Long id, RevealPositionRequest revealPositionRequest) {
        GameEntity gameEntity = retrieveGame(id);
        if (!gameEntity.getGameStatusEntity().equals(GameStatus.ACTIVE)) {
            throw new BadRequestException("Cannot play non active games");
        }
        gameService.revealField(revealPositionRequest.getColumn(), revealPositionRequest.getField(), gameEntity);

        int winCondition = (gameEntity.getGameColumns().size() * gameEntity.getGameColumns().get(0).getGameFields().size()) - gameEntity.getMines();
        if (gameEntity.getRevealedFields() == winCondition) {
            gameEntity.setGameStatusEntity(GameStatus.WON);
        }
        gameRepository.save(gameEntity);
        return minesweeperConverter.toMinesweeper(gameEntity);
    }

    private GameEntity retrieveGame(Long id) {
        Long owner = authService.getCurrentUser().getUserId();
        Optional<GameEntity> byId = gameRepository.findById(id);
        if (!byId.isPresent()) {
            throw new NotFoundException();
        }
        GameEntity gameEntity = byId.get();
        if (!gameEntity.getAccountEntity().getId().equals(owner)) {
            throw new ForbiddenException();
        }
        return gameEntity;
    }

}
