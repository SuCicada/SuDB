import dbmanager.DBManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import suorm.Session;
import suorm.SessionFactory;

/**
 * Created with IntelliJ IDEA.
 * User: peng
 * Date: 9/22/19
 * Time: 4:01 PM
 * To change this template use File | Settings | File Templates.
 * Description:
 */

public class Main {

    public static void main(String[] args) {
        DBManager dbm = DBManager.getInstanc();
        SessionFactory sf = new SessionFactory(dbm);
        Session session = sf.openSession();
//        System.out.println("sdfs");
    }
}
