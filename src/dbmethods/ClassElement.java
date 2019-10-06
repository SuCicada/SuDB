package dbmethods;
import org.dom4j.Element;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: peng
 * Date: 10/6/19
 * Time: 1:59 PM
 * To change this template use File | Settings | File Templates.
 * Description:
 */
public class ClassElement  {
    Element element;
    public ClassElement(Element element){
        this.element = element;
    }
    public Element getElement(){
        return element;
    }

    public String getTableName(){
        return element.attributeValue("table");
    }
    public String getIdName(){
        return element.element("id").attributeValue("name");
    }

    public String getIdColumn(){
        return element.element("id").attributeValue("column");
    }

//    public String getNameFromColumn(){
//
//    }

    /* 兼容Element方法 */

    public String attributeValue(String n){
        return element.attributeValue(n);
    }

    public List<Element> elements(){
        return element.elements();
    }
}
