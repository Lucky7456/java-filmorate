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
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    private static final String URL = "/users";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final User user = new User();

    @BeforeEach
    void setUp() {
        user.setName("userName");
        user.setEmail("valid@mail.ru");
        user.setLogin("loginSuccess");
        user.setBirthday(LocalDate.now().minusDays(1));
        user.setId(null);
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
                        .content(objectMapper.writeValueAsString(user))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(user.getName()));
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
                        .content(objectMapper.writeValueAsString(user))
                )
                .andExpect(status().isNotFound())
                .andExpect(result -> assertInstanceOf(ValidationException.class, result.getResolvedException()));
    }

    @Test
    void shouldReturnOkOnPutValidFilm() throws Exception {
        MvcResult result = this.mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andReturn();
        String response = result.getResponse().getContentAsString();

        user.setId(Long.valueOf(JsonPath.parse(response).read("$.id").toString()));
        user.setName("updated" + user.getClass());
        this.mockMvc.perform(put(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(user.getName()));
    }
}
