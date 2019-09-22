package suorm;

import java.util.List;
import java.util.Map;

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
    public int insert(Object element) {
        return 0;
    }

    @Override
    public int delete() {
        return 0;
    }

    @Override
    public List<Map<String, Object>> select() {
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
