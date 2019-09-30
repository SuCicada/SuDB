package dbmethods;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * Created with IntelliJ IDEA.
 * User: peng
 * Date: 9/29/19
 * Time: 11:45 AM
 * To change this template use File | Settings | File Templates.
 * Description:
 */
public class XMLCode implements Code{

    public void init(){

    }




    @Override
    public Document buildCode() {
        return buildCode("entities.sudb.xml");
    }

    public Document buildCode(String xmlPath) {


        return null;
    }

    @Override
    public String getCode() {
        return null;
    }
}
