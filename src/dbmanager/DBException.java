package dbmanager;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class DBException extends Exception {

    /**
     * Creates a new instance of <code>org.su.dbmanager.DBException</code> without detail
     * message.
     */
    public DBException() {
    }

    /**
     * Constructs an instance of <code>org.su.dbmanager.DBException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public DBException(String msg) {
        super(msg);
    }
}
