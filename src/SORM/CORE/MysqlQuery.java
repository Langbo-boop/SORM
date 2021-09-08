package SORM.CORE;

import SORM.Utils.JDBCUtils;
import SORM.Utils.ReflectUtils;
import SORM.Utils.StringUtils;
import SORM.bean.ColumnInfo;
import SORM.bean.TableInfo;
import po.Employee;
import po.Event;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static SORM.Utils.ReflectUtils.invokeGet;

/**
 * 通过java实现对mysql数据库的增删改查等操作,以下增删改查操作均已全部实现.
 * connection 使用连接池进行了优化
 */
public class MysqlQuery implements Query{

    @Override
    public void  executeDML(String sql, Object[] params) throws ClassNotFoundException, SQLException {
        //建立与数据库的连接
        Connection connection = DBManager.getConnectionFromPool();
        PreparedStatement preparedStatement = null;
        int count=0;
        try {

            preparedStatement = connection.prepareStatement(sql);
            //给sqL语句传入参数
            if(params.length>0){
                JDBCUtils.handleParams(preparedStatement,params);
            }
            count = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("出错啦！");
            e.printStackTrace();
        }finally{
            DBManager.close(preparedStatement);
            DBManager.close(connection);
        }
        System.out.println("影响了"+count+"行记录.");
        System.out.println(sql);
    }

    @Override
    public void executeDML(String sql) throws ClassNotFoundException, SQLException {
        Connection connection = DBManager.getConnectionFromPool();
        Statement statement = connection.createStatement();
        statement.execute(sql);
        DBManager.close(connection);
        DBManager.close(statement);

    }

    @Override
    public void insert(Object object) throws ClassNotFoundException, SQLException {
        //obj-->数据库中，insert into table_name(name,salary,sex)values(?,?,?)
        //1.拿到数据库表名，拿到不为空的属性信息，这些都可以用反射来完成
        //获得数据库表名
        Class clazz = object.getClass();
        //获取表信息
        TableInfo tableInfo = TableContext.getClassTableInfoMap().get(clazz);
        //获得object的属性列表
        Field[] fields = clazz.getDeclaredFields();
        //拼接sql语句
        StringBuilder sql =new StringBuilder("INSERT INTO "+tableInfo.getName()+"(");
        //存储参数的值
        List<Object>params = new ArrayList<>();
        //声明存储参数不为空的所有属性的个数
        int countNotNull = 0;
        for(Field f:fields){
            String name = f.getName();//获得属性名
            Object value = ReflectUtils.invokeGet(name,object);
            if(value!=null){
                countNotNull++;
                sql.append(name+",");
                params.add(value);//将此参数添加进入params表中，然后再将其中对应的值通过反射取出来
            }
        }
        sql.setCharAt(sql.length()-1,')');//将以上字符的最后一个添加形成小括号
        sql.append(" VALUES (");
        for(int i=0;i<countNotNull;i++){
            sql.append("?,");
        }
        sql.setCharAt(sql.length()-1,')');

        //执行sql语句
        executeDML(sql.toString(),params.toArray());

    }

    @Override
    public void delete(Class clazz, Object id) throws ClassNotFoundException, SQLException {
        //删除某一对象，通过class找到对应的类并且进行删除
        //delete from table_name where id=1
        //根据clazz获取TableInfo
        TableInfo tableInfo =  TableContext.getClassTableInfoMap().get(clazz);
        //拼接sql语句
        String sql = "DELETE FROM "+tableInfo.getName()+" where "+tableInfo.getOnlyPriKey().getName()+"=?";//？问号是占位符
        //执行sql语句
        System.out.println(sql);
        executeDML(sql,new Object[]{id});
    }

    //涉及反射操作,比较复杂
    @Override
    public void delete(Object object) {

        Class clazz = object.getClass();
        TableInfo tableInfo = TableContext.getClassTableInfoMap().get(clazz);
        ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();
        try{
            //通过反射获取对象中获取主键字段的方法
           Object id = invokeGet(onlyPriKey.getName(),object);
            delete(clazz,id);//调用方法执行删除操作
        }catch (Exception e){
            System.out.println("反射出现错误啦！位置在MysqlQuery的delete重写方法中"+e);
        }

    }

