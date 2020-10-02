package com.example.minesweep.util;

import com.example.minesweep.domain.GameStatusEntity;

public class GameStatus {

    public static final GameStatusEntity ACTIVE = GameStatusEntity.builder().id(1L).status("ACTIVE").build();
    public static final GameStatusEntity WON = GameStatusEntity.builder().id(2L).status("WON").build();
    public static final GameStatusEntity LOST = GameStatusEntity.builder().id(3L).status("LOST").build();
}
