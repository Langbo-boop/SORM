package SORM.CORE;

import java.sql.SQLException;

/**
 * 测试类，测试编写的函数是否正常运行,测试连接池是否正常工作
 */
public class Test {
    public static void query() throws InstantiationException, IllegalAccessException, SQLException, ClassNotFoundException {
        //获得工厂对象
        QueryFactory queryFactory = QueryFactory.getQueryFactory();
        //根据工厂对象创建Query对象
        Query query = queryFactory.createQuery();
        String sql = "SELECT age FROM employee where id=?";
        Object[]params={1};
        Number age = query.queryNumber(sql,params);
        System.out.println("查询的年龄为："+age);
    }
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, SQLException, ClassNotFoundException {
        long startTime = System.currentTimeMillis();
        for(int i=0;i<40000;i++){
            query();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("加连接池的耗时为:"+(endTime-startTime)+"ms");
        /**
         * 不加连接池的耗时约等于41312ms
         * 加连接池的耗时为2023ms，效率提升了差不多20倍
         * 即使数量再次增加，连接池的效率依然很高
         */

    }
}