    /**
     *
     * @param object 待删除的对象
     * @param fieldName 待删除对象的字段名
     * @param value 字段值
     * 核心idea，我们可以使用value和fieldName来拼接符合条件的删除执行的sql语句
     * 对应的sql delete语句为delete from table_name where fieldName=value
     *
     */

    @Override
    public void delete(Object object, String fieldName, String value) throws ClassNotFoundException, SQLException {
        //获得对象的clazz信息
        Class clazz = object.getClass();
        //根据clazz信息获取对象相对应的表结构信息
        TableInfo tableInfo = TableContext.getClassTableInfoMap().get(clazz);
        String sql = "DELETE FROM "+tableInfo.getName()+" WHERE "+fieldName+"="+value;
        System.out.println(sql);
        executeDML(sql);
    }

    /**
     * 对一张表的所有数据进行同时更新
     * @param object 更新的对象
     * @param fieldNames 更新的属性列表
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    public int update(Object object, String[] fieldNames) throws ClassNotFoundException, SQLException {
        //修改语句
        //格式为：update table_name set fieldName=? AND fieldName=?，批量产生更新语句
        //？问号值可以根据反射获取，即指定多个属性的值自动更新，而不是添加多重语句条件

        //获得object对象的类对象
        Class clazz = object.getClass();
        //获得object对象对应类的对应表的信息
        TableInfo tableInfo = TableContext.getClassTableInfoMap().get(clazz);

        //拼接sql语句
        StringBuilder sql = new StringBuilder("UPDATE "+tableInfo.getName()+" set ");
        //声明存储属性值的列表
        List<Object>params = new ArrayList<>();
        if(fieldNames.length>0){
            //最后一个不能加AND
            for(int i=0;i<fieldNames.length-1;i++){
                //拼接sql语句
                sql.append(fieldNames[i]+"=? AND ");
                //获得属性对应的值
                Object value = ReflectUtils.invokeGet(fieldNames[i],object);
                params.add(value);
            }
            //添加最后一个语句的值
            sql.append(fieldNames[fieldNames.length-1]+"=?");
            Object value = ReflectUtils.invokeGet(fieldNames[fieldNames.length-1],object);
            params.add(value);
        }
        System.out.println(sql.toString());
        executeDML(sql.toString(),params.toArray());
        return 0;
    }

    /**
     * 已通过测试
     *对符合字段条件的指定对象的属性值进行更新操作
     * @param object 待更新的对象
     * @param fieldNames 待更新的对象的属性名数组
     * @param fieldsConditon 待更新对象需要满足的属性条件
     * @return 更新影响的记录数
     * 示例：UPDATE table_name SET fieldName=? WHERE fieldName=?;
     */
    @Override
    public int update(Object object, String[] fieldNames, String[] fieldsConditon) throws ClassNotFoundException, SQLException {
        //根据object获得数据库表信息
        Class clazz = object.getClass();
        TableInfo tableInfo = TableContext.getClassTableInfoMap().get(clazz);

        //准备拼接字符串
        StringBuilder sql = new StringBuilder("UPDATE "+tableInfo.getName()+" "+"SET ");
        //存储参数的数组
        List<Object>params = new ArrayList<>();
        //拼接待更新字段的信息
        if(fieldNames.length>0){
            for(int i=0;i<fieldNames.length-1;i++){
                sql.append(fieldNames[i]+"=? AND ");
                //获得该属性对应的值
                Object value = ReflectUtils.invokeGet(fieldNames[i],object);
                params.add(value);
            }
            Object value = ReflectUtils.invokeGet(fieldNames[fieldNames.length-1],object);
            sql.append(fieldNames[fieldNames.length-1]+"=? ");
            params.add(value);
        }
        //填充满足的条件,where字段部分
        if(fieldsConditon.length>0 && fieldNames.length>0){//如果对字段有要求
            sql.append("WHERE ");
            //对不是最后一个的字符串符合的操作进行操作
            for(int i=0;i<fieldsConditon.length-1;i++){
                sql.append(fieldsConditon[i]+"=? AND ");
                Object value = ReflectUtils.invokeGet(fieldsConditon[i],object);
                params.add(value);
            }
            sql.append(fieldsConditon[fieldsConditon.length-1]+"=?");
            Object value = ReflectUtils.invokeGet(fieldsConditon[fieldsConditon.length-1],object);
            params.add(value);
        }

        //调用语句执行操作
        executeDML(sql.toString(),params.toArray());

        return 0;
    }

