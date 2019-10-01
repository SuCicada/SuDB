package suorm;

import dbmanager.DBException;
import dbmanager.DBExecutor;
import dbmanager.DBManager;
import dbmethods.XMLCodeManager;
import org.dom4j.Element;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: peng
 * Date: 9/21/19
 * Time: 5:05 PM
 * To change this template use File | Settings | File Templates.
 * Description:
 */
public class SessionImpl implements Session{
    XMLCodeManager xmlCodeManager;
    DBExecutor dbExecutor = null;

    public SessionImpl(XMLCodeManager xmlCodeManager){
        this.xmlCodeManager = xmlCodeManager;
        dbExecutor = DBManager.getInstanc().getDBExecutor();
    }

    @Override
    public int save(Object entity) throws NoSuchMethodException, DBException, InvocationTargetException, IllegalAccessException {
        Class Entity = entity.getClass();
        Element element = xmlCodeManager.getClassElement(Entity.getName());
        Method method[] = Entity.getDeclaredMethods();
        Field field[] = Entity.getDeclaredFields();
        StringBuilder sql = new StringBuilder("insert into ");
        StringBuilder columns = new StringBuilder("(");
        StringBuilder values = new StringBuilder("(");
        List<String> valueList = new ArrayList(element.elements().size()-1);
        String table = element.attributeValue("table");
        for(Element e: element.elements()){
            if(!e.getName().equals("id")){
                columns.append(e.attributeValue("column"))
                        .append(",");

                String varName = e.attributeValue("name");
                String methodName = "get"+varName.substring(0,1).toUpperCase()+varName.substring(1);
                values.append("?,");
                valueList.add((String) Entity.getDeclaredMethod(methodName).invoke(entity));
            }
        }
        columns.deleteCharAt(columns.length()-1).append(") ");
        values.deleteCharAt(values.length()-1).append(");");
        sql.append(table).append(columns).append("values").append(values);
        dbExecutor.update(sql.toString(),valueList);
        Object idObj = dbExecutor.query("select last_insert_id() as id;").get(0).get("id");
        return Integer.parseInt(idObj.toString());
    }

    @Override
    public int delete(Object entity) {
        return 0;
    }

    @Override
    public List<Object> query(String str) {
        return null;
    }

    @Override
    public int update(Object entity) {
        return 0;
    }

    @Override
    public Object get(Class Entity, int Id) {
        return null;
    }

}
