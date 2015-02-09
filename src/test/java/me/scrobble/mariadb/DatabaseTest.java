package me.scrobble.mariadb;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;

public abstract class DatabaseTest {

    private File baseDir;
    private DB server;
    private String database;
    private String port;

    @BeforeClass
    @Parameters({"port"})
    public void start(@Optional("2906") String port) throws ManagedProcessException, IOException,
            ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        this.port = port;
        database = UUID.randomUUID().toString();
        baseDir = Files.createTempDirectory("KiwiGridCloudDatabase-MariaDB").toFile();
        DBConfigurationBuilder configurationBuilder = DBConfigurationBuilder.newBuilder();
        configurationBuilder.setPort(Integer.parseInt(port));
        configurationBuilder.setBaseDir(baseDir.getAbsolutePath());
        configurationBuilder.setDataDir(baseDir.getAbsolutePath() + File.separator + "data-dir");
        server = DB.newEmbeddedDB(configurationBuilder.build());
        server.start();
        server.createDB(database);
    }

    @AfterTest
    public void stop() throws ManagedProcessException, InterruptedException {
        server.stop();
        Thread.sleep(30000); // May need some time to stop.
        FileUtils.deleteQuietly(baseDir);
    }

    public String getDatabaseUrl() {
        return "jdbc:mysql://localhost:" + port + "/" + database;
    }
}
