package SORM.bean;

/**
 * 管理配置文件的信息
 * @author 李浪波
 */
public class Configuration {

    /**
     * 驱动类
     */
    private String driver;

    /**
     * jdbc的url
     */
    private String url;

    /**
     * 数据库用户名
     */
    private String user;

    /**
     * 数据库用户名密码
     */
    private String pwd;

    /**
     * 当前使用的数据库
     */
    private String usingDB;

    /**
     * 源代码绝对路径
     */
    private String srcPath;

    /**
     * 扫描生成java类的包，（po的意思是persistence object即持久对象，意味着和类和表结构对应)
     */
    private String poPackage;

    /**
     * 使用的queryClass对象
     */
    private String queryClass;

    /**
     * 连接池最大连接数
     */
    private int  POOL_MAX_SIZE;

    /**
     * 连接池最小连接数
     */
    private int POOL_MIN_SIZE;

    public Configuration(){

    }

    public Configuration(String driver, String url, String user, String pwd, String usingDB, String srcPath, String poPackage, String queryClass) {
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.pwd = pwd;
        this.usingDB = usingDB;
        this.srcPath = srcPath;
        this.poPackage = poPackage;
        this.queryClass = queryClass;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getUsingDB() {
        return usingDB;
    }

    public void setUsingDB(String usingDB) {
        this.usingDB = usingDB;
    }

    public String getSrcPath() {
        return srcPath;
    }

    public void setSrcPath(String srcPath) {
        this.srcPath = srcPath;
    }

    public String getPoPackage() {
        return poPackage;
    }

    public void setPoPackage(String poPackage) {
        this.poPackage = poPackage;
    }

    public String getQueryClass() {
        return queryClass;
    }

    public void setQueryClass(String queryClass) {
        this.queryClass = queryClass;
    }

    public int getPOOL_MAX_SIZE() {
        return POOL_MAX_SIZE;
    }

    public void setPOOL_MAX_SIZE(int POOL_MAX_SIZE) {
        this.POOL_MAX_SIZE = POOL_MAX_SIZE;
    }

    public int getPOOL_MIN_SIZE() {
        return POOL_MIN_SIZE;
    }

    public void setPOOL_MIN_SIZE(int POOL_MIN_SIZE) {
        this.POOL_MIN_SIZE = POOL_MIN_SIZE;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "driver='" + driver + '\'' +
                ", url='" + url + '\'' +
                ", user='" + user + '\'' +
                ", pwd='" + pwd + '\'' +
                ", usingDB='" + usingDB + '\'' +
                ", srcPath='" + srcPath + '\'' +
                ", poPackage='" + poPackage + '\'' +
                ", queryClass='" + queryClass + '\'' +
                ", POOL_MAX_SIZE=" + POOL_MAX_SIZE +
                ", POOL_MIN_SIZE=" + POOL_MIN_SIZE +
                '}';
    }
}