    /**
     *多行多列的查询
     * @param sql 对应的sql语句
     * @param clazz 对应表结构的类对象
     * @param params sql语句参数
     * @return 返回查询的多组记录
     */
    @Override
    public List queryRows(String sql, Class clazz, Object[] params) throws ClassNotFoundException, SQLException {
        Connection connection = DBManager.getConnectionFromPool();
        PreparedStatement preparedStatement = null;
        List list = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement(sql);
            if(params!=null){//给sql设置参数
                JDBCUtils.handleParams(preparedStatement,params);
            }
            //打印代理执行对象
            System.out.println(preparedStatement);
            resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            //多行
            while(resultSet.next()){
                if(list==null){
                    list = new ArrayList();
                }
                Object rowObject = clazz.newInstance();//调用javabean的无参构造器
                int countColumns = metaData.getColumnCount();
                //多列，select age,name,salary from user where id>? and age<?;
                for(int i=0;i<countColumns;i++){
                    String columnName  = metaData.getColumnLabel(i+1);//获得第i+1隔列名,该方法可以获得列的别名
                    Object columnValue = resultSet.getObject(i+1);
                    //调用对象的setUserName(String name)设置columnValue
                   ReflectUtils.invokeSet(rowObject,columnName,columnValue);
                }
                list.add(rowObject);
            }
        }catch (Exception e){
            System.out.println("查询出错啦！"+e);
            e.printStackTrace();
        }finally{
            DBManager.close(resultSet,preparedStatement);
            DBManager.close(connection);

        }
        return list;
    }

    /**
     *
     * @param sql 对应的sql语句
     * @param clazz 对应表结构的类对象
     * @param params sql语句参数
     * @return 返回查询的一组记录
     */

    @Override
    public Object queryUniqueRow(String sql, Class clazz, Object[] params) throws ClassNotFoundException, SQLException {
        Connection connection = DBManager.getConnectionFromPool();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet =null;
        try{
            preparedStatement = connection.prepareStatement(sql);
            JDBCUtils.handleParams(preparedStatement,params);
            System.out.println(preparedStatement);
            resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            //查询语句
            while (resultSet.next()) {
                //获得类实例对象
                Object rowObject = clazz.newInstance();
                int countColumns = metaData.getColumnCount();
                for(int i=0;i<countColumns;i++){
                    //获得第i+1个属性的值
                    String columnName = metaData.getColumnLabel(i+1);
                    //获得第i+1个字段的值
                    Object columnValue = resultSet.getObject(i+1);
                    //对指定对象的指定属性设置值,存储查询字段的值
                    ReflectUtils.invokeSet(rowObject,columnName,columnValue);
                }
                //返回查询到的对象，里面包含所查询的数据的对应字段的值
                return rowObject;
            }

        }catch(Exception e){

        }finally{
            DBManager.close(resultSet,preparedStatement);
            DBManager.close(connection);
        }
        return null;
    }

    /**
     *此代码已经执行成功！
     * @param sql sql语句(字符串形式)
     * @param params sql语句的参数
     * @return 返回查询语句的单个值
     * 示例sql语句：select age from table_name where id=1?
     */
    @Override
    public Object queryValue(String sql, Object[] params) throws ClassNotFoundException, SQLException {
        Connection connection = DBManager.getConnectionFromPool();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement(sql);
            if(params!=null){
                JDBCUtils.handleParams(preparedStatement,params);
            }
            //打印预编译好的语句
            System.out.println(preparedStatement);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                //由于只有一个数据，因此可以直接取出第一个数据的值即可
                Object object= resultSet.getObject(1);
                return object;
            }
        }catch(Exception e){
            System.out.println("出现异常啦！"+e);
        }finally{
            DBManager.close(resultSet,preparedStatement);
            DBManager.close(connection);
        }
        return null;
    }

    /**
     *目前该方法查询非常的耗时间
     * @param sql sql语句，要求未知参数以preparedStatment占位符的形式给出,即要有问号的占位符?
     * @param params sql参数
     * @return 返回查询的数值信息
     */
    @Override
    public Number queryNumber(String sql, Object[] params) throws ClassNotFoundException, SQLException {
        Connection connection = DBManager.getConnectionFromPool();//使用连接池获得连接对象
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement(sql);
            if(params!=null){
                JDBCUtils.handleParams(preparedStatement,params);
            }
            System.out.println(preparedStatement);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                //由于是查询单行值，所以只需要取出第一行的值即可
                Object object = resultSet.getObject(1);
                return (Number)object;
            }
            return null;
        }catch(Exception e){
            System.out.println("出现错误啦！"+e);
        }finally{
           DBManager.close(resultSet,preparedStatement);
           DBManager.close(connection);//将连接池回收进入连接池中
        }
        return null;
    }

    /**
     * 根据类对象在数据库中创建对应的表结构
     * @param clazz 传入的类对象
     */
    @Override
    public void createTable(Class clazz) throws SQLException, ClassNotFoundException {
        //示例：CREATE TABLE tableName(field fieldType,...)
        //CREATE TABLE tableName(field fieldType,field fieldType);
        //获得表名，注意以下函数获得的第一部分是包名.类名，所有需要将包名和点号去掉并且将后面字符串的第一个字符转换为小写
        if(clazz==null){
            System.out.println("请不要传入空指针类对象！！！");
            return;
        }
        String ClassNameInfo = clazz.getName();
        //获得包名与类名的分隔符"."所在的位置
        int index = ClassNameInfo.indexOf(".");
        //获取对象的类名
        String ClassName = ClassNameInfo.substring(index+1,ClassNameInfo.length());
        //定义表名,转换成功！
        String tableName = StringUtils.firstChar2LowerCase(ClassName);
        //获得类属性信息
        Field[] fields=clazz.getDeclaredFields();
        //声明存储属性名的的数组
        String[] fieldsName = new String[fields.length];
        //声明存储每个属性类型的数组
        String[] fieldsType = new String[fields.length];
        //根据类属性信息获取属性名和属性类型
        for(int i=0;i<fields.length;i++){
            fieldsName[i] = fields[i].getName();
            if(fields[i].getGenericType().toString().length()>16){
                //去除掉class java.lang.部分
                fieldsType[i] =fields[i].getGenericType().toString().substring(16,fields[i].getGenericType()
                        .toString().length());
                continue;
            }
            fieldsType[i] = fields[i].getGenericType().toString();
        }
        //以上已经成功获取对象对应的字段类型和属性信息，可以拼接sql字符串了
        //还有一个任务就是将java数据类型改为数据库类型，因此需要开发对应的接口函数,已经开发完毕
        TypeConvertor convertor = new JavaTypeConvertor();//获取将java数据类型转换为数据库类型的数据
        //拼接sql语句
        StringBuilder sql = new StringBuilder("CREATE TABLE "+tableName+"(");
        for(int i=0;i<fieldsName.length;i++){
            if(i<fieldsName.length-1){//如果i不是最后一个属性
                sql.append(fieldsName[i]+" "+convertor.javaType2DatabaseType(fieldsType[i])+",");
                continue;
            }
            sql.append(fieldsName[i]+" "+fieldsType[i]+")");//如果是最后一个字段
        }

        //在数据库中建立相关表
        executeDML(sql.toString());
        System.out.println("建表成功或者已存在相关的表！");

    }

    /**
     *
     * @param object 传入的对象
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @Override
    public void createTable(Object object) throws SQLException, ClassNotFoundException {
        //获得对象的class对象
        Class clazz = object.getClass();
        if(clazz!=null){
            //调用函数执行建立表操作
            createTable(clazz);
        }else{
            System.out.println("请先建立类！对象传入存在错误！！！");
        }
    }

    /**
     * 创建数据库表
     * @param DBName 传入的数据库名
     */
    @Override
    public void createDataBase(String DBName) throws SQLException, ClassNotFoundException {
        String sql = "CREATE DATABASE "+DBName;
        executeDML(sql);
    }
}
