package SORM.CORE;

/**
 * 负责java数据类型和mysql数据类型的互相转化
 * @author 李浪波
 */
public interface TypeConvertor {
    /**
     * 提供数据库类型到java类型的转换
     * @param column 传入的数据库字段类型
     * @return 返回转换后的java数据类型
     */
    public String databaseType2JavaType(String column);

    /**
     * 将传入的java数据类型转换为对应的数据库类型
     * @param type 传入的java数据类型
     * @return java数据库转换为数据库类型的数据
     */
    public String javaType2DatabaseType(String type);
}
