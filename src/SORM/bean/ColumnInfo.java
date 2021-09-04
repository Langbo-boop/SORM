package SORM.bean;

/**
 * 封装了表中一个字段的信息
 * @author 李浪波
 */
public class ColumnInfo {
    private String name;//字段名称
    private String dataType;//字段的数据类型
    /**
     * 键类型，根据提供的键生成对应的数据类型
     * 0：普通键
     * 1：主键
     * 2：外键
     */
    private int keyType;

    public ColumnInfo(){

    }

    public ColumnInfo(String name, String dataType, int keyType) {
        this.name = name;
        this.dataType = dataType;
        this.keyType = keyType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getKeyType() {
        return keyType;
    }

    public void setKeyType(int keyType) {
        this.keyType = keyType;
    }

    @Override
    public String toString() {
        return "ColumnInfo{" +
                "name='" + name + '\'' +
                ", dataType='" + dataType + '\'' +
                ", keyType=" + keyType +
                '}';
    }
}
