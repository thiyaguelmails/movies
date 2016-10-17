/**
 * This source file is subject to the license that is bundled with this package in the file LICENSE.
 */
package com.codeup.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper <T> {

    /**
     * Re-create an existing entity
     *
     * @param resultSet
     * @return A existing entity
     * @throws SQLException
     */
    T mapRow(ResultSet resultSet) throws SQLException;

    /**
     * This method is used to create new entities after an insert operation
     *
     * @param id The identifier generated by the DB for the new entity
     * @param parameters The arguments needed by the entity's constructor
     * @return A new entity
     */
    T newEntity(int id, Object[] parameters);
}
