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
        return dbExecutor.query("select column_name,column_key,data_type,column_comment from" +
                " information_schema.columns where" +
                " table_schema=? and table_name=?;",db,table);
    }

    @Override
    public List<Map<String, Object>> getRows(String table) throws DBException {
        return null;
    }

    /**
     * 查询数据库中的表，按照列排列方式返回
     * @return
     * @throws DBException
     */
    @Override
    public Map<String, List<Object>> showTables() throws DBException {
        return dbExecutor.queryWithRow("select table_name as table_name " +
                "from information_schema.tables where table_schema=database();");
    }
}
