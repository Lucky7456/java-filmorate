package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.interfaces.BaseFilmTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FilmController.class)
public class FilmControllerTest extends BaseFilmTest {
    private static final String URL = "/films";

    @MockBean
    private FilmService filmService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnEmptyList() throws Exception {
        this.mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void shouldReturnBadRequestOnPostEmptyFilm() throws Exception {
        when(filmService.create(any(Film.class))).thenReturn(film);
        this.mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Film()))
                )
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void shouldReturnOkOnPostValidFilm() throws Exception {
        when(filmService.create(any(Film.class))).thenReturn(film);
        this.mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(film.getName()));
    }

    @Test
    void shouldReturnBadRequestOnPutEmptyFilm() throws Exception {
        this.mockMvc.perform(put(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Film()))
                )
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void shouldReturnNotFoundOnPutFilmWithUnmappedId() throws Exception {
        when(filmService.update(any(Film.class))).thenThrow(NotFoundException.class);
        this.mockMvc.perform(put(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film))
                )
                .andExpect(status().isNotFound())
                .andExpect(result -> assertInstanceOf(NotFoundException.class, result.getResolvedException()));
    }

    @Test
    void shouldReturnOkOnPutValidFilm() throws Exception {
        when(filmService.create(any(Film.class))).thenReturn(film);
        String response = this.mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(film.getName()))
                .andReturn().getResponse().getContentAsString();

        film.setId(Long.valueOf(JsonPath.parse(response).read("$.id").toString()));
        film.setName("updated" + film.getClass());
        when(filmService.update(any(Film.class))).thenReturn(film);
        this.mockMvc.perform(put(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(film.getName()));
    }
}
