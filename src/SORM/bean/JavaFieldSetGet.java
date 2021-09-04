package SORM.bean;

/**
 * 封装java对象的属性和set，get方法
 * 为属性和set，get方法的源代码
 * 根据mysql数据类型封装转换成对应的java声明代码和set,get方法
 */
public class JavaFieldSetGet {
    /**
     * java对象的属性信息
     */
    private String fieldInfo;//private int a;
    /**
     * java属性的信息获取
     */
    private String getInfo; //public int getID(){return this.id;}
    /**
     * java属性信息设置
     */
    private String setInfo;//public setID(int id){this.id=id;}

    public JavaFieldSetGet() {
    }

    public JavaFieldSetGet(String fieldInfo, String getInfo, String setInfo) {
        this.fieldInfo = fieldInfo;
        this.getInfo = getInfo;
        this.setInfo = setInfo;
    }

    public String getFieldInfo() {
        return fieldInfo;
    }

    public void setFieldInfo(String fieldInfo) {
        this.fieldInfo = fieldInfo;
    }

    public String getGetInfo() {
        return getInfo;
    }

    public void setGetInfo(String getInfo) {
        this.getInfo = getInfo;
    }

    public String getSetInfo() {
        return setInfo;
    }

    public void setSetInfo(String setInfo) {
        this.setInfo = setInfo;
    }
    //重写toString方法
    @Override
    public String toString() {
        return fieldInfo+"\n"+getInfo+"\n"+setInfo;
    }
}
