package dbeasy;

import dbmanager.DBException;
import dbmanager.DBManager;

/**
 * Created with IntelliJ IDEA.
 * User: peng
 * Date: 9/21/19
 * Time: 5:53 PM
 * To change this template use File | Settings | File Templates.
 * Description:
 */
public class EasyQueryFractory {
    public EasyQuery getEasyQuery() throws DBException {
        return DBManager.getInstanc().getDBExecutor().getEasyQuery();
    }

}
