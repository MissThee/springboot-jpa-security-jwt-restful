//package com.tenmax.db;
//
//import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
//import com.jfinal.plugin.activerecord.dialect.OracleDialect;
//import com.jfinal.plugin.druid.DruidPlugin;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//@Component
//public class _ActiveRecordTool {
//
//    static private String url;
//    static private String userName;
//    static private String password;
//    static private String driverClassName;
//
//    static private String url1;
//    static private String userName1;
//    static private String password1;
//    static private String driverClassName1;
//
//
//    @Value("${spring.datasource0.url}")
//    public void setUrl(String a) {
//        url = a;
//    }
//
//    @Value("${spring.datasource0.username}")
//    public void setUserName(String a) {
//        userName = a;
//    }
//
//    @Value("${spring.datasource0.password}")
//    public void setPassword(String a) {
//        password = a;
//    }
//
//    @Value("${spring.datasource0.driver-class-name}")
//    public void setDriverClassName(String a) {
//        driverClassName = a;
//    }
//
//    @Value("${spring.datasource1.url}")
//    public void setUrl1(String a) {
//        url1 = a;
//    }
//
//    @Value("${spring.datasource1.username}")
//    public void setUserName1(String a) {
//        userName1 = a;
//    }
//
//    @Value("${spring.datasource1.password}")
//    public void setPassword1(String a) {
//        password1 = a;
//    }
//
//    @Value("${spring.datasource1.driver-class-name}")
//    public void setDriverClassName1(String a) {
//        driverClassName1 = a;
//    }
//
//    public static void start() {
//        DruidPlugin dp = new DruidPlugin(url, userName, password, driverClassName);
//        ActiveRecordPlugin arp = new ActiveRecordPlugin("DB", dp);
//        server.model.db._MappingKit.mapping(arp);
//        // 与web环境唯一的不同是要手动调用一次相关插件的start()方法
//        dp.start();
//        arp.start();
//        arp.setShowSql(true);
//
//        DruidPlugin dp1 = new DruidPlugin(url1, userName1, password1, driverClassName1);
//        ActiveRecordPlugin arp1 = new ActiveRecordPlugin("DB1", dp1);
//        server.model.db1._MappingKit.mapping(arp1);
//        dp1.start();
//        arp1.setDialect(new OracleDialect());
//        arp1.start();arp1.setShowSql(true);
//    }
//
//}
