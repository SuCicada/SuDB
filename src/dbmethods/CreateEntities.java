package dbmethods;

import dbeasy.EasyQuery;
import dbmanager.DBException;
import dbmanager.DBExecutor;
import dbmanager.DBManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.URI;
import java.nio.file.Paths;
import java.util.*;

/**
 *
 * @author  SuCicada
 */
public class CreateEntities {
    /** 生成的实体类目录所在目录*/
    private String dirName;
    /** 实体类自定义包名　*/
    private String packageName;
    /** 实体类自定义包路径*/
    private String packagePath;
    /** 数据类型对照表*/
    Properties properties = new Properties();
    EasyQuery easyQuery = DBManager.getInstanc().getDBExecutor().getEasyQuery();
    Logger log = Logger.getLogger(this.getClass());

    public CreateEntities() throws DBException {
    }


    public void createEntities(List<String> tables) throws DBException, IOException {
        createEntities(tables,null);
    }

    public void createEntities(List<String> tables,String packagePath) throws DBException, IOException {
        /*读取数据类型参照表*/
        URI uri = Paths.get("src/db2java.properties").toAbsolutePath().toUri();
        File file = new File(uri);
        properties.load(new FileInputStream(file));

        /* 创建实体类目录*/
        packageName = "entities";
        dirName = "out/" + packageName;
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
        /* 对表名修改，指定类名 */
        String tableName = table.substring(0,1).toUpperCase()+table.substring(1).toLowerCase();
        File javaFile = new File(dirName+"/"+tableName+".java");
        if(javaFile.exists()){
            javaFile.delete();
        }
        javaFile.createNewFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter(javaFile));

        /* 将构造好的java代码写入到文件中*/
//        bw.write(buildJavaCode(tableName));
        bw.close();
        System.out.println("Create Entity successful.");
    }

    private void createXML(){

    }

}
