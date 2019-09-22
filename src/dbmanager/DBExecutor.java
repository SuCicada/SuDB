package dbmanager;


import dbeasy.EasyQuery;
import dbeasy.EasyQueryForMySQL;

import java.sql.*;
import java.util.*;


public class DBExecutor {
    /**
     * 执行查询sql语句
     * @param sql
     * @return
     */
    public List<Map<String,Object>> query(String sql,String ... args) throws DBException {
        List list = null;
        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = getConnection();
            stat = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                stat.setObject(i + 1, args[i]);
            }
            //System.out.println(stat.toString());
            rs = stat.executeQuery();
            list = resultSetToList(rs);

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            close(rs,stat, conn);
        }
        return list;
    }
    /**
     * 将resultset转化为List
     *
     */
    private List<Map<String, Object>> resultSetToList(ResultSet rs) throws SQLException {
        if (rs == null) {
            return Collections.EMPTY_LIST;
        }
        ResultSetMetaData md = rs.getMetaData();
        int columnCount = md.getColumnCount();
        List list = new ArrayList();
        //将map放入集合中方便使用个别的查询
        Map rowData = new HashMap();
        while (rs.next()) {
            rowData = new LinkedHashMap(columnCount);
            //将集合放在map中
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(rowData);
        }
        return list;
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
            stat = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                stat.setObject(i + 1, args[i]);
            }
            System.out.println(stat.toString());
            result = stat.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
            //            e.printStackTrace();
        } finally {
            close(stat, conn);
        }
        return result;
    }

    /**
     * 得到连接对象
     * @return
     */
    private Connection getConnection() {
        DBManager dbm =  DBManager.getInstanc();
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
     * @return
     */
    public EasyQuery getEasyQuery(){

        EasyQuery easyQuery = new EasyQueryForMySQL();
        return easyQuery;
    }
}
