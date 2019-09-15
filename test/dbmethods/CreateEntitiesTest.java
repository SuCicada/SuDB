package dbmethods;

import dbmanager.DBException;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CreateEntitiesTest {

    @Test
    public void createEntities() {
        try {
            new CreateEntities().createEntities(Arrays.asList(new String[]{"user"}));
        } catch (DBException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}