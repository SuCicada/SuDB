package suorm;

import dbmanager.DBExecutor;

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
    DBExecutor dbExecutor = null;

    int insert(Object element);

    int delete();

    List<Map<String,Object>> select();

    int update();

    Object get(Class Entity, int Id);
}

