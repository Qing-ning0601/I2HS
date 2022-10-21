import club.hsspace.whypps.util.NumberTools;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Arrays;

/**
 * @ClassName: Test
 * @CreateTime: 2022/7/17
 * @Comment: test
 * @Author: Qing_ning
 * @Mail: 1750359613@qq.com
 */
public class Test {

    private static final Logger logger = LoggerFactory.getLogger(Test.class);

    @org.junit.Test
    public void test() throws Exception{

        InputStream sqlInputStream = new FileInputStream("F:\\project\\I2HS\\run\\plugins\\club.hsspace.sqlplugin\\mybatis-config.xml");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(sqlInputStream);
        Element configuration = doc.getDocumentElement();

        Element mappers = doc.createElement("mappers");
        configuration.appendChild(mappers);

        File mapperFile = new File("F:\\project\\I2HS\\run\\app\\club.hsspace.whypps.I2HS\\mapper");
        File[] files = mapperFile.listFiles((dir, name) -> name.endsWith(".xml"));
        for (File file : files) {
            Element mapper = doc.createElement("mapper");
            mappers.appendChild(mapper);

            Attr resource = doc.createAttribute("url");
            resource.setValue("file:///" + file);
            mapper.setAttributeNode(resource);
        }

        TransformerFactory tfac = TransformerFactory.newInstance();
        Transformer tra = tfac.newTransformer();
        DOMSource domSource = new DOMSource(configuration);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        StreamResult sr = new StreamResult(os);
        tra.transform(domSource, sr);

        byte[] bytes = os.toByteArray();
        bytes = Arrays.copyOfRange(bytes, 38, bytes.length);
        bytes = NumberTools.bytesMerger("""
                <?xml version="1.0" encoding="UTF-8" ?>
                <!DOCTYPE configuration
                        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
                        "http://mybatis.org/dtd/mybatis-3-config.dtd">
                """.getBytes(), bytes);

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(new ByteArrayInputStream(bytes));

        try (SqlSession session = sqlSessionFactory.openSession()) {
            Object user = session.selectOne("mapper.User.selectByUserName");

            System.out.println(user);
        }

    }

}
