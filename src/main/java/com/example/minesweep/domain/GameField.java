package com.example.minesweep.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GameField {

    private Long id;
    private Integer value;
    private Boolean hidden;
    private Boolean flagged;
    private Boolean mined;

}
