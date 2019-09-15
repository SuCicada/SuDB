package dbmethods;

import dbmanager.DBException;
import dbmethods.CreateFactory;
import org.junit.Test;

public class CreateFactoryTest {

    @Test
    public void readSqlFile() {
        try{
            new CreateFactory().createTableFromFile("src/tables.sql");

        } catch (DBException e) {
            System.out.println(e.getMessage());
        }


    }
}