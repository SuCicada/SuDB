package dbmanager;

//import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
//import java.util.logging.Logger;

public class DBManager {
    static DBManager DBM = null;
    Connection connection;
    static String JDBC_DRIVER;
    static String DATABASE;
    static String DBType;
    static String DB_URL;
    static String USER;
    static String PASSWORD;
    private static BasicDataSource ds = null;
    private String db;
    private String characterEncoding;
    Logger log = Logger.getLogger(this.getClass());

    private DBManager(){}

    public static DBManager getInstanc(){
        if (DBM == null || ds.isClosed()){
            DBM = new DBManager();
            /* 解决关闭连接池，并新开连接池，不占用mysql连接进程*/
            ds = new BasicDataSource();
            DBM.DBHelp();
        }
        return DBM;
    }

    public static DBManager getInstance(String driver){
        if (DBM == null){
            DBM = new DBManager();
            DBM.DBHelp(driver);
        }
        return DBM;
    }

    private void DBHelp(String driver){
        Properties properties = new Properties();
        try {
            URI uri = Paths.get("src/dblook.properties").toAbsolutePath().toUri();
            System.out.println(uri);
            System.out.println("................................................");
            //properties.load(new java.io.FileInputStream(new java.io.File(url.toURI())));

            File file = new File(uri);
            properties.load(new java.io.FileInputStream(file));


            String Host = properties.getProperty(driver+".host");
            String Port = properties.getProperty(driver+".port");
            DATABASE = properties.getProperty(driver+".database");
            JDBC_DRIVER = properties.getProperty(driver + ".driver");
            DB_URL = properties.getProperty(driver + ".url")+Host+":"+Port+"/"+DATABASE;
            USER = properties.getProperty(driver + ".username");
            PASSWORD = properties.getProperty(driver + ".password");
            characterEncoding = properties.getProperty(driver + ".characterEncoding");
            DBType = properties.getProperty(driver + ".databaseType");

            String InitialSize = properties.getProperty(driver + ".InitialSize");
            String MaxActive = properties.getProperty(driver + ".MaxActive");
            String MaxWait = properties.getProperty(driver + ".MaxWait");
            String MinIdle = properties.getProperty(driver + ".MinIdle");

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
            //System.out.println("数据库初始化已完成.....");
        } catch (FileNotFoundException ex) {
            log.error(ex.getMessage());
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    private void DBHelp() {
        this.DBHelp("default");
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

    public void close(){
        try {
            ds.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getDBName(){
        return DATABASE;
    }

    public String getDBType(){
        return DBType;
    }
}
