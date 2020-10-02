package com.example.minesweep.service;

import com.example.minesweep.domain.AccountEntity;
import com.example.minesweep.domain.GameColumn;
import com.example.minesweep.domain.GameEntity;
import com.example.minesweep.domain.GameEntity.GameEntityBuilder;
import com.example.minesweep.domain.GameField;
import com.example.minesweep.domain.GameField.GameFieldBuilder;
import com.example.minesweep.exception.ForbiddenException;
import com.example.minesweep.exception.NotFoundException;
import com.example.minesweep.function.MinesweeperConverter;
import com.example.minesweep.repository.GameRepository;
import com.example.minesweep.rest.request.CreateGameRequest;
import com.example.minesweep.rest.response.MinesweeperGame;
import com.example.minesweep.util.DeleteMeUtils;
import com.example.minesweep.util.GameStatus;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MinesweeperService {

    private final GameRepository gameRepository;
    private final AuthService authService;
    private final MinesweeperConverter minesweeperConverter;

    @Transactional
    public MinesweeperGame createGame(CreateGameRequest createGameRequest) {
        Long owner = authService.getCurrentUser().getUserId();
        GameEntityBuilder emptyGameBuilder = this.createEmptyGame(createGameRequest.getColumns(), createGameRequest.getRows());
        emptyGameBuilder.accountEntity(AccountEntity.builder().id(owner).build());
        GameEntity gameEntity = this.addMines(emptyGameBuilder.build(), createGameRequest.getMines());
        gameEntity.setStartTime(Instant.now());
        GameEntity savedEntity = gameRepository.save(gameEntity);

        return minesweeperConverter.toMinesweeper(savedEntity);
    }

    public MinesweeperGame getGame(Long id) {
        Long owner = authService.getCurrentUser().getUserId();
        Optional<GameEntity> byId = gameRepository.findById(id);
        if (!byId.isPresent()) {
            throw new NotFoundException();
        }
        GameEntity gameEntity = byId.get();
        if (!gameEntity.getAccountEntity().getId().equals(owner)) {
            throw new ForbiddenException();
        }
        DeleteMeUtils.print(gameEntity, true);
        return minesweeperConverter.toMinesweeper(gameEntity);
    }

    private GameEntityBuilder createEmptyGame(int columns, int rows) {
        List<GameColumn> columnsList = Lists.newArrayList();
        for (long i = 0; i < columns; i++) {
            List<GameField> fields = Lists.newArrayList();
            for (long j = 0; j < rows; j++) {
                GameFieldBuilder fieldBuilder = GameField.builder().value(0).hidden(true);
                fields.add(fieldBuilder.id(j).flagged(false).mined(false).hidden(true).value(0).build());
            }
            columnsList.add(GameColumn.builder().id(i).gameFields(fields).build());
        }
        return GameEntity.builder().gameColumns(columnsList).revealedFields(0).gameStatusEntity(GameStatus.ACTIVE);
    }

    private GameEntity addMines(GameEntity game, int mines) {
        int columns = game.getGameColumns().size();
        int rows = game.getGameColumns().get(0).getGameFields().size();
        int placedMines = 0;
        int columnsSize = columns - 1;
        int fieldSize = rows - 1;
        while (mines != placedMines) {
            int c = RandomUtils.nextInt(0, columns);
            int r = RandomUtils.nextInt(0, rows);
            GameField mineField = game.getGameColumns().get(c).getGameFields().get(r);
            if (!mineField.getMined()) {
                for (int i = c - 1 >= 0 ? c - 1 : c; i <= (c + 1 <= columnsSize ? c + 1 : c); i++) {
                    for (int j = r - 1 >= 0 ? r - 1 : r; j <= (r + 1 <= fieldSize ? r + 1 : r); j++) {
                        GameField field = game.getGameColumns().get(i).getGameFields().get(j);
                        if (i == c && r == j) {
                            field.setMined(true);
                        } else {
                            field.setValue(field.getValue() + 1);
                        }
                    }
                }
                placedMines++;
            }
        }
        return game;
    }
}
