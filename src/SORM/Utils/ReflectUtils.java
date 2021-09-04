package SORM.Utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 封装了反射的常见操作
 * @author 李浪波
 */
public class ReflectUtils {
    /**
     * 通过反射调用获取get方法并且获得其主键
     * @param name 表里的主键名
     * @param obj 示例对象
     * @return 待删除的主键条件
     */
    public static Object invokeGet(String name,Object obj){
        try{
            Class c = obj.getClass();
            Method method = c.getMethod(("get"+StringUtils.firstChar2UpperCase(name)),null);
            return method.invoke(obj,null);
        }catch (Exception e){
            System.out.println("出现错误啦！"+e);
            return null;//生成方法不成功！
        }
    }
    public static void invokeSet(Object object,String columnName,Object columnValue) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class clazz = object.getClass();
        Method method = clazz.getDeclaredMethod("set"+StringUtils.firstChar2UpperCase(columnName),
                columnValue.getClass());
        method.invoke(object,columnValue);
    }
}
