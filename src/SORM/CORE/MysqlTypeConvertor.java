package SORM.CORE;

import java.lang.reflect.Type;

/**
 * 将mysql数据类型转换成java数据类型
 */
public class MysqlTypeConvertor implements TypeConvertor {

    /**
     * 将sql数据类型转化为java数据类型
     * @param column 传入的数据库字段类型
     * @return 返回转换后的java数据类型
     */
    @Override
    public String databaseType2JavaType(String column) {

        if("varchar".equalsIgnoreCase(column) || "char".equalsIgnoreCase(column)){
            return "String";
        }else if("int".equalsIgnoreCase(column) || "tinyint".equalsIgnoreCase(column)
                || "smallint".equalsIgnoreCase(column) || "integer".equalsIgnoreCase(column)
                ||"int unsigned".equalsIgnoreCase(column)){
            return "Integer";
        }else if("bigint".equalsIgnoreCase(column)){
            return "Long";
        }else if("double".equalsIgnoreCase(column) || "float".equalsIgnoreCase(column)
                || "decimal".equalsIgnoreCase(column)){
            return "Double";
        }else if("clob".equalsIgnoreCase(column)){
            return "java.sql.Clob";
        }else if("blob".equalsIgnoreCase(column)){
            return "java.sql.Blob";
        }else if("date".equalsIgnoreCase(column)){
            return "java.sql.Date";
        }else if("time".equalsIgnoreCase(column)){
            return "java.sql.Time";
        }else if("timestamp".equalsIgnoreCase(column)){
            return "java.sql.Timestamp";
        }else{
            return null;
        }
    }

    @Override
    public String javaType2DatabaseType(String type) {
        return null;
    }
}
