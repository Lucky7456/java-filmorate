package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.util.BaseCrudStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FilmDbStorage extends BaseCrudStorage<Film> implements FilmStorage {
    private static final String TABLE_NAME = "films";
    private static final String FIND_MOST_POPULAR_QUERY =
            "SELECT f.* " +
            "FROM films AS f " +
            "LEFT JOIN likes AS l ON l.film_id = f.id " +
            "%s" +
            "GROUP BY f.id " +
            "ORDER BY COUNT(l.user_id) DESC " +
            "LIMIT ?";
    private static final String FIND_BY_DIRECTORS_QUERY =
            "SELECT f.* " +
            "FROM films AS f " +
            "%s" +
            "WHERE f.id IN (SELECT film_id FROM directors WHERE director_id = ?) " +
            "GROUP BY f.id " +
            "ORDER BY %s";
    private static final String FIND_RECOMMENDATIONS_QUERY =
            "SELECT * FROM films " +
            "WHERE id IN (SELECT film_id " +
            "FROM likes " +
            "WHERE user_id = (SELECT USER_ID " +
            "FROM likes " +
            "WHERE user_id <> ? AND film_id IN (SELECT film_id " +
            "FROM likes " +
            "WHERE user_id = ?) " +
            "GROUP BY user_id " +
            "ORDER BY COUNT(film_id) DESC " +
            "LIMIT 1))";
    private static final String FIND_LIKED_QUERY =
            "SELECT * FROM films " +
            "WHERE id IN (SELECT film_id " +
            "FROM likes " +
            "WHERE user_id = ?)";
    private static final String UPDATE_QUERY =
            "UPDATE films " +
            "SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? " +
            "WHERE id = ?";
    private static final String SEARCH_QUERY =
            "SELECT f.* " +
            "FROM films AS f " +
            "LEFT JOIN likes AS l ON l.film_id = f.id " +
            "%s" +
            "GROUP BY f.id " +
            "ORDER BY COUNT(l.user_id) DESC";
    private static final String FIND_COMMON_QUERY =
            "SELECT f.* " +
            "FROM films AS f " +
            "LEFT JOIN likes AS l ON l.film_id = f.id " +
            "WHERE f.ID IN (SELECT l1.film_id " +
            "FROM LIKES l1 " +
            "JOIN LIKES l2 ON l1.film_id = l2.film_id " +
            "WHERE l1.user_id = ? AND l2.user_id  = ?) " +
            "GROUP BY f.id " +
            "ORDER BY COUNT(l.user_id) DESC";

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper, TABLE_NAME, FIND_MOST_POPULAR_QUERY, UPDATE_QUERY);
    }

    @Override
    public List<Film> search(String query, List<String> by) {
        StringBuilder resultQuery = new StringBuilder();
        List<Object> resultParams = new ArrayList<>();
        resultParams.add(query);
        if (by.size() == 2) {
            resultParams.add(query);
        }
        if (by.contains("director")) {
            resultQuery.append("JOIN directors AS d ON d.id = f.director_id AND d.name ILIKE CONCAT('%', ?, '%') ");
        }
        if (by.contains("title")) {
            resultQuery.append("WHERE f.name ILIKE CONCAT('%', ?, '%') ");
        }

        return findMany(String.format(SEARCH_QUERY, resultQuery), resultParams.toArray());
    }

    @Override
    public List<Film> findSorted(long id, String sortBy) {
        StringBuilder resultQuery = new StringBuilder();
        String orderBy = "";
        if (sortBy.equals("year")) {
            orderBy = "EXTRACT(YEAR FROM release_date) ASC";
        }
        if (sortBy.equals("likes")) {
            resultQuery.append("LEFT JOIN likes AS l ON l.film_id = f.id ");
            orderBy = "COUNT(l.user_id) DESC";
        }

        return findMany(String.format(FIND_BY_DIRECTORS_QUERY, resultQuery, orderBy), id);
    }

    @Override
    public List<Film> findRecommendations(long id) {
        return findMany(FIND_RECOMMENDATIONS_QUERY, id, id);
    }

    @Override
    public List<Film> findLiked(long id) {
        return findMany(FIND_LIKED_QUERY, id);
    }

    @Override
    public List<Film> findCommon(long userId, long friendId) {
        return findMany(FIND_COMMON_QUERY, userId, friendId);
    }

    @Override
    public List<Film> findAllBy(Object... params) {
        StringBuilder resultQuery = new StringBuilder();
        List<Object> resultParams = new ArrayList<>();
        int genreId = (int) params[1];
        if (genreId > 0) {
            resultQuery.append("JOIN genres AS g ON g.film_id = f.id AND g.genre_id = ? ");
            resultParams.add(genreId);
        }
        int year = (int) params[2];
        if (year > 0) {
            resultQuery.append("WHERE EXTRACT(YEAR FROM f.release_date) = ? ");
            resultParams.add(year);
        }
        int count = (int) params[0];
        resultParams.add(count);

        return findMany(String.format(FIND_MOST_POPULAR_QUERY, resultQuery), resultParams.toArray());
    }

    @Override
    protected Map<String, Object> toMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());
        values.put("rating_id", film.getMpa());
        return values;
    }
}
