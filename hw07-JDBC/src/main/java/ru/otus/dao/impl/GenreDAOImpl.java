package ru.otus.dao.impl;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import ru.otus.dao.GenreDAO;
import ru.otus.entity.Genre;
import ru.otus.mapper.GenreRowMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class GenreDAOImpl implements GenreDAO {
    private static final String FIND_BY_NAME =
            "select * from genres where genre_name = :name";

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    private final GenreRowMapper genreRowMapper;

    public GenreDAOImpl(NamedParameterJdbcOperations namedParameterJdbcOperations, GenreRowMapper genreRowMapper) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
        this.genreRowMapper = genreRowMapper;
    }

    @Override
    public Optional<Genre> findByName(String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        final var genres = namedParameterJdbcOperations.query(FIND_BY_NAME, params, genreRowMapper);
        if (CollectionUtils.isEmpty(genres)) {
            return Optional.empty();
        }
        return Optional.ofNullable(genres.get(0));
    }
}
