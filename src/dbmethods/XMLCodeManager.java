package dbmethods;

import dbmanager.DBException;
import org.apache.logging.log4j.core.appender.FileManager;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: peng
 * Date: 9/29/19
 * Time: 9:19 PM
 * To change this template use File | Settings | File Templates.
 * Description:
 */
public class XMLCodeManager {
    private Document cnfDocument;
    private Map<String, String> db2java;
    private String packageName;

    public XMLCodeManager(){
        cnfDocument = DocumentHelper.createDocument()
                .addElement("SuDB-configuration")
                .addElement("SuDB-mapping")
                .getDocument();
    }

    public XMLCodeManager(Document document){
        cnfDocument = document;
    }

    public XMLCodeManager(String path) throws DocumentException {
        SAXReader reader = new SAXReader();
        reader.setEncoding("utf-8");
        cnfDocument = reader.read(new File(path));
    }

    public void setDB2JAVA(Map<String, String> db2java) throws DBException {
        if(db2java == null){
            throw new DBException("No DB2JAVA.");
        }
        this.db2java = db2java;
    }

    public void setpackageName(String path){
        this.packageName = path;
    }

    private Element getMappingElement(){
        return cnfDocument.getRootElement()
                .element("SuDB-mapping");
    }

    public Element createClassElement(String table, List<Map<String, Object>> columns) throws DBException {
        Element mapping = getMappingElement();
        mapping.addAttribute("package",packageName);

        Element tableClass = mapping.addElement("class")
                .addAttribute("name","T_" + table)
                .addAttribute("table",table);
        boolean hasIdColumn = false;
        for(Map<String,Object> column: columns) {
            String column_key = (String)column.get("COLUMN_KEY");
            String columnName = (String) column.get("COLUMN_NAME");
            String dataType = (String) column.get("DATA_TYPE");
            if(column_key == null){
                column_key = (String)column.get("column_key");
            }
            if(columnName==null) {
                columnName = (String)column.get("column_name");
            }
            if(dataType==null) {
                dataType = (String)column.get("data_type");
            }
//            dataType = dataType.toUpperCase();
//            String type = db2java.get(dataType).replaceAll(".*\\.","");
            Element newElement;
            if(column_key !=null && column_key.equals("PRI")) {
                newElement = DocumentHelper.createElement("id");
                tableClass.elements().add(0,newElement);
                hasIdColumn = true;
            }else{
                newElement = tableClass.addElement("property");
            }
            newElement.addAttribute("name",columnName)
                    .addAttribute("type",dataType)
                    .addAttribute("column",columnName);
        }
        if(!hasIdColumn){
            throw new DBException("Table "+table+" doesn't have Primary Key!");
        }
        return tableClass;
    }

    /**
     * [!] 在xml中查找某一个表的class的Element结点,目前采用遍历找, 试采用map等更高效方法
     * @param className
     * @return
     */
    public ClassElement getClassElement(String className){
        for(Element e: getMappingElement().elements()){
            if(e.attributeValue("name").equals(className)){
                return new ClassElement(e);
            }
        }
        return null;
    }

    public void saveXmlFile(String path) throws IOException {
        FileWriter out = new FileWriter(path);
        OutputFormat formater=OutputFormat.createPrettyPrint();
        XMLWriter xmlWriter = new XMLWriter(out,formater);
        xmlWriter.write(cnfDocument);
        out.close();
        System.out.println("XML: "+path+" has saved successful.");
    }

//    public void saveXmlFile() throws IOException {
//        saveXmlFile("entities.sudb.xml");
//    }



}
