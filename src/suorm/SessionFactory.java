package suorm;

import dbmanager.DBManager;

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

    public SessionFactory(DBManager dbm){
        this.dbm = dbm;
    }

    public Session openSession(){
        return new SessionImpl();
    }
}
