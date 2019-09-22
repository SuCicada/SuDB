package dbeasy;

import dbmanager.DBException;
import dbmanager.DBManager;

import java.util.List;
import java.util.Map;

public interface EasyQuery {
    DBManager dbManager = DBManager.getInstanc();
    List<Map<String,Object>> getColumns(String table) throws DBException;
    List<Map<String,Object>> getRows(String table) throws DBException;

    default String getDBName(){
        return dbManager.getDBName();
    }

    default String getDBType(){
        return dbManager.getDBType();
    }
}
