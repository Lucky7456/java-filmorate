package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mappers.GenreRowMapper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({GenreDbStorage.class, GenreRowMapper.class})
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GenreStorageTest {
    private final GenreStorage storage;
    
    @Test
    public void testFindAllGenres() {
        assertThat(storage.findAll()).hasSize(6);
    }
    
    @Test
    public void testFindGenreById() {
        int id = 1;
        Optional<Genre> genreOptional = storage.findOneById(id);
        
        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(
                        genre -> assertThat(genre)
                                .hasFieldOrPropertyWithValue("id", id)
                );
    }
    
    @Test
    public void testFindAllGenresOfFilm() {
        long id = 4L;
        
        assertThat(storage.findAllBy(id))
                .hasOnlyElementsOfType(Genre.class)
                .hasSize(3);
    }
}
