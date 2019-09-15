package dbmethods;

import dbmanager.DBException;
import dbmanager.DBExec;
import dbmanager.DBManager;

import java.util.List;
import java.util.Map;

public class EasyQuery {
    public static List<Map<String,Object>> getColumns(String table) throws DBException {
        String db = getDBName();
        return new DBExec().query(String.format("select column_name,column_key,data_type,column_comment from" +
                " information_schema.columns where" +
                " table_schema='%s' and table_name='%s';",db,table));
    }
    public static String getDBName(){
        return DBManager.getInstanc().getDBName();
    }

    public static String getDBType(){
        return DBManager.getInstanc().getDBType();
    }
}
