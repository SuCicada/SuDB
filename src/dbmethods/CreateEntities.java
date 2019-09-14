package dbmethods;

import dbmanager.DBException;
import dbmanager.DBExec;

import java.io.*;
import java.net.URI;
import java.nio.file.Paths;
import java.util.*;

public class CreateEntities {
    private String dirName;
//    private List<Map<String, Object>> columns;
    Properties properties = new Properties();

    public void createEntities(List<String> tables) throws DBException, IOException {
        URI uri = Paths.get("src/db2java.properties").toAbsolutePath().toUri();
        File file = new File(uri);
        properties.load(new FileInputStream(file));

        dirName = "entities";
        File dir = new File(dirName);
        dir.mkdir();

        for(String table : tables){
            createEntity(table);
        }
    }
    public void createEntity(String table) throws DBException, IOException {
        String tableName = table.substring(0,1).toUpperCase()+table.substring(1).toLowerCase();
        File javaFile = new File(dirName+"/"+tableName+".java");
        if(javaFile.exists()){
            javaFile.delete();
        }
        javaFile.createNewFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter(javaFile));

    }
    public String buildJavaCode(String tableName) throws IOException, DBException {
        List<Map<String, Object>> columns = EasyQuery.getColumns(tableName.toLowerCase());
        StringBuffer javaSchema = new StringBuffer();
        StringBuffer classMembers = new StringBuffer();
        StringBuffer classGetter = new StringBuffer();
        StringBuffer classSetter = new StringBuffer();

        Set<String> jarSet =  new HashSet<>();

        javaSchema.append("package "+dirName+";\n");



        for(Map<String, Object> column: columns){
            String columnName = (String)column.get("column_name");
            String dataType = (String)column.get("data_type");
            String columnKey = (String)column.get("column_key");
            String columnComment = (String)column.get("column_comment");

            String jar = properties.getProperty(columnKey);
            if(!jarSet.contains(jar)){
                jarSet.add(jar);
                javaSchema.append(String.format("import %s;\n",jar));
            }
            javaSchema.append("\tprivate ");
        }

        javaSchema.append(String.format("public class %s {\n",tableName));

        javaSchema.append(String.format("}");
    }
}
