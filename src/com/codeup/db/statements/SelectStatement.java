/**
 * This source file is subject to the license that is bundled with this package in the file LICENSE.
 */
package com.codeup.db.statements;

import com.codeup.db.QueryParameters;
import com.codeup.db.RowMapper;
import com.codeup.db.builders.HasSQLRepresentation;
import com.codeup.db.builders.queries.Select;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class SelectStatement<T> {
    private final Connection connection;
    private final Select select;
    private final RowMapper<T> mapper;

    public SelectStatement(
        Connection connection,
        String table,
        RowMapper<T> mapper
    ) {
        this.connection = connection;
        this.select = Select.from(table);
        this.mapper = mapper;
    }

    public SelectStatement(SelectStatement<T> statement) {
        this(statement.connection, statement.select, statement.mapper);
    }

    private SelectStatement(
        Connection connection,
        Select select,
        RowMapper<T> mapper
    ) {
        this.connection = connection;
        this.select = select;
        this.mapper = mapper;
    }

    public SelectStatement<T> select(String... columns) {
        select.columns(columns);
        return this;
    }

    public SelectStatement<T> count() {
        select.count();
        return this;
    }

    public SelectStatement<T> addAlias(String alias) {
        select.addTableAlias(alias);
        return this;
    }

    public SelectStatement<T> matching(Criteria criteria) {
        criteria.applyTo(select);
        return this;
    }

    public SelectStatement<T> where(String clause) {
        select.where(clause);
        return this;
    }

    public SelectStatement<T> whereIn(String column, String[] values) {
        select.where(column, values.length);
        return this;
    }

    public SelectStatement<T> offset(int offset) {
        select.offset(offset);
        return this;
    }

    public SelectStatement<T> limit(int limit) {
        select.limit(limit);
        return this;
    }

    public SelectStatement<T> join(String table, String on) {
        select.join(table, on);
        return this;
    }

    public Hydrator<T> execute(Object... parameters) {
        try (PreparedStatement statement = connection.prepareStatement(
            select.toSQL()
        )) {
            QueryParameters.bind(statement, parameters);

            ResultSet resultSet = statement.executeQuery();

            return new Hydrator<>(resultSet, mapper);
        } catch (SQLException e) {
            throw queryException(select, parameters, e);
        }
    }

    private RuntimeException queryException(
        HasSQLRepresentation statement,
        Object[] parameters,
        SQLException cause
    ) {
        return new RuntimeException(
            String.format(
                "Cannot execute statement %s with parameters %s",
                statement.toSQL(),
                Arrays.toString(parameters)
            ),
            cause
        );
    }
}
