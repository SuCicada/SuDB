package dbmethods;

import dbmanager.DBException;
import dbmanager.DBExec;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.List;
import java.util.Map;

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

    public void createEntities(List<String> tables) throws DBException, IOException {
        String dirName = "entities";
        File dir = new File(dirName);
        dir.mkdir();

        for(String table : tables){
            createEntity(table,dirName);
        }
    }
    public void createEntity(String table,String dirName) throws DBException, IOException {
        List<Map<String, Object>> columns = EasyQuery.getColumns(table);
        String tableName = table.substring(0,1).toUpperCase()+table.substring(1).toLowerCase();
        File javaFile = new File(dirName+"/"+tableName+".java");
        if(javaFile.exists()){
            javaFile.delete();
        }
        javaFile.createNewFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter(javaFile));

    }
    public String buildJavaCode(String dirName,String tableName,List<Map<String, Object>> columns){
        StringBuffer javaSchema = new StringBuffer();
        StringBuffer classMembers = new StringBuffer();
        StringBuffer classGetter = new StringBuffer();
        StringBuffer classSetter = new StringBuffer();


        javaSchema.append("package "+dirName+";\n");

        javaSchema.append(String.format("public class %s {",tableName));

        for(Map<String, Object> column: columns){
            String columnName = (String)column.get("column_name");
            String dataType = (String)column.get("data_type");
            String columnKey = (String)column.get("column_key");
            String columnComment = (String)column.get("column_comment");K
            javaSchema.append("\tprivate ");
        }

    }
}
