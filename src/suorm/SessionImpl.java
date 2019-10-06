package suorm;

import dbmanager.DBException;
import dbmanager.DBExecutor;
import dbmanager.DBManager;
import dbmethods.ClassElement;
import dbmethods.DB2JAVA;
import dbmethods.DB2JAVAFactory;
import dbmethods.XMLCodeManager;
import org.dom4j.Element;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
    private XMLCodeManager xmlCodeManager;
    private DBExecutor dbExecutor = null;


    public SessionImpl(XMLCodeManager xmlCodeManager){
        this.xmlCodeManager = xmlCodeManager;
        dbExecutor = DBManager.getInstanc().getDBExecutor();
    }

    @Override
    public int save(Object entity) throws NoSuchMethodException, DBException, InvocationTargetException, IllegalAccessException {
        Class Entity = entity.getClass();
        ClassElement element = xmlCodeManager.getClassElement(Entity.getName());
//        Method method[] = Entity.getDeclaredMethods();
//        Field field[] = Entity.getDeclaredFields();
        StringBuilder sql = new StringBuilder("insert into ");
        StringBuilder columns = new StringBuilder("(");
        StringBuilder values = new StringBuilder("(");
        List<String> valueList = new ArrayList(element.elements().size()-1);
//        String table = element.attributeValue("table");
        String table = element.getTableName();
        /* 拼接要插入的列的值 */
        for(Element e: element.elements()){
            /* 跳过*/
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
    public int delete(Object entity) throws NoSuchMethodException, DBException, InvocationTargetException, IllegalAccessException,NullPointerException {
        if(entity == null){
            throw new DBException("Entity Object is NUll.");
        }
        Class Entity = entity.getClass();
        ClassElement element = xmlCodeManager.getClassElement(Entity.getName());
        StringBuilder sql = new StringBuilder("delete from ");
        String table = element.attributeValue("table");
        sql.append(table).append(" where ");

//        String idName = element.element("id").attributeValue("name");
//        String idColumn = element.element("id").attributeValue("column");
        String idName = element.getIdName();
        String idColumn = element.getIdColumn();
        String method = "get" + idName.substring(0,1).toUpperCase()+idName.substring(1);
        int id = Integer.parseInt(Entity.getDeclaredMethod(method).invoke(entity).toString());
        sql.append(idColumn+" = "+id);
        List<String> valueList = new ArrayList<>(1);
        dbExecutor.update(sql.toString(),valueList);
        return 0;
    }

    @Override
    public List<Object> query(String str) throws DBException {
        StringBuilder sql = new StringBuilder("select * ");
        sql.append(str);
        List<Map<String,Object>> resSet= dbExecutor.query(sql.toString());
        List<Object> res = new ArrayList(resSet.size());
        for(Map<String,Object> map: resSet){

        }
        return null;
    }

    @Override
    public int update(Object entity) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, DBException {
        Class Entity = entity.getClass();
        ClassElement element = xmlCodeManager.getClassElement(Entity.getName());
        StringBuilder sql = new StringBuilder("update ");
        String table = element.getTableName();
        List<String> valueList = new ArrayList(element.elements().size()-1);
        sql.append(table).append(" set ");
        String idValue = null;
        for(Element e: element.elements()) {
            String varName = e.attributeValue("name");
            String column = e.attributeValue("column");
            String methodName = "get"+varName.substring(0,1).toUpperCase()+varName.substring(1);
            String value = String.valueOf(Entity.getDeclaredMethod(methodName).invoke(entity));
            if(!e.getName().equals("id")) {
                sql.append(column).append("=?").append(",");
                valueList.add(value);
            }else{
                idValue = value;
            }
        }
        sql.deleteCharAt(sql.length()-1)
                .append(" where ")
                .append(element.getIdColumn())
                .append("=?;");
        valueList.add(idValue);

        dbExecutor.update(sql.toString(),valueList);
        return 0;
    }

    @Override
    public Object get(Class Entity, int id) throws DBException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException {
        DB2JAVA db2JAVA = DB2JAVAFactory.getDB2JAVA();
        ClassElement element = xmlCodeManager.getClassElement(Entity.getName());
        StringBuilder sql = new StringBuilder("select * from ");
        String table = element.getTableName();
        String idColumn = element.getIdColumn();
        sql.append(table).append(" where ").append(idColumn).append(" = ").append(id);
        List<Map<String,Object>> resSet= dbExecutor.query(sql.toString());

        if(resSet.size()>1){
            throw new DBException("Too much query result.");
        }
        if(resSet.size()<1){
            return null;
        }
        Object entity = Entity.newInstance();
        Map<String,Object> map = resSet.get(0);
//        Method[] ms = Entity.getDeclaredMethods();
        for(Element e: element.elements()){
            String column = e.attributeValue("column");
            String name = e.attributeValue("name");
            Object value = map.get(column);
            String type = e.attributeValue("type");

            String javaType = db2JAVA.get(type);
            String method = "set"+name.substring(0,1).toUpperCase()+name.substring(1);

            Method setter = Entity.getDeclaredMethod(method,Class.forName(javaType));
            Class parClass = Class.forName(javaType);
            if(setter != null){
                setter.invoke(entity,parClass.getConstructor(String.class).newInstance(value.toString()));
            }else{
                throw new DBException("No Method ["+method+"] in Class ["+Entity.getName()+"]");
            }
        }
        return entity;
    }
}
