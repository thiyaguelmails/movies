/*
 * This source file is subject to the license that is bundled with this package in the file LICENSE.
 */
package com.codeup.movies.jdbc;

import com.codeup.db.ConfigurableDataSource;
import com.codeup.db.tests.MySQLSetup;
import com.codeup.movies.Category;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertNotNull;

public class JdbcCategoriesTest {
    @Test
    public void it_finds_existing_category() throws Exception {
        Category thriller = categories.named("thriller");

        assertNotNull(thriller);
        assertEquals("thriller", thriller.name());
    }

    @Test
    public void it_does_not_find_unknown_category() throws Exception {
        Category unknown = categories.named("this is an UNKNOWN category");

        assertNull(unknown);
    }

    @Test
    public void it_finds_all_categories_in_set() {
        List<Category> matching = categories.in("1", "4", "5");

        assertEquals(3, matching.size());
    }

    @Test
    public void it_finds_only_existing_categories_in_set() {
        List<Category> matching = categories.in("1", "4", "unknown");

        assertEquals(2, matching.size());
    }

    @Test
    public void it_finds_all_categories() {
        List<Category> all = categories.all();

        assertEquals(5, all.size());
    }

    @Test
    public void it_finds_all_categories_related_to_a_movie() throws Exception {
        JdbcMovies movies = new JdbcMovies(source.getConnection());

        List<Category> related = categories.relatedTo(movies.with(1));

        assertEquals(1, related.size());
    }

    @Test
    public void it_adds_a_new_category() {
        categories.add(Category.named("action"));

        assertNotNull(categories.named("action"));
    }

    @Before
    public void loadFixtures() throws Exception {
        source = ConfigurableDataSource.using(MySQLSetup.configuration());
        MySQLSetup.truncate(source, "movies_categories", "categories", "movies");
        Path path = Paths.get("src/test/resources/categories.xml");
        MySQLSetup.loadDataSet(source, path.toAbsolutePath().toString());

        categories = new JdbcCategories(source.getConnection());
    }

    private JdbcCategories categories;
    private MysqlDataSource source;
}
