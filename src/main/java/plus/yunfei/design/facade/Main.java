package plus.yunfei.design.facade;

import sun.security.x509.OCSPNoCheckExtension;

/**
 * @author houyunfei
 */
public class Main {
    public static void main(String[] args) {
        ServerFacade tomcat = new Tomcat();
        ServerFacade mySQL = new MySQL();
        /**
         * 门外/外观模式
         */
        tomcat.start();
        mySQL.start();
    }
}

interface ServerFacade{
    void start();
}


class Tomcat implements ServerFacade{
    void initEngine() {
        System.out.println("Tomcat 引擎初始化");
    }

    void initWeb() {
        System.out.println("Tomcat Web 初始化");
    }

    @Override
    public void start() {
        initEngine();
        initWeb();
    }
}


class MySQL implements ServerFacade{
    void initData() {
        System.out.println("MySQL 数据库初始化数据");
    }

    void checkLog() {
        System.out.println("MySQL 数据库检查日志");
    }

    void unlock() {
        System.out.println("MySQL 数据库解锁");
    }

    void listenPort() {
        System.out.println("MySQL 数据库监听端口");
    }

    @Override
    public void start() {
        initData();
        checkLog();
        unlock();
        listenPort();
    }
}
