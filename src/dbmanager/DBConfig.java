package dbmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: peng
 * Date: 9/22/19
 * Time: 3:15 PM
 * To change this template use File | Settings | File Templates.
 * Description:
 */
public class DBConfig {
    private String JDBC_DRIVER;
    private String DATABASE;
    private String DBType;
    private String DB_URL;
    private String DB_URL_Head;
    private String USER;
    private String PASSWORD;
    private String characterEncoding;
    private String Port;
    private String Host;

    private String InitialSize;
    private String MaxActive;
    private String MaxWait;
    private String MinIdle;
    
    private String configFilePath;
    private String driver;
    private Properties properties;
//    public DBConfig() {
//        this.configFilePath = "src/dblook.properties";
//    }
    public DBConfig(String configFilePath){
        this.configFilePath = configFilePath;
    }

//    public void config(){
//        this.config("default");
//    }

    private Properties readPropertiesFile() throws IOException {
        Properties properties = new Properties();
        URI uri = Paths.get(configFilePath).toAbsolutePath().toUri();
        System.out.println("properties file: " + uri);
        properties.load(new FileInputStream(new File(uri)));
        return properties;
    }

    public void config(String driver){
        this.driver = driver;
        try{
            properties = readPropertiesFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String get(String name){
        return properties.getProperty(driver+"."+name);
    }

    @Deprecated
    public void config_old(String driver){
        try{
            Properties properties = readPropertiesFile();

            this.Port = properties.getProperty(driver+".port");
            this.Host = properties.getProperty(driver+".host");
            this.DATABASE = properties.getProperty(driver+".database");

            this.JDBC_DRIVER = properties.getProperty(driver + ".driver");
            this.DB_URL = properties.getProperty(driver + ".url");
            this.DB_URL_Head = properties.getProperty(driver + ".urlHead");

            if(this.DB_URL.length() == 0 || this.DB_URL == null){
                /* 优先取 DB_URL, 没有在进行url拼接*/
                this.DB_URL = DB_URL_Head + Host+":"+Port+"/"+DATABASE;
            }
            this.USER = properties.getProperty(driver + ".username");
            this.PASSWORD = properties.getProperty(driver + ".password");
            this.characterEncoding = properties.getProperty(driver + ".characterEncoding");
            this.DBType = properties.getProperty(driver + ".databaseType");

            this.InitialSize = properties.getProperty(driver + ".InitialSize");
            this.MaxActive = properties.getProperty(driver + ".maxActive");
            this.MaxWait = properties.getProperty(driver + ".maxWait");
            this.MinIdle = properties.getProperty(driver + ".minIdle");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public String get_old(String name){
        String res = null;
        try {
            Class clazz = this.getClass();
            Field field = clazz.getDeclaredField(name);
            if(field != null){
                res = (String) field.get(this);
            }


        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return res;
    }
}
