package suorm;

import dbmanager.DBManager;
import dbmethods.XMLCode;
import dbmethods.XMLCodeManager;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: peng
 * Date: 9/22/19
 * Time: 5:08 PM
 * To change this template use File | Settings | File Templates.
 * Description:
 */
public class SessionFactory {
    DBManager dbm;
    XMLCodeManager xmlCodeManager;

    public SessionFactory(DBManager dbm){
        this.dbm = dbm;
    }

    private void loadXML() throws DocumentException {
        String xml = dbm.getEntityMapping();
        xmlCodeManager = new XMLCodeManager(xml);
    }

    public Session openSession() throws DocumentException {
        loadXML();
        return new SessionImpl(xmlCodeManager);
    }
}
