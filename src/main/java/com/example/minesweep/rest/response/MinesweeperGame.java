package com.example.minesweep.rest.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MinesweeperGame {

    private Long id;
    private Long gameDurationInSeconds;
    private List<Column> columns;
    private GameStatus status;
}
