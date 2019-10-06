package dbmanager;

//import org.apache.commons.dbcp.BasicDataSource;
import dbeasy.EasyQuery;
import dbmethods.DB2JAVA;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
//import java.util.logging.Logger;

public class DBManager {
    static DBManager DBM = null;
    private static BasicDataSource ds = null;
    private Logger log = Logger.getLogger(this.getClass());
    private static DBConfig configure;

    private static String JDBC_DRIVER;
    private static String DATABASE;
    private static String DBType;
    private static String DB_URL;
    private static String USER;
    private static String PASSWORD;
    private String characterEncoding;
    private String InitialSize;
    private String MaxActive;
    private String MaxWait;
    private String MinIdle;

    private String entityMapping;

//    private static DB2JAVA db2JAVA;
    private static String db2javaFilePath = "src/db2java.properties";

    private static String dbLookPath = "src/dblook.properties";

    private static String dbLookDriver = "default";
    private DBManager(){}

    public static DBManager getInstanc(){
        return getInstanc(dbLookPath,dbLookDriver);
    }

    public static DBManager getInstanc(String path,String driver){
        if (DBM == null || ds.isClosed()){
            DBM = new DBManager();
            /* 解决关闭连接池，并新开连接池，不占用mysql连接进程*/
            ds = new BasicDataSource();
//            db2JAVA = new DB2JAVA(db2javaFilePath, )
            DBM.DBHelp(path,driver);
        }
        return DBM;
    }

    public DBExecutor getDBExecutor() {
        return new DBExecutor(getInstanc());
    }

    private void DBHelp(String path,String driver){
        Properties properties = new Properties();
        configure = new DBConfig(path);
        configure.config(driver);

        DATABASE = configure.get("database");
        JDBC_DRIVER = configure.get("jdbcDriver");
        DB_URL = configure.get("url");
        USER = configure.get("username");
        PASSWORD = configure.get("password");
        characterEncoding = configure.get("characterEncoding");
        DBType = configure.get("dbType");
        InitialSize = configure.get("initialSize");
        MaxActive = configure.get("maxActive");
        MaxWait = configure.get("maxWait");
        MinIdle = configure.get("minIdle");
        entityMapping = configure.get("entityMapping");

        ds.setDriverClassName(JDBC_DRIVER);
        ds.setUrl(DB_URL);
        ds.setUsername(USER);
        ds.setPassword(PASSWORD);
        if(InitialSize != null && !InitialSize.equals("")){
            ds.setInitialSize(Integer.parseInt(InitialSize));
        }
        if(MaxWait != null && !MaxWait.equals("")){
            // dhcp2 : setMaxWait -> setMaxWaitMillis
            ds.setMaxWaitMillis(Long.parseLong(MaxWait));
        }
        if(MaxActive != null && !MaxActive.equals("")){
            // dhcp2 : setMaxActive -> setMaxTotal
            ds.setMaxTotal(Integer.parseInt(MaxActive));
        }
        if(MinIdle != null && !MinIdle.equals("")){
            ds.setMinIdle(Integer.parseInt(MinIdle));
        }
        //设置连接编码方式
        //ds.addConnectionProperty(name, value);
        System.out.println("数据库初始化已完成.....");
    }

    /**
     * 建立与数据库的链接并链接
     * @return
     */
    public Connection getConnection(){
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 关闭连接池
     */
    public void close(){
        try {
            ds.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到数据库名称
     * @return
     */
    public String getDBName(){
        return DATABASE;
    }

    /**
     * 得到数据库source类型
     * @return
     */
    @Deprecated
    public String getDBType(){
        return DBType;
    }

    public String getDb2javaFilePath(){
        return db2javaFilePath;
    }
    public String getEntityMapping() {
        return entityMapping;
    }

    public String getDbLookPath() {
        return dbLookPath;
    }
    public void setDbLookPath(String dbLookPath) {
        DBManager.dbLookPath = dbLookPath;
    }
    public String getDbLookDriver() {
        return dbLookDriver;
    }
    public void setDbLookDriver(String dbLookDriver) {

        DBManager.dbLookDriver = dbLookDriver;
    }
    /**
     * 设置自己的数据库类型配置文件
     * @param db2javaFilePath
     */
    public void setDb2javaFilePath(String db2javaFilePath){
        DBManager.db2javaFilePath = db2javaFilePath;
    }

}
