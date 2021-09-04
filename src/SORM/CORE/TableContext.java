package SORM.CORE;

import SORM.Utils.JavaFileUtils;
import SORM.Utils.StringUtils;
import SORM.bean.ColumnInfo;
import SORM.bean.TableInfo;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 负责管理数据库所有表结构和类结构的关系，并且根据表结构生成对应的类结构
 * @author 李浪波
 */
public class TableContext {
    /**
     * 表面为key,value为表结构的信息
     */
    private static Map<String, TableInfo> tableInfoMap = new HashMap<>();

    /**
     * 将po对象的Class对象和表对象关联起来，便于重用
     * key为该表结构对应的类的Class对象，value为表结构的信息
     */
    private static Map<Class,TableInfo> classTableInfoMap = new HashMap<>();

    /**
     * 初始化获得连接数据库中表的信息，包括字段信息，表名等
     * 同时设置相关表的主键等
     * 自动更新表的结构信息
     */
    static{
        try{
            //获得与数据库的连接
            Connection connection = DBManager.getConnectionWithDB();

            //连接测试语句已正常！
//            String sql = "SELECT* FROM employee WHERE id =1";
//            Statement statement = connection.createStatement();
//            ResultSet r=statement.executeQuery(sql);
//            while(r.next()){
//                System.out.println(r.getObject("age"));
//            }
            //获取数据库的元数据
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            //获得结果集
            ResultSet resultSet = databaseMetaData.getTables(null,"%","%",
                    new String[]{"TABLE"});
            //System.out.println(resultSet.next());//已经确定里面存在值判断resultSet中是否存在结果集
            while(resultSet.next()){
                //获取表名
                String tableName = (String)resultSet.getObject("TABLE_NAME");
                if(tableName.equalsIgnoreCase("user")){
                    continue;
                }
                TableInfo tableInfo = new TableInfo(tableName,new HashMap<String,ColumnInfo>(),new ArrayList<ColumnInfo>());
                //查询表中的所有字段
                ResultSet set = databaseMetaData.getColumns(null,"%",tableName,"%");
                while(set.next()){
                    ColumnInfo columnInfo = new ColumnInfo(set.getString("COLUMN_NAME"),
                            set.getString("TYPE_NAME"),0);//取出表中的一条字段信息
                    tableInfo.getColumnInfoMap().put(set.getString("COLUMN_NAME"),columnInfo);
                    tableInfo.getPriKeys().add(columnInfo);//添加联合主键
                }
                if(tableInfo.getPriKeys().size()>0){//如果联合主键不唯一，则取唯一主键方便使用,仅能删除唯一的主键
                    tableInfo.setOnlyPriKey(tableInfo.getPriKeys().get(0));
                }
                //将此表添加进入tableInfoMap中
                tableInfoMap.put(tableName,tableInfo);//将此表添加map之中
            }
            //每次自动更新生成的源代码
            updateJavaPOFile();
            //加载PO包下的所有类，便于重用，建立类对象和表结构中之间的关系
            loadPOTables();

        } catch (ClassNotFoundException | SQLException | IOException e) {
            System.out.println("DBManager类处理时发生异常！"+e);
            e.printStackTrace();
        }
    }

    private TableContext(){

    }

    public static Map<String,TableInfo>getTableInfoMap(){//获取tableInfoMap的信息
        return tableInfoMap;
    }
    public static Map<Class,TableInfo>getClassTableInfoMap(){
        return classTableInfoMap;
    }
    //修改字段时，更新其所在的表对应的java类源代码
    public static void updateJavaPOFile() throws IOException {
        MysqlTypeConvertor convertor = new MysqlTypeConvertor();
        for(TableInfo tableInfo:tableInfoMap.values()){
            JavaFileUtils.createJavaPOFile(tableInfo,convertor);
        }
    }
    //加载类和table_name之间的对应关系
    public static void loadPOTables() throws ClassNotFoundException {

        for(TableInfo tableInfo:tableInfoMap.values()){
            Class clazz = Class.forName(DBManager.getConfiguration().getPoPackage()+"."+ StringUtils
                    .firstChar2UpperCase(tableInfo.getName()));
            classTableInfoMap.put(clazz,tableInfo);
        }
    }
}
