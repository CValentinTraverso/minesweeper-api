package com.example.minesweep.service;

import com.example.minesweep.domain.GameColumn;
import com.example.minesweep.domain.GameEntity;
import com.example.minesweep.domain.GameField;
import com.example.minesweep.exception.BadRequestException;
import com.example.minesweep.util.GameStatus;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
public class GameService {

    public void revealField(int c, int r, GameEntity game) {
        int columnSize = game.getGameColumns().size() - 1;
        int rowSize = game.getGameColumns().get(0).getGameFields().size() - 1;

        if (c > columnSize || r > rowSize) {
            throw new BadRequestException("Out of bounds");
        }

        GameField pushedField = game.getGameColumns().get(c).getGameFields().get(r);
        if (pushedField.getMined()) {
            game.setGameStatusEntity(GameStatus.LOST);
            game.setEndTime(Instant.now());
            pushedField.setHidden(false);
        } else if (pushedField.getFlagType() != null) {
            throw new BadRequestException("A flagged field cannot be revealed");
        } else if (!pushedField.getHidden()) {
            throw new BadRequestException("A revealed field cannot be revealed");
        } else {
            reveal(c, r, game);
        }
    }

    private void reveal(int c, int r, GameEntity game) {
        int columnSize = game.getGameColumns().size() - 1;
        int rowSize = game.getGameColumns().get(0).getGameFields().size() - 1;

        GameField currentField = game.getGameColumns().get(c).getGameFields().get(r);

        if (currentField.getHidden()) {
            currentField.setHidden(false);
            game.setRevealedFields(game.getRevealedFields() + 1);
            if (currentField.getMined()) {
                log.error("Trying to reveal a mine field");
            }
            if (currentField.getValue() == 0) {
                for (int i = c - 1 >= 0 ? c - 1 : c; i <= (c + 1 <= columnSize ? c + 1 : c); i++) {
                    for (int j = r - 1 >= 0 ? r - 1 : r; j <= (r + 1 <= rowSize ? r + 1 : r); j++) {
                        reveal(i, j, game);
                    }
                }
            }
        }
    }

    public GameEntity.GameEntityBuilder createEmptyGame(int columns, int rows) {
        List<GameColumn> columnsList = Lists.newArrayList();
        for (long i = 0; i < columns; i++) {
            List<GameField> fields = Lists.newArrayList();
            for (long j = 0; j < rows; j++) {
                GameField.GameFieldBuilder fieldBuilder = GameField.builder().value(0).hidden(true);
                fields.add(fieldBuilder.id(j).flagType(null).mined(false).hidden(true).value(0).build());
            }
            columnsList.add(GameColumn.builder().id(i).gameFields(fields).build());
        }
        return GameEntity.builder().gameColumns(columnsList).revealedFields(0).gameStatusEntity(GameStatus.ACTIVE);
    }

    public GameEntity addMines(GameEntity game, int mines) {
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
