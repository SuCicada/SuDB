package dbmethods;

import dbmanager.DBException;
import dbmanager.DBExec;

import java.io.*;
import java.net.URI;
import java.nio.file.Paths;
import java.util.*;

public class CreateEntities {
    private String dirName;
    Properties properties = new Properties();
    private String packagePath;

    public void createEntities(List<String> tables) throws DBException, IOException {
        createEntities(tables,null);
    }
    public void createEntities(List<String> tables,String packagePath) throws DBException, IOException {
        /*读取数据类型参照表*/
        URI uri = Paths.get("src/db2java.properties").toAbsolutePath().toUri();
        File file = new File(uri);
        properties.load(new FileInputStream(file));

        /* 创建实体类目录*/
        dirName = "entities";
        File dir = new File(dirName);
        dir.mkdir();

        /* 生成的实体类的packet目录*/
        if(packagePath != null && packagePath.length()>0){
            if(packagePath.charAt(packagePath.length()-1) != '.'){
                packagePath += '.';
            }
            this.packagePath = packagePath;
        }else{
            this.packagePath = "";
        }

        /*循环对每个表生成实体类*/
        for(String table : tables){
            createEntity(table);
        }
    }

    private void createEntity(String table) throws DBException, IOException {
        String tableName = table.substring(0,1).toUpperCase()+table.substring(1).toLowerCase();
        File javaFile = new File(dirName+"/"+tableName+".java");
        if(javaFile.exists()){
            javaFile.delete();
        }
        javaFile.createNewFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter(javaFile));

        /* 将构造好的java代码写入到文件中*/
        bw.write(buildJavaCode(tableName));
        bw.close();
    }
    private String buildJavaCode(String tableName) throws IOException, DBException {
        List<Map<String, Object>> columns = EasyQuery.getColumns(tableName.toLowerCase());
        String DBType = EasyQuery.getDBType();

        StringBuffer javaSchema = new StringBuffer();
        StringBuffer javaSchemaConstructorParameter = new StringBuffer();
        StringBuffer javaSchemaConstructorBody = new StringBuffer();
        StringBuffer classMembers = new StringBuffer();
        StringBuffer classGetter = new StringBuffer();
        StringBuffer classSetter = new StringBuffer();

        Set<String> jarSet =  new HashSet<>();

        javaSchema.append(packagePath + "package "+dirName+";\n\n");

        for(Map<String, Object> column: columns){
            String columnName = (String)column.get("column_name");
            String dataType = (String)column.get("data_type");
            String columnKey = (String)column.get("column_key");
            String columnComment = (String)column.get("column_comment");

            if(columnName==null) {
                columnName = (String)column.get("COLUMN_NAME");
            }
            if(dataType==null) {
                dataType = (String)column.get("DATA_TYPE");
            }
            if(columnKey==null) {
                columnKey = (String)column.get("COLUMN_KEY");
            }
            if(columnComment==null) {
                columnComment = (String)column.get("COLUMN_COMMENT");
            }

            String jar = properties.getProperty(DBType + "." + dataType.toUpperCase());
            if(!jarSet.contains(jar)){
                jarSet.add(jar);
                javaSchema.append(String.format("import %s;\n",jar));
            }
            String type = jar.replaceAll(".*\\.","");
            String varName = columnName.substring(0,1).toUpperCase()+columnName.substring(1).toLowerCase();
            classMembers.append(String.format("\tprivate %s %s;\n", type, columnName));
            classSetter.append(String.format("\tpublic void set%s(%s %s) {\n" +
                    "\t\tthis.%s = %s;\n\t}\n\n",varName, type, columnName, columnName, columnName));
            classGetter.append(String.format("\tpublic %s get%s() {\n" +
                    "\t\treturn %s;\n\t}\n\n", type, varName, columnName));

            if(javaSchemaConstructorParameter.length()>0){
                javaSchemaConstructorParameter.append(", ");
            }
            javaSchemaConstructorParameter.append(type + " " + columnName);
            javaSchemaConstructorBody.append(String .format("\t\tthis.%s = %s;\n",columnName, columnName));
        }

        javaSchema.append("\n");
        javaSchema.append(String.format("public class %s {\n",tableName));
        javaSchema.append(classMembers);
        javaSchema.append("\n");
        javaSchema.append(String.format("\tpublic %s(){}\n\n",tableName));
        javaSchema.append(String.format("\tpublic %s(%s) {\n" +
                "%s\t}\n\n",tableName, javaSchemaConstructorParameter.toString(), javaSchemaConstructorBody.toString()));
        javaSchema.append(classSetter);
        javaSchema.append(classGetter);
        /* 消除最后一个空行*/
        javaSchema.delete(javaSchema.length()-1,javaSchema.length());
        javaSchema.append(String.format("}\n"));
        return javaSchema.toString();
    }
}
