import dbmanager.DBException;
import dbmanager.DBManager;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import suorm.Session;
import suorm.SessionFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * Created with IntelliJ IDEA.
 * User: peng
 * Date: 9/22/19
 * Time: 4:01 PM
 * To change this template use File | Settings | File Templates.
 * Description:
 */

public class Main {

    public static void main(String[] args) throws NoSuchMethodException, DBException, IllegalAccessException, InvocationTargetException, DocumentException {
        DBManager dbm = DBManager.getInstanc();
        SessionFactory sf = new SessionFactory(dbm);
        Session session = sf.openSession();

        T_user user = new T_user();
        user.setUsername("123");
        user.setPassword("1829239");

        int a = session.save(user);
        System.out.println(a);
    }
}
