package me.scrobble.mariadb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SimpleDatabaseTest extends DatabaseTest {
    @Test
    public void test() throws SQLException {
        try (Connection connection = DriverManager.getConnection(getDatabaseUrl())) {
            Assert.assertTrue(connection.prepareStatement("SHOW TABLES").execute());
        }
    }
}
