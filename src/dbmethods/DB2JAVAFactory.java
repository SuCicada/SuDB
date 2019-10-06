package dbmethods;

import dbeasy.EasyQuery;
import dbmanager.DBException;
import dbmanager.DBManager;

/**
 * Created with IntelliJ IDEA.
 * User: peng
 * Date: 10/6/19
 * Time: 4:53 PM
 * To change this template use File | Settings | File Templates.
 * Description:
 */
public class DB2JAVAFactory {

    public static DB2JAVA getDB2JAVA(){
        EasyQuery easyQuery = null;
        DBManager dbManager = DBManager.getInstanc();
        String db2javaFilePath = dbManager.getDb2javaFilePath();

        try {
            easyQuery = dbManager.getDBExecutor().getEasyQuery();
        } catch (DBException e) {
            e.printStackTrace();
        }
        return new DB2JAVA(db2javaFilePath,easyQuery.getDBType());
    }
}
