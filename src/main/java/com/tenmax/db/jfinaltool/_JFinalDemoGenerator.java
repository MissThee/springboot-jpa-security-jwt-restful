//package com.tenmax.db.jfinaltool;
//
//import com.jfinal.kit.PathKit;
//import com.jfinal.kit.PropKit;
//import com.jfinal.plugin.activerecord.dialect.OracleDialect;
//import com.jfinal.plugin.activerecord.generator.Generator;
//import com.jfinal.plugin.activerecord.generator.MetaBuilder;
//import com.jfinal.plugin.druid.DruidPlugin;
//import com.tenmax.SpringBootSecurityDemoApplication;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//import javax.sql.DataSource;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法 详见 JFinal 俱乐部:
// * http://jfinal.com/club
// * <p>
// * 在数据库表有任何变动时，运行一下 main 方法，极速响应变化进行代码重构
// */
//public class _JFinalDemoGenerator extends SpringBootServletInitializer {
//    private static String driverClassName;
//    private static String url="jdbc:mysql://localhost:3306/spring_boot_security?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&nullNamePatternMatchesAll=true&useSSL=true";
//    private static String username="user";
//    private static String password="1234";
//
//    public static void main(String[] args) {
//        Generator("DB");
////       Generator("DB1");
//    }
//
//
//    private static DataSource getDataSource(String DBName) {
////        PropKit.use("application.properties");
//        DruidPlugin druidPlugin;
//        switch (DBName) {
//            case "DB":
//            default:
//                druidPlugin = new DruidPlugin(url, username, password.trim());
//                break;
////            case "DB1":
////                druidPlugin = new DruidPlugin(PropKit.get("spring.datasource1.url"), PropKit.get("spring.datasource1.username"), PropKit.get("spring.datasource1.password").trim());
////                break;
//        }
//        druidPlugin.start();
//        return druidPlugin.getDataSource();
//    }
//
//    private static void Generator() {
//        Generator("");
//    }
//
//    private static void Generator(String DBType) {
//        // model 所使用的包名 (MappingKit 默认使用的包名)
//        String modelPackageName = "com.tenmax.db.primary.jentity" + DBType.replace("DB", "");
//        // model 文件保存路径 (MappingKit 与 DataDictionary 文件默认保存路径)
//        String modelOutputDir = PathKit.getWebRootPath() + "/src/main/java/" + modelPackageName.replace(".", "/");
//        // base model 所使用的包名
//        String baseModelPackageName = modelPackageName + ".base";
//        // base model 文件保存路径
//        String baseModelOutputDir = modelOutputDir + "/base";
//
//        // 创建生成器
//        Generator generator = new Generator(getDataSource(DBType), baseModelPackageName, baseModelOutputDir, modelPackageName, modelOutputDir);
//        switch (DBType) {
//            case "DB":
//                // 添加不需要生成的表名
//                generator.addExcludedTable("tb_generator");
//                generator.addExcludedTable("sys_role_permission");
//                generator.addExcludedTable("sys_user_role");
//                break;
//            case "DB1":
//                // 指定需要生成的表名（不指定时，需注释此方法）
//                generator.setMetaBuilder(new _MetaBuilder(getDataSource("DB1")));
//                generator.setDialect(new OracleDialect());
//                break;
//        }
//        // 设置是否在 Model 中生成 dao 对象
//        generator.setGenerateDaoInModel(true);
//        // 设置是否生成链式 setter 方法
//        generator.setGenerateChainSetter(true);
//        // 设置是否生成字典文件
//        generator.setGenerateDataDictionary(false);
//        // 设置需要被移除的表名前缀用于生成modelName。例如表名 "osc_user"，移除前缀 "osc_"后生成的model名为"User"而非 OscUser
//        generator.setRemovedTableNamePrefixes("t_");
//        // 生成
//        generator.generate();
//
//    }
//
//    public static class _MetaBuilder extends MetaBuilder {
//        public _MetaBuilder(DataSource dataSource) {
//            super(dataSource);
//        }
//
//        @Override
//        protected boolean isSkipTable(String tableName) {
//            List<String> tableLi = new ArrayList<>();
//            tableLi.add("D_PARAM");//填写需生成的表名
//            tableLi.add("D_PARAM_LAST");
//            tableLi.add("S_DEVICE_IP");
//            tableLi.add("SYS_CMD");
//            return !tableLi.contains(tableName);
//        }
//    }
//}
