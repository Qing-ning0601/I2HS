package club.hsspace.i2hs;

import club.hsspace.i2hs.servlet.ImageServlet;
import club.hsspace.whypps.action.Init;
import club.hsspace.whypps.framework.app.annotation.AppInterface;
import club.hsspace.whypps.framework.app.annotation.AppStart;
import club.hsspace.whypps.framework.manage.RunningSpace;
import club.hsspace.whypps.manage.ContainerManage;
import club.hsspace.whypps.model.ContainerClosable;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.EmptyResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @ClassName: RunTomcat
 * @CreateTime: 2022/8/9
 * @Comment: 运行Tomcat
 * @Author: Qing_ning
 * @Mail: 1750359613@qq.com
 */
@AppInterface(registerSort = 100)
public class RunTomcat implements ContainerClosable {

    private static final Logger logger = LoggerFactory.getLogger(RunTomcat.class);

    private String httpUrl;

    private String httpPort;

    @Init
    private void initWeb(RunningSpace runningSpace) throws IOException {
        InputStream is = runningSpace.getInputStream("http.properties");
        Properties prop = new Properties();
        prop.load(is);

        httpUrl = prop.getProperty("url", "http://127.0.0.1:8080/");
        httpPort = prop.getProperty("port", "8080");
    }

    private Tomcat tomcat;
    private Connector connector;

    @AppStart
    public void run(RunningSpace runningSpace, ContainerManage containerManage) throws LifecycleException, IOException {
        Thread.currentThread().setContextClassLoader(RunTomcat.class.getClassLoader());

        tomcat = new Tomcat();
        tomcat.setPort(8080);
        connector = tomcat.getConnector();

        Context ctx = tomcat.addContext("", runningSpace.getFile("tomcat").getAbsolutePath());

        Tomcat.addServlet(ctx, "I2HS", containerManage.getFromClass(ImageServlet.class));

        ctx.addServletMappingDecoded("/*", "I2HS");

        tomcat.start();

        logger.info("成功启动Tomcat");
    }

    @Override
    public void closeTask() {
        try {
            tomcat. stop();
        } catch (LifecycleException e) {
            throw new RuntimeException(e);
        }
        logger.info("已关闭Tomcat服务");
    }

    @Override
    public void close() throws IOException {
        try {
            tomcat.destroy();
        } catch (LifecycleException e) {
            throw new RuntimeException(e);
        }
        logger.info("已关闭Tomcat线程");
    }
}
