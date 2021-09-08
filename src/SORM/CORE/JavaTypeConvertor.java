package SORM.CORE;

/**
 * 将java数据类型转换为数据库类型,测试数据基本通过
 */
public class JavaTypeConvertor implements TypeConvertor{
    @Override
    public String databaseType2JavaType(String column) {
        return null;
    }

    /**
     * 实现将java数据类型转换为数据库类型的操作
     * @param type 传入的java数据类型
     * @return 传入的java引起的数据类型的转换
     */
    @Override
    public String javaType2DatabaseType(String type) {
        if("char".equalsIgnoreCase(type)){
            return "char";
        }else if("int".equalsIgnoreCase(type)){
            return "int";
        }else if("byte".equalsIgnoreCase(type)){
            return "tinyint";
        }else if("short".equalsIgnoreCase(type)){
            return "smallint";
        }else if("Integer".equalsIgnoreCase(type)){
            return "integer";
        }else if("unsigned int".equalsIgnoreCase(type)){
            return "int unsigned";
        } else if("double".equalsIgnoreCase(type) || "float".equalsIgnoreCase(type)
                || "decimal".equalsIgnoreCase(type)){
            return "double";
        }else if("String".equalsIgnoreCase(type)){//将所有的字符串对象类型都转换为varchar(200)类型
            return "varchar(200)";
        } else if("java.sql.Clob".equalsIgnoreCase(type)){
            return "clob";
        }else if("java.sql.Blob".equalsIgnoreCase(type)){
            return "blob";
        }else if("java.sql.Date".equalsIgnoreCase(type)){
            return "date";
        }else if("java.sql.Time".equalsIgnoreCase(type)){
            return "time";
        }else if("java.sql.Timestamp".equalsIgnoreCase(type)){
            return "timestamp";
        }else{
            return null;
        }
    }
}
