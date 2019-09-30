package dbmethods;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
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
    private Map<String, String> db2java;
    private String packageName;

    public void setDb2java(Map db2java){
        this.db2java = db2java;
    }

    public void createFromXml(String fileName) throws DocumentException {
        JAVACode javaCode = new JAVACode(packageName,db2java);

        SAXReader reader = new SAXReader();
        reader.setEncoding("utf-8");
        Document document = reader.read(new File(fileName));
        Element mapping = document.getRootElement().element("SuDB-mapping");

        for(Element classNode: mapping.elements("class")){
            String table = classNode.attribute("name").getName();
            List<Map<String,Object>> columns = element2List(classNode);

            javaCode.buildCode(table,columns);
        }


    }
    private List element2List(Element classNode){
        List<Map<String,Object>> columns = new ArrayList();
        Map<String,Object> map = new HashMap();
        for(Element row: classNode.elements()){
            map.put("column_name",row.attribute("name"));
            map.put("data_type",row.attribute("type"));
            map.put("column_key",row.getName().equals("id")? "PRI":"");
            map.put("column_comment",row.attribute("column_comment"));
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


