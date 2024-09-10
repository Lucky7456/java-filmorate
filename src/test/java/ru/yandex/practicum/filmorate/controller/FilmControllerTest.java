package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FilmController.class)
public class FilmControllerTest {
    private static final String URL = "/films";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final Film film = new Film();

    @BeforeEach
    void setUp() {
        film.setName("film");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.parse("1895-12-28"));
        film.setDuration(1);
        film.setId(null);
    }

    @Test
    void shouldReturnEmptyList() throws Exception {
        this.mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void shouldReturnBadRequestOnPostEmptyFilm() throws Exception {
        this.mockMvc.perform(post(URL))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnOkOnPostValidFilm() throws Exception {
        this.mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(film.getName()));
    }

    @Test
    void shouldReturnBadRequestOnPutEmptyFilm() throws Exception {
        this.mockMvc.perform(put(URL))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnNotFoundOnPutFilmWithUnmappedId() throws Exception {
        this.mockMvc.perform(put(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film))
                )
                .andExpect(status().isNotFound())
                .andExpect(result -> assertInstanceOf(ValidationException.class, result.getResolvedException()));
    }

    @Test
    void shouldReturnOkOnPutValidFilm() throws Exception {
        MvcResult result = this.mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(film.getName()))
                .andReturn();
        String response = result.getResponse().getContentAsString();

        film.setId(Long.valueOf(JsonPath.parse(response).read("$.id").toString()));
        film.setName("updated" + film.getClass());
        this.mockMvc.perform(put(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(film.getName()));
    }
}
