package com.example.minesweep.function;

import com.example.minesweep.domain.GameStatusEntity;
import com.example.minesweep.rest.response.GameStatus;
import org.springframework.stereotype.Service;

@Service
public class GameStatusConverter {

    public GameStatus toGameStatus(GameStatusEntity gameStatusEntity) {
        return GameStatus.builder()
                .name(gameStatusEntity.getStatus())
                .id(gameStatusEntity.getId())
                .build();
    }
}
