package dbeasy;

import dbmanager.DBException;
import dbmanager.DBExecutor;

import java.util.List;
import java.util.Map;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: peng
 * \* Date: 9/15/19
 * \* Time: 7:28 PM
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 * @author peng
 */
public class EasyQueryForMySQL implements EasyQuery{

    @Override
    public  List<Map<String,Object>> getColumns(String table) throws DBException {
        String db = getDBName();
        return dbExecutor.query(String.format("select column_name,column_key,data_type,column_comment from" +
                " information_schema.columns where" +
                " table_schema='%s' and table_name='%s';",db,table));
    }

    @Override
    public List<Map<String, Object>> getRows(String table) throws DBException {
        return null;
    }
}
