package dbmethods;

import dbmanager.DBException;
import dbmanager.DBExecutor;
import dbmanager.DBManager;
import org.apache.log4j.Logger;

import java.io.*;

public class TablesCreator {
    private String sql = null;
    private Logger log = Logger.getLogger(this.getClass());
    private DBExecutor dbExecutor = DBManager.getInstanc().getDBExecutor();


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
        dbExecutor.update(sql);
    }

    public void createTableFromSQL(String filePath) throws DBException {
        readSqlFile(filePath);
        executeSql();
    }
    public void createTableFromString(String sql) throws DBException {
        this.sql = sql;
        executeSql();
    }

    public void createTableFromXML(String filePath){

    }

}
