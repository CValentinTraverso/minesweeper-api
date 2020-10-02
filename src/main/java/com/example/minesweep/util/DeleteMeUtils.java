package com.example.minesweep.util;

import com.example.minesweep.domain.GameEntity;

public class DeleteMeUtils {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    public static void print(GameEntity game, boolean printMines) {
        game.getGameColumns().forEach(c -> {
            c.getGameFields().forEach(r -> {
                if (r.getMined() && printMines) {
                    System.out.print(ANSI_RED + "Mine" + ANSI_RESET);
                } else if (r.getFlagged()) {
                    System.out.print("Flag");
                } else if (!r.getHidden()) {
                    if (r.getMined()) {
                        System.out.print(ANSI_RED + "Mine" + ANSI_RESET);
                    } else {
                        System.out.print(" " + r.getValue() + "  ");
                    }
                } else {
                    System.out.print("Hide");
                }
                System.out.print(" | ");
            });
            System.out.println();
        });
    }

}
