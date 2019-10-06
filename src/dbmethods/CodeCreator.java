package dbmethods;

import dbeasy.EasyQuery;
import dbmanager.DBException;
import dbmanager.DBExecutor;
import dbmanager.DBManager;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;

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
    private String rootDir;
    /** 数据类型对照表*/
//    Map<String,String> db2java;
    DB2JAVA db2java;
//    String db2javaFilePath;
    /** 数据库类型 */
    private String DBType;

    /** 簡單執行對象 */
    private EasyQuery easyQuery;
    private DBManager dbManager;
    private Logger log = Logger.getLogger(this.getClass());

    public CodeCreator(DBManager dbManager) throws DBException, IOException {
        /* 默认数据库类型配置文件 */
        this.dbManager = dbManager;
        easyQuery = dbManager.getDBExecutor().getEasyQuery();
//        db2javaFilePath = "src/db2java.properties";
        packageName = "sucicada";
        rootDir = "SuDB-out-structure";
//        db2java = new DB2JAVA(db2javaFilePath,easyQuery.getDBType());
        db2java = DB2JAVAFactory.getDB2JAVA();
    }


    public void setpackageName(String packageName){
        if(packageName != null && packageName.length() > 0){
            this.packageName = packageName;
        }
    }

//    /**
//     * 类型配置文件转map，消除数据库特异性
//     * @param properties
//     * @return
//     */
//    private Map properties2map(Properties properties){
//        Map<String,String> db2java = new HashMap();
//        for(Map.Entry<Object, Object> set :properties.entrySet()){
//            String[] en = set.getKey().toString().split("\\.");
//            if(en[0].equals(DBType)){
//                db2java.put(en[1], (String) set.getValue());
//            }
//        }
//        return db2java;
//    }

//    /**
//     * 载入数据库类型配置
//     * @throws IOException
//     */
//    public void loadDB2JAVA() throws IOException {
//        if(db2java == null){
//            /*读取数据类型参照表*/
//            URI uri = Paths.get(db2javaFilePath).toAbsolutePath().toUri();
//            File file = new File(uri);
//            Properties properties = new Properties();
//            properties.load(new FileInputStream(file));
//            DBType = easyQuery.getDBType();
//            db2java = properties2map(properties);
//        }
//    }

    /**
     * 创建项目目录结构
     */
    public void createStructure(){
        /* 创建实体类目录*/
//        packageName = "entities";
        if(new File(rootDir).exists()){
            System.out.println("Structure exists: "+rootDir);
//            return ;
        }
        dirName = rootDir + "/src/"+packageName.replaceAll("\\.","/")+"/";
        File dir = new File(dirName);
        dir.mkdirs();

        packagePath = dirName+"entities";
        new File(packagePath).mkdirs();
        System.out.println("Create Structure successful.");
    }

    public void cleanStructure(){
        delAll(new File(rootDir));
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

    public void createXML() throws DBException, IOException {
        List columns = easyQuery.showTables().get("table_name");
        System.out.println(columns);
        createXML(columns);
    }

    public void createXML(String[] tables) throws DBException, IOException {
        createXML(Arrays.asList(tables));
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
        xmlCodeManager.saveXmlFile(rootDir+"/src/entities.sudb.xml");
    }

    public void createJavaFromXML() throws DocumentException, DBException, IOException {
        createJavaFromXML(rootDir+"/src/entities.sudb.xml");
    }
    public void createJavaFromXML(String XMLpath) throws DocumentException, DBException, IOException {
        JAVACodeManager javaBeanManager = new JAVACodeManager();
        createStructure();
//        loadDB2JAVA();
        javaBeanManager.setPackageName(packageName);
        javaBeanManager.setPackagePath(packagePath);
        javaBeanManager.setDb2java(db2java);
        javaBeanManager.createFromXml(XMLpath);
    }

    @Deprecated
    public void createJava(String[] tables){
        createJava(Arrays.asList(tables));
    }

    @Deprecated
    public void createJava(List<String> tables){
//        javaBeanManager.buildCodeFrom
        /*javaCode.init(this);*/
//        javaCode.init(table,columns,DBType,db2java,packagePath,packageName);
//        res = javaCode.buildCode();
    }



}

