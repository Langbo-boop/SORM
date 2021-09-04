package SORM.CORE;

/**
 * 执行sql的Query对象
 * 创建Query对象的工厂类
 * @author 李浪波
 */
public class QueryFactory implements Cloneable{

    //单例模式，工厂类只能有一个
    private static QueryFactory queryFactory = new QueryFactory();

    private static Query prototypeQuery;//原型对象

    static{
        try {
             Class clazz = Class.forName(DBManager.getConfiguration().getQueryClass());
             prototypeQuery = (Query)clazz.newInstance();
        } catch (ClassNotFoundException e) {
            System.out.println("加载类失败啦！"+e);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 私有化配置信息
     */
    private QueryFactory(){

    }
    /**
     * 根据配置信息来配置不同的Query对象
     * @return
     */
    public Query createQuery() throws InstantiationException, IllegalAccessException {
        /**
         * 通过克隆返回对应的对象
         */
        return  prototypeQuery;

    }

    /**
     * 单例模式返回queryFactory工厂
     * @return
     */
    public static QueryFactory getQueryFactory(){
        return queryFactory;
    }
}
