package dbmanager;


import dbeasy.EasyQuery;
import dbeasy.EasyQueryForMySQL;

import java.sql.*;
import java.util.*;


public class DBExecutor {
    private DBManager dbm;

    public DBExecutor(DBManager dbm){
//         dbm = DBManager.getInstanc();
        this.dbm = dbm;
    }

    /**
     * 执行查询sql语句
     * @param sql
     * @return
     */
    public List<Map<String,Object>> query(String sql,String ... args) throws DBException {
        return (List<Map<String, Object>>) queryCore(sql,args,queryMethod.column);
    }

    /**
     * 执行查询sql语句，结果以列分组
     * @param sql
     * @param args
     * @return
     * @throws DBException
     */
    public Map<String,List<Object>> queryWithRow(String sql,String ... args) throws DBException {
        return (Map<String, List<Object>>) queryCore(sql,args,queryMethod.row);
    }

    /**
     * 将resultset转化为List
     *
     */
    private List<Map<String, Object>> resultSetToListWithColumn(ResultSet rs) throws SQLException {
        if (rs == null) {
            return Collections.EMPTY_LIST;
        }
        ResultSetMetaData md = rs.getMetaData();
        int columnCount = md.getColumnCount();
        List list = new ArrayList();
        //将map放入集合中方便使用个别的查询
        while (rs.next()) {
        Map rowData = new LinkedHashMap(columnCount);
            //将集合放在map中
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnLabel(i), rs.getObject(i));
            }
            list.add(rowData);
        }
        return list;
    }

    public Map<String,List<Object>> resultSetToListWithRow(ResultSet rs) throws SQLException {
        if (rs == null) {
            return (Map<String, List<Object>>) Collections.EMPTY_LIST;
        }
        ResultSetMetaData md = rs.getMetaData();
        int columnCount = md.getColumnCount();
        rs.last();
        int rowCount = rs.getRow();
        rs.beforeFirst();
        Map<String,List<Object>> rowData = new LinkedHashMap(columnCount);
        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                String column = md.getColumnLabel(i);
                List<Object> list = rowData.get(column);
                if(list == null || list.isEmpty()){
                    list= new ArrayList(rowCount);
                    rowData.put(column, list);
                }
                list.add(rs.getObject(i));
            }
        }
        return rowData ;
    }

    private enum queryMethod{row,column};
    private Object queryCore(String sql,String []args,queryMethod queryMethod) throws DBException {
        Object res = null;
        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = getConnection();
            stat = prepareSQL(conn,sql,args);
            System.out.println(stat.toString());
            rs = stat.executeQuery();
            switch(queryMethod){
                case row:
                    res = resultSetToListWithRow(rs);
                    break;
                case column:
                    res = resultSetToListWithColumn(rs);
            }

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            close(rs,stat, conn);
        }
        return res;
    }


    /**
     * 执行更新sql语句
     * @param sql
     * @return int，反映影响数据表中的记录数
     */
    public int update(String sql,String ... args) throws DBException {
        int result = 0;
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = getConnection();
            stat = prepareSQL(conn,sql,args);
            System.out.println(stat.toString());
            result = stat.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            close(stat, conn);
        }
        return result;
    }

    public int update(String sql,List<String> args) throws DBException {
        return update(sql,args.toArray(new String[args.size()]));
    }


    private PreparedStatement prepareSQL(Connection conn,String sql,String ... args) throws SQLException {
        PreparedStatement stat = conn.prepareStatement(sql);
        for (int i = 0; i < args.length; i++) {
            stat.setObject(i + 1, args[i]);
        }
        return stat;
    }

    /**
     * 得到连接对象
     * @return
     */
    private Connection getConnection() {
        return dbm.getConnection();
    }

    /**
     * 释放连接
     *
     * @param stat
     * @param conn
     */
    private void close(PreparedStatement stat, Connection conn) {
        close(null, stat, conn);
    }

    /**
     * 释放连接
     *
     * @param rs
     * @param stat
     * @param conn
     */
    private void close(ResultSet rs, PreparedStatement stat, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stat != null) {
                    stat.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 关闭连接池
     */
    public void fullClose(){
        DBManager.getInstanc().close();
    }

    /**
     * 得到easyQuery
     * [这里怎么做工程模式]
     * [使用了簡單工廠]
     * @return
     */
    public EasyQuery getEasyQuery() throws DBException {
        String dbType = DBManager.getInstanc().getDBType();
        EasyQuery easyQuery = null;

        switch (dbType){
            case "mysql":
                easyQuery = new EasyQueryForMySQL();
                break;
            default:
                throw new DBException("Unknown DBType: " + dbType);
        }
        return easyQuery;
    }
}
