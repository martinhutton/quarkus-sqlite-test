package org.test.com;

import javax.enterprise.inject.spi.CDI;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

import io.agroal.api.AgroalDataSource;
import io.quarkus.logging.Log;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

@QuarkusMain
public class Main {

    public static void main(String... args) {

        Quarkus.run(MyApp.class, args);
    }

    public static class MyApp implements QuarkusApplication {

        @Override
        public int run(String... args) throws Exception {

            Connection connection = null;
            try {
                connection = runDbMigration();
            } catch (SQLException e) {

                URL url = getClass().getResource("entities.db");

                Log.error("Database not created");
                Files.createFile(Path.of(url.getPath()));
                connection = runDbMigration();
            }
            catch (LiquibaseException e) {
                URL url = getClass().getResource("entities.db");

                Log.error("Database corrupt, trashing and bringing up new database");
                Files.delete(Path.of(url.getPath()));
                Files.createFile(Path.of(url.getPath()));
                connection = runDbMigration();

            } finally {
                if (connection != null) {
                    try {
                        connection.rollback();
                        connection.close();
                    } catch (SQLException e) {
                        //nothing to do
                    }
                }
            }
            Quarkus.waitForExit();
            return 0;
        }

        public Connection runDbMigration() throws LiquibaseException, SQLException {


            AgroalDataSource sqliteDb = CDI.current().select(AgroalDataSource.class).get();
            Connection connection;
            connection = sqliteDb.getConnection();
            Liquibase liquibase;

            Database database = DatabaseFactory.getInstance()
                                               .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            liquibase = new Liquibase(
                    "/db/changeLog.xml"
                    ,
                    new ClassLoaderResourceAccessor(),
                    database);
            liquibase.update(new Contexts(), new LabelExpression());

            return connection;
        }

    }
}
