package dbmethods;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: peng
 * Date: 10/6/19
 * Time: 4:12 PM
 * To change this template use File | Settings | File Templates.
 * Description:
 */
public class DB2JAVA {
    Map<String,String> db2java;
    String db2javaFilePath;
    String DBType;

    public DB2JAVA(String db2javaFilePath, String DBType) {
        this.db2javaFilePath = db2javaFilePath;
        this.DBType = DBType;
        try {
            loadDB2JAVA();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 载入数据库类型配置
     * @throws IOException
     */
    private void loadDB2JAVA() throws IOException {
        if(db2java == null){
            /*读取数据类型参照表*/
            URI uri = Paths.get(db2javaFilePath).toAbsolutePath().toUri();
            File file = new File(uri);
            Properties properties = new Properties();
            properties.load(new FileInputStream(file));
//            DBType = easyQuery.getDBType();
            db2java = properties2map(properties);
        }
    }
    /**
     * 类型配置文件转map，消除数据库特异性
     * @param properties
     * @return
     */
    private Map properties2map(Properties properties){
        Map<String,String> db2java = new HashMap();
        for(Map.Entry<Object, Object> set :properties.entrySet()){
            String[] en = set.getKey().toString().split("\\.");
            if(en[0].equals(DBType)){
                db2java.put(en[1], (String) set.getValue());
            }
        }
        return db2java;
    }

    public Map<String,String> getMap(){
        return db2java;
    }

    public String get(String key){
        return db2java.get(key.toUpperCase());
    }

    public boolean isEmpty(){
        return db2java.isEmpty();
    }
}
