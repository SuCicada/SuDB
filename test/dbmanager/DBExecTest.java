package dbmanager;

import org.junit.Test;

import static org.junit.Assert.*;

public class DBExecTest {

    @Test
    public void query() {
    }

    @Test
    public void update() {
        try {
            new DBExec().update("insert user(username,password) values('adfadf',md5('adafdf'))");

            new DBExec().fullClose();
            new DBExec().update("insert user(username,password) values('adfadf',md5('adafdf'))");
            new DBExec().fullClose();

            System.out.println();
        } catch (DBException e) {
            e.printStackTrace();
        }

    }
}