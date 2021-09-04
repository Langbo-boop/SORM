package SORM.bean;

import java.util.List;
import java.util.Map;

/**
 * 存储表的信息
 * @author 李浪波
 */
public class TableInfo {

    private String name;//表名
    /**
     * 所有字段的信息
     */
    private Map<String,ColumnInfo> columnInfoMap;

    /**
     * 唯一主键，目前仅能处理表中有且仅有一个主键的情形
     */
    private ColumnInfo onlyPriKey;

    /**
     * 联合主键，使用list存储联合主键，供以后使用
     */
    private List<ColumnInfo> priKeys;
    public TableInfo(){

    }

    public TableInfo(String name, Map<String, ColumnInfo> columnInfoMap, ColumnInfo onlyPriKey) {
        this.name = name;
        this.columnInfoMap = columnInfoMap;
        this.onlyPriKey = onlyPriKey;
    }

    public TableInfo(String name, Map<String, ColumnInfo> columnInfoMap, List<ColumnInfo> priKeys) {
        this.name = name;
        this.columnInfoMap = columnInfoMap;
        this.priKeys = priKeys;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, ColumnInfo> getColumnInfoMap() {
        return columnInfoMap;
    }

    public void setColumnInfoMap(Map<String, ColumnInfo> columnInfoMap) {
        this.columnInfoMap = columnInfoMap;
    }

    public ColumnInfo getOnlyPriKey() {
        return onlyPriKey;
    }

    public void setOnlyPriKey(ColumnInfo onlyPriKey) {
        this.onlyPriKey = onlyPriKey;
    }

    public List<ColumnInfo> getPriKeys() {
        return priKeys;
    }

    public void setPriKeys(List<ColumnInfo> priKeys) {
        this.priKeys = priKeys;
    }

    @Override
    public String toString() {
        return "TableInfo{" +
                "name='" + name + '\'' +
                ", columnInfoMap=" + columnInfoMap +
                ", onlyPriKey=" + onlyPriKey +
                ", priKeys=" + priKeys +
                '}';
    }
}
