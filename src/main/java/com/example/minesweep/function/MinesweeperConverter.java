package com.example.minesweep.function;

import com.example.minesweep.domain.GameColumn;
import com.example.minesweep.domain.GameEntity;
import com.example.minesweep.domain.GameField;
import com.example.minesweep.rest.request.FlagType;
import com.example.minesweep.rest.response.Column;
import com.example.minesweep.rest.response.Field;
import com.example.minesweep.rest.response.FieldCondition;
import com.example.minesweep.rest.response.MinesweeperGame;
import com.example.minesweep.util.GameStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MinesweeperConverter {

    private final GameStatusConverter gameStatusConverter;

    public MinesweeperGame toMinesweeper(GameEntity gameEntity) {
        return MinesweeperGame
                .builder()
                .id(gameEntity.getId())
                .status(gameStatusConverter.toGameStatus(gameEntity.getGameStatusEntity()))
                .columns(gameEntity
                        .getGameColumns()
                        .stream()
                        .map(c -> this.toColumn(c, gameEntity.getGameStatusEntity().getId().equals(GameStatus.LOST.getId()))).collect(Collectors.toList()))
                .gameDurationInSeconds(Duration.between(gameEntity.getStartTime(),
                        gameEntity.getEndTime() != null ? gameEntity.getEndTime() : Instant.now()).getSeconds())
                .build();

    }

    private Column toColumn(GameColumn gameColumn, boolean shouldReveal) {
        return Column.builder()
                .fields(gameColumn.getGameFields().stream().map(f -> this.toField(f, shouldReveal)).collect(Collectors.toList()))
                .position(gameColumn.getId())
                .build();
    }

    private Field toField(GameField gameField, boolean shouldReveal) {
        Integer value;
        FieldCondition condition;
        if (shouldReveal) {
            condition = gameField.getMined() ? FieldCondition.MINE : gameField.getHidden() ? FieldCondition.HIDDEN : FieldCondition.REVEALED;
        } else {
            condition = gameField.getFlagType() != null ?
                    gameField.getFlagType().equals(FlagType.FLAG) ? FieldCondition.FLAG : FieldCondition.MARKED :
                    gameField.getHidden() ? FieldCondition.HIDDEN :
                            FieldCondition.REVEALED;

        }

        value = condition.equals(FieldCondition.REVEALED) ? gameField.getValue() : null;
        return Field.builder()
                .position(gameField.getId())
                .fieldCondition(condition)
                .value(value)
                .build();
    }
}
