package com.example.minesweep.rest.controller;

import com.example.minesweep.BaseIntegrationTest;
import com.example.minesweep.MinesweepApplication;
import com.example.minesweep.domain.GameEntity;
import com.example.minesweep.repository.GameRepository;
import com.example.minesweep.rest.request.CreateGameRequest;
import com.example.minesweep.rest.request.RevealPositionRequest;
import com.example.minesweep.rest.response.GameStatus;
import com.example.minesweep.rest.response.MinesweeperGame;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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

        ResponseEntity<String> responseEntity = createGame(createGameRequest, getAuthHeaders());

        assertThat(responseEntity.getStatusCode().is2xxSuccessful(), is(true));
        MinesweeperGame minesweeperGame = objectMapper.readValue(responseEntity.getBody(), MinesweeperGame.class);
        assertThat(minesweeperGame.getStatus(), is(equalTo(GameStatus.builder().id(1L).name("ACTIVE").build())));
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

        ResponseEntity<String> responseEntity = createGame(createGameRequest, getAuthHeaders());

        assertThat(responseEntity.getStatusCode().is4xxClientError(), is(true));
    }

    @Test
    public void testRevealRevealsAllFieldsWithValueZero() throws JsonProcessingException {
    CreateGameRequest createGameRequest = CreateGameRequest
                .builder()
                .rows(10)
                .columns(10)
                .mines(0)
                .build();
        HttpHeaders authHeaders = getAuthHeaders();
        ResponseEntity<String> responseEntity = createGame(createGameRequest, authHeaders);
        MinesweeperGame minesweeperGame = objectMapper.readValue(responseEntity.getBody(), MinesweeperGame.class);

        RevealPositionRequest revealPositionRequest = RevealPositionRequest.builder()
                .column(0)
                .field(0)
                .build();

        ResponseEntity<String> revealResponse = testRestTemplate.exchange(
                String.format(testUrl + "/v1/minesweeper/%d/reveal", minesweeperGame.getId()),
                HttpMethod.PUT,
                new HttpEntity<>(revealPositionRequest, authHeaders),
                String.class
        );

        assertThat(revealResponse.getStatusCode().is2xxSuccessful(), is(true));
        MinesweeperGame revealedGame = objectMapper.readValue(revealResponse.getBody(), MinesweeperGame.class);
        assertThat(revealedGame.getStatus().getId(), is(equalTo(2L)));
        GameEntity entity = gameRepository.getOne(revealedGame.getId());
        assertThat(entity.getRevealedFields(), is(equalTo(100)));
    }

    private ResponseEntity<String> createGame(CreateGameRequest createGameRequest, HttpHeaders authHeaders) throws JsonProcessingException {
        return testRestTemplate.exchange(
                testUrl + "/v1/minesweeper",
                HttpMethod.POST,
                new HttpEntity<>(createGameRequest, authHeaders),
                String.class
        );
    }
}