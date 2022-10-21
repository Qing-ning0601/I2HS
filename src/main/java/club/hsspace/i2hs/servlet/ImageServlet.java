package club.hsspace.i2hs.servlet;

import club.hsspace.i2hs.ImageController;
import club.hsspace.i2hs.ImageMapper;
import club.hsspace.i2hs.entity.ChainEntity;
import club.hsspace.i2hs.entity.ImageEntity;
import club.hsspace.i2hs.entity.VisitEntity;
import club.hsspace.i2hs.service.OOSService;
import club.hsspace.whypps.action.Init;
import club.hsspace.whypps.action.Injection;
import club.hsspace.whypps.framework.app.annotation.AppInterface;
import club.hsspace.whypps.framework.app.annotation.AppStart;
import com.github.yitter.idgen.YitIdHelper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @ClassName: ImageServlet
 * @CreateTime: 2022/8/9
 * @Comment: 图床访问
 * @Author: Qing_ning
 * @Mail: 1750359613@qq.com
 */
@AppInterface
public class ImageServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ImageServlet.class);

    private SqlSessionFactory sqlSessionFactory;

    @Injection
    private OOSService oosService;

    @Init
    public void initSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
        logger.info("ImageServlet初始化成功");
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        String thumbnail = req.getParameter("thumbnail");

        SqlSession sqlSession = sqlSessionFactory.openSession();
        ImageMapper mapper = sqlSession.getMapper(ImageMapper.class);

        resp.setContentType("image/jpeg");
        String chainAddr = path.substring(1);
        ChainEntity chainEntity = mapper.selectChainByChainAddr(chainAddr);

        String uri = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getRequestURI() + (req.getQueryString() != null ? "?" + req.getQueryString() : "");

        VisitEntity visitEntity = new VisitEntity();
        visitEntity.setLogId(YitIdHelper.nextId());
        visitEntity.setUrl(uri);
        visitEntity.setIp(req.getRemoteAddr() + ":" +req.getRemotePort());

        if (chainEntity == null) {
            resp.sendError(404, "资源不存在");
            visitEntity.setSuccess(false);
        } else {
            ImageEntity imageEntity = mapper.selectImageOnce(chainEntity.getImageId());

            byte[] image;
            if (thumbnail == null)
                image = oosService.download(mapper.selectFileOnce(imageEntity.getImageNum()).getPath());
            else
                image = oosService.download(mapper.selectFileOnce(imageEntity.getThumbnailNum()).getPath());

            ServletOutputStream outputStream = resp.getOutputStream();
            outputStream.write(image);
            outputStream.flush();
            outputStream.close();
            visitEntity.setSuccess(true);
            visitEntity.setChainId(chainEntity.getImageId());

        }

        mapper.insertVisit(visitEntity);

        sqlSession.commit();
        sqlSession.close();
    }

}
