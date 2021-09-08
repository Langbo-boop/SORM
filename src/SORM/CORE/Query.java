package SORM.CORE;

import java.sql.SQLException;
import java.util.List;

/**
 * 为用户提供增删改查的服务接口类
 * @author 李浪波
 */
public interface Query {

    /**
     * goal:指定参数或者不指定参数，执行sql语句
     * @param sql 相对应的sql语句
     * @param params 参数
     */
    public void executeDML(String sql,Object[] params) throws ClassNotFoundException, SQLException;

    /**
     * 仅根据sql语句
     * @param sql 执行sql语句
     */
    public void executeDML(String sql) throws ClassNotFoundException, SQLException;

    /**
     * 把对象中不为null的数据存储入数据库中，如果数字，默认为0
     * 将对应的object对象添加入数据库中
     * @param object 待添加的对象
     */
    public void insert(Object object) throws ClassNotFoundException, SQLException;

    /**
     * goal:删除clazz类中对应id的记录
     * @param clazz 对应类的对象
     * @param id 指定待删除记录的id
     */
    public void delete(Class clazz,Object id) throws ClassNotFoundException, SQLException; //delete from table_name where id=? (this is the condition).

    /**
     * 删除指定的对象
     * @param object 待删除的对象
     */
    public void delete(Object object);

    /**
     * 根据指定对象的任意字段及其值执行删除语句
     * 示例：delete from where name="billy"
     * 这个方法可以避免只使用主键来删除字段的局限，实现了只要指定任意字段名及其值就可进行删除操作的目标
     * @param object 待删除的对象
     * @param fieldName 待删除对象的字段名
     * @param value 字段值
     * 核心idea，我们可以使用value和fieldName来拼接符合条件的删除执行的sql语句
     *              而对应的表可以使我们找到待删除的表,从而完成整个sql语句字符串的拼接

     */
    public void delete(Object object,String fieldName,String value) throws ClassNotFoundException, SQLException;

    /**
     * 更新记录的信息,同时更新表中的多条记录,fieldName列表为指定更新的属性名
     * @param object 更新的对象
     * @param fieldNames 更新的属性列表
     * @return 更新属性的个数
     */

    public int update(Object object,String[] fieldNames) throws ClassNotFoundException, SQLException;//delete user set name=? age=?

    /**
     * 根据指定的条件更新多条目录
     * @param object 待更新的对象
     * @param fieldNames 待更新的对象的属性名数组
     * @param fieldsConditon 待更新对象需要满足的条件
     * @return 更新影响的记录条数
     * 示例：update table_name set fieldName=? where column=?
     */
    public int update(Object object,String[]fieldNames,String[]fieldsConditon) throws ClassNotFoundException, SQLException;

    /**
     *返回多行记录，并且将每行记录封装到clazz指定的类对象中
     * @param sql 对应的sql语句
     * @param clazz 对应表结构的类对象
     * @param params sql语句参数
     * @return 返回查询到的结果列表
     */

    public List queryRows(String sql,Class clazz,Object[] params) throws ClassNotFoundException, SQLException;

    /**
     *返回一行记录，并且将每行记录封装到clazz指定的类对象中
     * @param sql 对应的sql语句
     * @param clazz 对应表结构的类对象
     * @param params sql语句参数
     * @return 返回查询到的结果列表
     */
    public Object queryUniqueRow(String sql,Class clazz,Object[] params) throws ClassNotFoundException, SQLException;

    /**
     * 查询返回一个值
     * @param sql sql语句(字符串形式)
     * @param params sql语句的参数
     * @return 返回查询值
     */
    public Object queryValue(String sql,Object[] params) throws ClassNotFoundException, SQLException;

    /**
     * 查询返回一个数字
     * @param sql sql语句
     * @param params sql参数
     * @return 返回的数字
     */
    public Number queryNumber(String sql,Object[] params) throws ClassNotFoundException, SQLException;

    /**
     * 根据传入类对象在mysql数据库中执行对应的建立表操作
     * @param clazz 传入的类的Class对象
     */
    public void createTable(Class clazz) throws SQLException, ClassNotFoundException;

    /**
     * 根据传入对象在对应的数据库中建立与该对象对应的表
     * @param object
     */
    public void createTable(Object object) throws SQLException, ClassNotFoundException;

    /**
     * 创建数据库
     * @param DBName 传入的数据库名
     */
    public void createDataBase(String DBName) throws SQLException, ClassNotFoundException;



}
