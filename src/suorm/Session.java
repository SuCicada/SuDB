package suorm;

import com.mysql.cj.x.protobuf.MysqlxCursor;
import dbmanager.DBException;
import dbmanager.DBExecutor;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: peng
 * Date: 9/15/19
 * Time: 9:57 PM
 * To change this template use File | Settings | File Templates.
 * Description:
 */
public interface Session {

    int save(Object entity) throws NoSuchMethodException, DBException, InvocationTargetException, IllegalAccessException;

    int delete(Object entity) throws NoSuchMethodException, DBException, InvocationTargetException, IllegalAccessException;

//    Object query(Class Entity, int Id);

    List<Object> query(String str) throws DBException;

    int update(Object entity) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, DBException;

    Object get(Class Entity, int id) throws DBException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException;
}

