package dbmethods;

import dbmanager.DBException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.*;
/**
 * Created with IntelliJ IDEA.
 * User: peng
 * Date: 9/29/19
 * Time: 11:43 AM
 * To change this template use File | Settings | File Templates.
 * Description:
 */
public class JAVACodeManager {
    /** 数据类型对照表*/
//    private Map<String, String> db2java;
    private DB2JAVA db2java;
    private String packageName;
    private String packagePath;

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }

    public void setDb2java(DB2JAVA db2java){
        this.db2java = db2java;
    }

    private void checkDb2java() throws DBException {
        if(db2java == null || db2java.isEmpty()){
            throw new DBException("No DB2Java.");
        }
    }

    public void createFromXml(String fileName) throws DocumentException, DBException, IOException {
        checkDb2java();
        JAVACode javaCode = new JAVACode(packageName,db2java);

        SAXReader reader = new SAXReader();
        reader.setEncoding("utf-8");
        Document document = reader.read(new File(fileName));
        Element mapping = document.getRootElement().element("SuDB-mapping");

        for(Element classNode: mapping.elements("class")){
            String table = classNode.attributeValue("name");
            List<Map<String,Object>> columns = element2List(classNode);
            javaCode.buildCode(table,columns);
//            System.out.println(a);
            String className = table.substring(0,1).toUpperCase()+table.substring(1);
            String classFile = packagePath+"/"+className+".java";
            javaCode.saveJavaFile(classFile);
        }


    }



    private List element2List(Element classNode){
        List<Map<String,Object>> columns = new ArrayList();
        for(Element row: classNode.elements()){
            Map<String,Object> map = new HashMap();
            map.put("column_name",row.attributeValue("name"));
            map.put("data_type",row.attributeValue("type"));
            map.put("column_key",row.getName().equals("id")? "PRI":"");
            map.put("column_comment",row.attributeValue("column_comment"));
            columns.add(map);
        }
        return columns;
    }

    public void createFromDB(){

    }
//    public void init(String table, List<Map<String, Object>> columns, String DBType, Properties db2java, String packagePath, String packageName) {
//        this.table = table;
//        this.columns = columns;
//        this.DBType = DBType;
//        this.db2java = db2java;
//        this.packagePath = packagePath;
//        this.packageName = packageName;
//    }

    public void init(CodeCreator codeCreator){

    }

}


