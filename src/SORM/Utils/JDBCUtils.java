package SORM.Utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 封装jdbc查询常用操作
 * @author 李浪波
 */
public class JDBCUtils {
    /**
     * 给sql语句传入参数
     * @param ps sql语句预编译处理对象
     * @param params 传入的参数
     * @throws SQLException sql操作异常
     */
    public static void handleParams(PreparedStatement ps,Object[]params) throws SQLException {
        if(params.length>0){
            for(int i=0;i<params.length;i++){
                ps.setObject(i+1,params[i]);
            }
        }
    }
}
