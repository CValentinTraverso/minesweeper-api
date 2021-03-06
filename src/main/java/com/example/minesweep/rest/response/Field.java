package com.example.minesweep.rest.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Field {

    private Long position;
    private FieldCondition fieldCondition;
    private Integer value;
}
