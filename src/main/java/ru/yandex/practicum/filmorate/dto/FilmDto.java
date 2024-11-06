package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Value;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.validator.PastDate;

import java.time.LocalDate;
import java.util.Set;

public enum FilmDto {;
    private interface Id { Long getId(); }
    private interface Name { @NotBlank String getName(); }
    private interface Description { @NotBlank @Size(max = 200) String getDescription(); }
    private interface ReleaseDate { @PastDate("1895-12-28") LocalDate getReleaseDate(); }
    private interface Duration { @Positive int getDuration(); }
    private interface MpaRequest { RatingMpaDto getMpa(); }
    private interface MpaResponse { RatingMpa getMpa(); }
    private interface GenresRequest { Set<GenreDto> getGenres(); }
    private interface GenresResponse { Set<Genre> getGenres(); }
    private interface DirectorsRequest { Set<DirectorDto> getDirectors(); }
    private interface DirectorsResponse { Set<Director> getDirectors(); }

    public enum Request {;
        @Value public static class Create implements Name, Description, ReleaseDate, Duration, MpaRequest, GenresRequest, DirectorsRequest {
            String name;
            String description;
            LocalDate releaseDate;
            int duration;
            RatingMpaDto mpa;
            Set<GenreDto> genres;
            Set<DirectorDto> directors;
        }

        @Value public static class Update implements Id, Name, Description, ReleaseDate, Duration, MpaRequest, GenresRequest, DirectorsRequest {
            Long id;
            String name;
            String description;
            LocalDate releaseDate;
            int duration;
            RatingMpaDto mpa;
            Set<GenreDto> genres;
            Set<DirectorDto> directors;
        }
    }

    public enum Response {;
        @Value public static class Public implements Id, Name, Description, ReleaseDate, Duration, MpaResponse, GenresResponse, DirectorsResponse {
            Long id;
            String name;
            String description;
            LocalDate releaseDate;
            int duration;
            RatingMpa mpa;
            Set<Genre> genres;
            Set<Director> directors;
        }
    }
}
