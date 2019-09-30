package suorm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: peng
 * Date: 9/21/19
 * Time: 5:05 PM
 * To change this template use File | Settings | File Templates.
 * Description:
 */
public class SessionImpl implements Session{
    @Override
    public int save(Class Entity, Object element) {
        Method method[] = Entity.getDeclaredMethods();
        Field field[] = Entity.getDeclaredFields();
        StringBuilder sql;

        return 0;
    }

    @Override
    public int delete() {
        return 0;
    }

    @Override
    public Object select() {
        return null;
    }

    @Override
    public int update() {
        return 0;
    }

    @Override
    public Object get(Class Entity, int Id) {
        return null;
    }
}
