import dbmanager.DBException;
import dbmanager.DBManager;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import suorm.Session;
import suorm.SessionFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: peng
 * Date: 9/22/19
 * Time: 4:01 PM
 * To change this template use File | Settings | File Templates.
 * Description:
 */

public class Main {

    public static void main(String[] args) throws NoSuchMethodException, DBException, IllegalAccessException, InvocationTargetException, DocumentException, InstantiationException, ClassNotFoundException {
        DBManager dbm = DBManager.getInstanc();
        SessionFactory sf = new SessionFactory(dbm);
        Session session = sf.openSession();

        T_user user = new T_user();
//        user.setUsername("123");
//        user.setPassword(new Date().toString());

//        int a = session.save(user);
//        System.out.println(a);
        user = (T_user) session.get(T_user.class,130);
        user.setUsername(String.valueOf(Math.random()));
        session.update(user);
        System.out.println();
    }
}
