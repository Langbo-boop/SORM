package SORM.DBConnectionPool;

import SORM.CORE.DBManager;
import SORM.bean.Configuration;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 管理Connection的连接池
 */
public class Pool {
    /**
     * 连接池对象，用于存储和管理Connection
     */
    private static List<Connection> pool;

    /**
     * 连接池中最大连接数，从配置文件中读取
     */
    private static final int POOL_MAX_SIZE = DBManager.getConfiguration().getPOOL_MAX_SIZE();

    /**
     * 连接池中最小连接数，从配置文件中读取得来
     */
    private static final int POOL_MIN_SIZE = DBManager.getConfiguration().getPOOL_MIN_SIZE();


    /**
     *初始化连接池，使其中的连接池达到最少连接数
     */
    public void initPool() throws ClassNotFoundException {
        if(pool==null){
            pool = new ArrayList<Connection>();
        }
        while(pool.size()<POOL_MIN_SIZE){
            pool.add(DBManager.getConnectionWithDB());
            System.out.println("初始化连接池，池中连接数为："+pool.size()+"个。");
        }

    }

    public Pool() throws ClassNotFoundException {
        initPool();
    }

    /**
     * return  从连接池中取出一个连接，是最后一个连接
     */
    public synchronized Connection getConnection(){
        int lastIndex = pool.size()-1;
        Connection connection = pool.get(lastIndex);
        pool.remove(lastIndex);//删掉这个connection
        return connection;
    }

    /**
     * 关闭连接connection，实际是将connection 放回池中并不是真的关闭
     */
    public synchronized void close(Connection connection) throws SQLException {
        if(pool.size()>0) {
            connection.close();//超过数据库最大连接数，真的将此关闭
        }else{
            pool.add(connection);//将连接放入连接池中
        }
    }




}
