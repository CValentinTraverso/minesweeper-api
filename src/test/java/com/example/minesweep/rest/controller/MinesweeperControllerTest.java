package com.example.minesweep.rest.controller;

import com.example.minesweep.BaseIntegrationTest;
import com.example.minesweep.MinesweepApplication;
import com.example.minesweep.domain.GameEntity;
import com.example.minesweep.repository.GameRepository;
import com.example.minesweep.rest.request.CreateGameRequest;
import com.example.minesweep.rest.response.MinesweeperGame;
import com.example.minesweep.util.GameStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MinesweepApplication.class)
public class MinesweeperControllerTest extends BaseIntegrationTest {


    @Autowired
    GameRepository gameRepository;

    @Test
    public void testCreateGame() throws JsonProcessingException {
        CreateGameRequest createGameRequest = CreateGameRequest
                .builder()
                .rows(10)
                .columns(10)
                .mines(10)
                .build();

        ResponseEntity<String> responseEntity = testRestTemplate.exchange(
                testUrl + "/v1/minesweeper",
                HttpMethod.POST,
                new HttpEntity<>(createGameRequest, getAuthHeaders()),
                String.class
        );

        assertThat(responseEntity.getStatusCode().is2xxSuccessful(), is(true));
        MinesweeperGame minesweeperGame = objectMapper.readValue(responseEntity.getBody(), MinesweeperGame.class);
        assertThat(minesweeperGame.getStatus(), is(equalTo(GameStatus.ACTIVE.getId())));
        assertThat(minesweeperGame.getColumns().size(), is(equalTo(10)));
        assertThat(minesweeperGame.getColumns().get(0).getFields().size(), is(equalTo(10)));
        AtomicInteger mines = new AtomicInteger();
        GameEntity entity = gameRepository.getOne(minesweeperGame.getId());
        entity.getGameColumns().forEach(c-> c.getGameFields().forEach(f -> {
            if (f.getMined()) {
                mines.getAndIncrement();
            }
        }));
        assertThat(mines.get(), is(equalTo(10)));
    }

    @Test
    public void testCreateImpossibleGame() throws JsonProcessingException {
        CreateGameRequest createGameRequest = CreateGameRequest
                .builder()
                .rows(1)
                .columns(1)
                .mines(10)
                .build();

        ResponseEntity<String> responseEntity = testRestTemplate.exchange(
                testUrl + "/v1/minesweeper",
                HttpMethod.POST,
                new HttpEntity<>(createGameRequest, getAuthHeaders()),
                String.class
        );

        assertThat(responseEntity.getStatusCode().is4xxClientError(), is(true));
    }
}