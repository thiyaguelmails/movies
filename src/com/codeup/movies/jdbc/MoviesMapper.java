/**
 * This source file is subject to the license that is bundled with this package in the file LICENSE.
 */
package com.codeup.movies.jdbc;

import com.codeup.db.RowMapper;
import com.codeup.movies.Movie;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MoviesMapper implements RowMapper<Movie> {
    @Override
    public Movie mapRow(ResultSet resultSet) throws SQLException {
        return new Movie(
            resultSet.getLong("id"),
            resultSet.getString("title"),
            resultSet.getInt("rating"),
            resultSet.getString("thumbnail")
        );
    }

    @Override
    public Movie mapRow(Object[] values) {
        return new Movie(
            (long) values[0],
            values[1].toString(),
            (int) values[2],
            thumbnail(values[3])
        );
    }

    @Override
    public Movie newEntity(long id, Object[] parameters) {
        return new Movie(
            id,
            parameters[0].toString(),
            (int) parameters[1],
            thumbnail(parameters[2])
        );
    }

    private String thumbnail(Object thumbnail) {
        return thumbnail == null ? null : thumbnail.toString();
    }
}
