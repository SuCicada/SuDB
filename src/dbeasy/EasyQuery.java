package dbeasy;

import dbmanager.DBException;
import dbmanager.DBExecutor;
import dbmanager.DBManager;

import java.util.List;
import java.util.Map;

public interface EasyQuery {
    DBManager dbManager = DBManager.getInstanc();

    DBExecutor dbExecutor = dbManager.getDBExecutor();

    List<Map<String,Object>> getColumns(String table) throws DBException;

    List<Map<String,Object>> getRows(String table) throws DBException;

    Map<String,List<Object>> showTables() throws DBException;

    default String getDBName() throws DBException {
        return (String) dbExecutor.query("select database() as db;").get(0).get("db");
        //        return dbManager.getDBName();
    }

    default String getDBType(){
        return dbManager.getDBType();
    }
}
