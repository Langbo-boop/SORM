package SORM.CORE;

import SORM.DBConnectionPool.Pool;
import SORM.bean.Configuration;


import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * 根据配置信息，维持与数据库连接池的管理
 */
public class DBManager {
    /**
     * //配置文件对象，里面存储了配置文件的各个属性的信息
     */
    private static Configuration configuration;
    private static Pool pool;

    static {
        //生成加载配置文件的Properties对象
        Properties properties = new Properties();
        try {
            //加载配置文件
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties"));
        } catch (IOException e) {
            System.out.println("加载db.properties文件出错啦！" + e);
            e.printStackTrace();
        }
        //生成配置文件对象
        configuration = new Configuration();
        configuration.setDriver(properties.getProperty("driver"));
        configuration.setUser(properties.getProperty("user"));
        configuration.setPwd(properties.getProperty("pwd"));
        configuration.setUrl(properties.getProperty("url"));
        configuration.setSrcPath(properties.getProperty("srcPath"));
        configuration.setPoPackage(properties.getProperty("poPackage"));
        configuration.setUsingDB(properties.getProperty("usingDB"));
        configuration.setQueryClass(properties.getProperty("queryClass"));
        configuration.setPOOL_MAX_SIZE(Integer.parseInt(properties.getProperty("POOL_MAX_SIZE")));
        configuration.setPOOL_MIN_SIZE(Integer.parseInt(properties.getProperty("POOL_MIN_SIZE")));


    }

    //返回连接对象
    public static Connection getConnectionWithDB() throws ClassNotFoundException {
        //加载驱动类
        Class.forName(configuration.getDriver());
        try {
            Connection connection = DriverManager.getConnection(configuration.getUrl(), configuration.getUser(),
                    configuration.getPwd());
            return connection; //先建立连接，后期再增加连接池处理
        } catch (SQLException e) {
            System.out.println("建立连接失败！");
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @return 返回从连接池中获得的对象
     */
    public static Connection getConnectionFromPool() throws ClassNotFoundException {
        if(pool==null){
            pool = new Pool();
        }
        return pool.getConnection();
    }

    /**
     * 关闭连接池中的对象
     * @param connection 待关闭的对象
     * @throws SQLException
     */
    public static void close(Connection connection) throws SQLException {
        pool.close(connection);
    }
    public static void close(ResultSet resultSet, Statement statement){
        try{
            if(resultSet!=null) {
                resultSet.close();
            }
        }catch (Exception e) {
            System.out.println("关闭ResultSet时失败啦！"+e);
        }
        try {
            if(statement!=null) {
                statement.close();
            }
        }catch(Exception e) {
            System.out.println("关闭Statement时失败啦！"+e);
        }
    }

    //close stream after using connection
    public static void close(ResultSet resultSet, Statement statement, Connection connection) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException throwables) {
            System.out.println("ResultSet流关闭失败！");
            throwables.printStackTrace();
        }
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException throwables) {
            System.out.println("Statement流关闭失败！");
            throwables.printStackTrace();
        }
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException throwables) {
            System.out.println("Connection关闭失败！");
            throwables.printStackTrace();
        }
    }
    //close related stream
    public static void close(Statement statement,Connection connection){
        try{
            if(statement!=null){
                statement.close();
            }
        } catch (SQLException throwables) {
            System.out.println("Statement关闭失败！");
            throwables.printStackTrace();
        }
        try{
            if(connection!=null){
                connection.close();
            }
        } catch (SQLException throwables) {
            System.out.println("Connection关闭失败！");
            throwables.printStackTrace();
        }

    }

    public static void close(Statement statement){
        try{
            if(statement!=null){
                statement.close();
            }
        } catch (SQLException throwables) {
            System.out.println("关闭Statement时出错啦！"+throwables);
            throwables.printStackTrace();
        }
    }

    //获取Configuration对象
    public static Configuration getConfiguration(){
        return configuration;//返回configuration对象
    }

}
