package dbmethods;

import dbeasy.EasyQuery;
import dbmanager.DBException;
import dbmanager.DBExecutor;
import dbmanager.DBManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: peng
 * Date: 9/29/19
 * Time: 11:50 AM
 * To change this template use File | Settings | File Templates.
 * Description:
 */
public class CodeCreator {
    /** 生成的实体类目录所在目录*/
    private String dirName;
    /** 实体类自定义包名　*/
    private String packageName;
    /** 实体类自定义包路径*/
    private String packagePath;
    /** 数据类型对照表*/
    Map<String,String> db2java;
    String db2javaFilePath;
    /** 数据库类型 */
    private String DBType;

    /** 簡單執行對象 */
    private EasyQuery easyQuery;
    private Logger log = Logger.getLogger(this.getClass());

//    private XMLCodeManager xmlCodeManager;

    public CodeCreator() throws DBException {
        /* 默认数据库类型配置文件 */
        easyQuery = DBManager.getInstanc().getDBExecutor().getEasyQuery();
        db2javaFilePath = "src/db2java.properties";
        packageName = "sucicada";
    }

    /**
     * 设置自己的数据库类型配置文件
     * @param db2javaFilePath
     */
    public void setDb2javaFilePath(String db2javaFilePath){
        this.db2javaFilePath = db2javaFilePath;
    }

    public void setpackageName(String packageName){
        if(packageName != null && packageName.length() > 0){
            this.packageName = packageName;
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

    /**
     * 载入数据库类型配置
     * @throws IOException
     */
    public void loadDB2JAVA() throws IOException {
        if(db2java == null){
            /*读取数据类型参照表*/
            URI uri = Paths.get(db2javaFilePath).toAbsolutePath().toUri();
            File file = new File(uri);
            Properties properties = new Properties();
            properties.load(new FileInputStream(file));
            DBType = easyQuery.getDBType();
            db2java = properties2map(properties);
        }
    }

    /**
     * 创建项目目录结构
     */
    public void createStructure(){
        /* 创建实体类目录*/
//        packageName = "entities";
        String rootdir = "SuDB-out-structure";
        delAll(new File(rootdir));
        packagePath = packageName.replaceAll("\\.","/");
        dirName = rootdir + "/src/"+packagePath+"/";
        File dir = new File(dirName);
        dir.mkdirs();

        new File(dirName+"entities").mkdir();
        System.out.println("Create Structure successful.");
    }

    /**
     * 删除目录下所有文件
     * @param file
     */
    private void delAll(File file){
        if(file.isDirectory()){
            File[] a = file.listFiles();
            for(File f: a){
                delAll(f);
            }
        }
        System.out.println(file.getName());
        file.delete();
    }

    public void crateXML(){
//        List columns =
    }

    public void createXML(String[] tabales) throws DBException, IOException {
        createXML(Arrays.asList(tabales));
    }

    public void createXML(List<String> tables) throws DBException, IOException {
        XMLCodeManager xmlCodeManager = new XMLCodeManager();
        xmlCodeManager.setpackageName(packageName);
//        loadDB2JAVA();
//        xmlCodeManager.setDB2JAVA(db2java);
        for(String table: tables){
            List<Map<String, Object>> columns = easyQuery.getColumns(table);
            xmlCodeManager.createClassElement(table,columns);
        }
        xmlCodeManager.saveXmlFile();
    }

    public void createJavaFromXML(){

    }

    public void createJava(String[] tables){
        createJava(Arrays.asList(tables));
    }

    public void createJava(List<String> tables){
//        JAVACodeManager javaBeanManager = new JAVACodeManager();
//        javaBeanManager.buildCodeFrom
        /*javaCode.init(this);*/
//        javaCode.init(table,columns,DBType,db2java,packagePath,packageName);
//        res = javaCode.buildCode();
    }

    public void createEntities(List<String> tables,String packagePath) throws DBException, IOException {

//        createStructure(packagePath);

        /*循环对每个表生成实体类*/
        for(String table : tables){
            createEntity(table);
        }
    }
    public void createEntities(List<String> tables) throws DBException, IOException {
        createEntities(tables,null);
    }


    private void createEntity(String table) throws DBException {
        /* 根据表名得到表列信息*/
        List<Map<String, Object>> columns = easyQuery.getColumns(table);
//        createXmlCode(table);
//        createJavaBean(table,columns);
    }



}

