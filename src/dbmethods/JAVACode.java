package dbmethods;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: peng
 * Date: 9/30/19
 * Time: 6:22 PM
 * To change this template use File | Settings | File Templates.
 * Description:
 */
public class JAVACode {
    StringBuffer javaSchema = new StringBuffer();
    StringBuffer javaSchemaConstructorParameter = new StringBuffer();
    StringBuffer javaSchemaConstructorBody = new StringBuffer();
    StringBuffer classMembers = new StringBuffer();
    StringBuffer classGetter = new StringBuffer();
    StringBuffer classSetter = new StringBuffer();
    String packageName;
    Map<String,String> db2java;

    public JAVACode(String packageName, Map<String, String> db2java) {
        this.packageName = packageName;
        this.db2java = db2java;
    }

    public String buildCode(String table, List<Map<String,Object>> columns) {
        Set<String> jarSet =  new HashSet<>();
        javaSchema.append("package " + packageName+";\n\n");
        for(Map<String, Object> column: columns){
            String columnName = (String)column.get("column_name");
            String dataType = (String)column.get("data_type");
            String columnKey = (String)column.get("column_key");
            String columnComment = (String)column.get("column_comment");

            if(columnName==null) {
                columnName = (String)column.get("COLUMN_NAME");
            }
            if(dataType==null) {
                dataType = (String)column.get("DATA_TYPE");
            }
            if(columnKey==null) {
                columnKey = (String)column.get("COLUMN_KEY");
            }
            if(columnComment==null) {
                columnComment = (String)column.get("COLUMN_COMMENT");
            }

            String jar = db2java.get(dataType.toUpperCase());
            if(!jarSet.contains(jar)){
                jarSet.add(jar);
                javaSchema.append(String.format("import %s;\n",jar));
            }
            String type = jar.replaceAll(".*\\.","");
            /* 实体类变量命名 */
            String varName = columnName.substring(0,1).toUpperCase()+columnName.substring(1).toLowerCase();
            classMembers.append(String.format("\tprivate %s %s;\n", type, columnName));
            classSetter.append(String.format("\tpublic void set%s(%s %s) {\n" +
                    "\t\tthis.%s = %s;\n\t}\n\n",varName, type, columnName, columnName, columnName));
            classGetter.append(String.format("\tpublic %s get%s() {\n" +
                    "\t\treturn %s;\n\t}\n\n", type, varName, columnName));

            if(javaSchemaConstructorParameter.length()>0){
                javaSchemaConstructorParameter.append(", ");
            }
            javaSchemaConstructorParameter.append(type + " " + columnName);
            javaSchemaConstructorBody.append(String .format("\t\tthis.%s = %s;\n",columnName, columnName));
        }

        javaSchema.append("\n");
        javaSchema.append(String.format("public class %s {\n",table));
        javaSchema.append(classMembers);
        javaSchema.append("\n");
        javaSchema.append(String.format("\tpublic %s(){}\n\n",table));
        javaSchema.append(String.format("\tpublic %s(%s) {\n" +
                "%s\t}\n\n",table, javaSchemaConstructorParameter.toString(), javaSchemaConstructorBody.toString()));
        javaSchema.append(classSetter);
        javaSchema.append(classGetter);
        /* 消除最后一个空行*/
        javaSchema.delete(javaSchema.length()-1,javaSchema.length());
        javaSchema.append(String.format("}\n"));
        return javaSchema.toString();
    }

}
