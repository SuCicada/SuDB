package dbmethods;

import dbmanager.DBException;
import dbmanager.DBExec;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.URI;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class CreateFactory {
    private String sql = null;
    private Logger log = Logger.getLogger(this.getClass());

    private void readSqlFile(String filePath){
        StringBuilder sb = new StringBuilder();
        try{
            BufferedReader bf = new BufferedReader(new FileReader(filePath));
            String line = null;
            while((line = bf.readLine())!=null){
                sb.append(line);
            }
            sql = sb.toString();
            log.debug("Table structure: "+ sql);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void executeSql() throws DBException {
        new DBExec().update(sql);
    }

    public void createTableFromFile(String filePath) throws DBException {
        readSqlFile(filePath);
        executeSql();
    }
    public void createTableFromString(String sql) throws DBException {
        this.sql = sql;
        executeSql();
    }


}
