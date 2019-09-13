package test;

import dbmanager.DBExec;

public class Test {
    public static void main(String[] args) {
        new DBExec().update("insert into user (username,password) values(?,?)","ad","asf");
    }

}
