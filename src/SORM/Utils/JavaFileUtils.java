package SORM.Utils;

import SORM.CORE.DBManager;
import SORM.CORE.MysqlTypeConvertor;
import SORM.CORE.TableContext;
import SORM.CORE.TypeConvertor;
import SORM.bean.ColumnInfo;
import SORM.bean.Configuration;
import SORM.bean.JavaFieldSetGet;
import SORM.bean.TableInfo;
import com.sun.security.jgss.GSSUtil;
import jdk.swing.interop.SwingInterOpUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;

/**
 * 封装了生成java文件常用的操作,以及数据库字段生成java代码相关的操作
 * @author 李浪波
 */
public class JavaFileUtils {
    /**
     * 根据字段的信息，返回对应的java属性信息和set，get方法源码;比如username(varchar)-->username(String)以及对应的set和get方法
     * @param columnInfo 字段信息
     * @param typeConvertor 类型转换器
     * @return  返回JavaFieldSetGet对象
     * 该函数已经别被测试生成成功啦！
     */
    public static JavaFieldSetGet createJavaFieldSetGetSRC(ColumnInfo columnInfo, TypeConvertor typeConvertor){

        JavaFieldSetGet javaFieldSetGet = new JavaFieldSetGet();
        String javaFieldType = typeConvertor.databaseType2JavaType(columnInfo.getDataType());//获得java类属性的类型
        /**
         * 生成对应的java语句
         */
        javaFieldSetGet.setFieldInfo("\tprivate "+javaFieldType+" "+columnInfo.getName()+";\n");

        /**
         * 生成get方法的源码
         * 示例: public user getUser(){return user;}
         */
        StringBuilder getSrc = new StringBuilder();
        getSrc.append("\tpublic "+javaFieldType+" get"+StringUtils.firstChar2UpperCase(columnInfo.getName())+"(){\n");
        getSrc.append("\t\treturn "+columnInfo.getName()+";\n");
        getSrc.append("\t}\n");
        javaFieldSetGet.setGetInfo(getSrc.toString());

        /**
         * 生成get方法的源代码
         * 示例：public void setUsername(JavaFieldType username){this.username=username;}
         */
        StringBuilder setSrc = new StringBuilder();
        setSrc.append("\tpublic "+"void "+"set"+StringUtils.firstChar2UpperCase(columnInfo.getName()));
        setSrc.append("("+javaFieldType+" "+columnInfo.getName()+"){\n");
        setSrc.append("\t\tthis."+columnInfo.getName()+" = "+columnInfo.getName()+";\n");
        setSrc.append("\t}\n");
        javaFieldSetGet.setSetInfo(setSrc.toString());

        return javaFieldSetGet;//返回已经封装好的源码对象
    }

    /**
     * 根据表信息，生成java类源代码
     * @param tableInfo mysql表结构信息
     * @param convertor 数据类型转换器
     * @return java类的源代码
     */
    private static String createJavaClassSRC(TableInfo tableInfo,MysqlTypeConvertor convertor){
        //源代码拼接对象
        StringBuilder src = new StringBuilder();
        //表中的字段及其映射
        var columnInfoMap = tableInfo.getColumnInfoMap();
        //表中的字段对应的代码集合
        var javaFieldSetGets = new ArrayList<JavaFieldSetGet>();

        //生成字段的set,get方法并且将其存入容器之中
        for(ColumnInfo c:columnInfoMap.values()){
            javaFieldSetGets.add(createJavaFieldSetGetSRC(c,convertor));
        }

        //生成package语句
        src.append("package "+ DBManager.getConfiguration().getPoPackage()+";\n\n");
        //生成import语句
        src.append("import"+" java.sql.*;\n");
        src.append("import"+" java.util.*;\n\n");
        //生成类声明语句
        src.append("public class "+StringUtils.firstChar2UpperCase(tableInfo.getName())+" {\n\n");
        //生成属性列表
        for(JavaFieldSetGet javaFieldSetGet:javaFieldSetGets){
            src.append(javaFieldSetGet.getFieldInfo());
        }
        src.append("\n");

        //生成无参构造器
        //示例：public User(){}
        src.append("\tpublic "+StringUtils.firstChar2UpperCase(tableInfo.getName())+"(){\n");
        src.append("\t}\n\n");

        //生成带全部参数的构造器,为了避免和有些类的数据库其实没有字段，因此需要判断其是否有字段，否则该方法不应该调用，从而可以避免出现问题
        //示例：public User(String user){this.user = user;}
        if(columnInfoMap.size()>0){//仅当存在参数时，才调用此方法
            src.append("\tpublic "+StringUtils.firstChar2UpperCase(tableInfo.getName())+"(");
            //添加字段
            int length = columnInfoMap.size();
            int count = 0;
            for(ColumnInfo columnInfo:columnInfoMap.values()){
                if(count<length-1){
                    src.append(convertor.databaseType2JavaType(columnInfo.getDataType())+" "+columnInfo.getName()+",");
                }else{
                    src.append(convertor.databaseType2JavaType(columnInfo.getDataType())+" "+columnInfo.getName());
                }
                count++;
            }
            src.append("){\n");
            for(ColumnInfo columnInfo:columnInfoMap.values()){
                src.append("\t\tthis."+columnInfo.getName()+" = "+columnInfo.getName()+";\n");
            }
            src.append("\t}\n\n");

        }
        //生成set,get的源代码
        for(JavaFieldSetGet javaFieldSetGet:javaFieldSetGets){
            src.append(javaFieldSetGet.getGetInfo()+"\n");
        }
        for(JavaFieldSetGet javaFieldSetGet:javaFieldSetGets){
            src.append(javaFieldSetGet.getSetInfo()+"\n");
        }
        //生成结束时的代码
        src.append("}\n");

        //返回生成的源代码
        return src.toString();

    }

    /**
     * 将生成的代码类源文件生成放入指定的Po包下.
     * @param tableInfo 数据库表信息
     * @param convertor 数据类型转换器，mysql-->java
     */
    public static void createJavaPOFile(TableInfo tableInfo,MysqlTypeConvertor convertor) throws IOException {
        String src = createJavaClassSRC(tableInfo,convertor);//生成源代码文件
        //拼接指定读入的目录路径
        String srcPath = DBManager.getConfiguration().getSrcPath()+"\\";
        String poPath = DBManager.getConfiguration().getPoPackage().replaceAll("\\.","\\\\");
        String destPath = srcPath+poPath;
        File f = new File(destPath);
        if(!f.exists()){
            f.mkdirs();
        }
        System.out.println(f.getAbsoluteFile());
        BufferedWriter bw = null;
        bw = new BufferedWriter(new FileWriter(f.getAbsolutePath()+"\\"+StringUtils.firstChar2UpperCase(tableInfo.getName()+".java")));
        bw.write(src);//写入源代码文件内容
        bw.flush();
        //打印建立表的日志信息
        System.out.println("建立表"+tableInfo.getName()+"对应的类："+StringUtils.firstChar2UpperCase(tableInfo.getName()+".java"));
        //System.out.println(src);
    }

    //测试此类中的两个函数是否正常工作
//    public static void main(String[] args) throws IOException {
//        MysqlTypeConvertor convertor =new MysqlTypeConvertor();
//        var map = TableContext.getTableInfoMap();
//        for(TableInfo t:map.values()){
//            //System.out.println(t);
//            createJavaPOFile(t,convertor);
//        }
//
//    }
}
