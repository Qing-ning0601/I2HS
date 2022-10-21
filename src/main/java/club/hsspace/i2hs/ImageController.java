package club.hsspace.i2hs;

import club.hsspace.i2hs.entity.ChainEntity;
import club.hsspace.i2hs.entity.FileEntity;
import club.hsspace.i2hs.entity.ImageEntity;
import club.hsspace.i2hs.service.OOSService;
import club.hsspace.whypps.action.Init;
import club.hsspace.whypps.framework.app.BinRRecord;
import club.hsspace.whypps.framework.app.annotation.ApiBinMsg;
import club.hsspace.whypps.framework.app.annotation.ApiDataMsg;
import club.hsspace.whypps.framework.app.annotation.AppInterface;
import club.hsspace.whypps.framework.app.annotation.DataParam;
import club.hsspace.whypps.framework.manage.RunningSpace;
import club.hsspace.whypps.model.senior.Code;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.yitter.idgen.YitIdHelper;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @ClassName: ImageController
 * @CreateTime: 2022/8/1
 * @Comment: 图片控制层
 * @Author: Qing_ning
 * @Mail: 1750359613@qq.com
 */
@AppInterface
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    private String httpUrl;

    private String httpPort;

    @Init
    private void initWeb(RunningSpace runningSpace) throws IOException {
        InputStream is = runningSpace.getInputStream("http.properties");
        Properties prop = new Properties();
        prop.load(is);

        httpUrl = prop.getProperty("url", "http://127.0.0.1:8080/");
        httpPort = prop.getProperty("port","8080");
    }

    @ApiDataMsg("/getImageCount")
    public int getImageCount(SqlSession sqlSession, UserMount userMount) {
        ImageMapper mapper = sqlSession.getMapper(ImageMapper.class);
        return mapper.selectImageCount(userMount.getUser().getUserId());
    }

    @ApiDataMsg("/getImage")
    public JSONArray getImage(SqlSession sqlSession, UserMount userMount, @DataParam("page") Integer page, @DataParam("rowCount") Integer count) {
        ImageMapper mapper = sqlSession.getMapper(ImageMapper.class);
        List<ImageEntity> imageEntities = mapper.selectImage(userMount.getUser().getUserId(), (page - 1) * count, count);
        JSONArray ja = new JSONArray();
        List<JSONObject> jos = imageEntities.stream()
                .map(imageEntity -> {
                    JSONObject jo = new JSONObject();
                    jo.put("id", imageEntity.getImageId());
                    jo.put("name", imageEntity.getName());
                    jo.put("tag", imageEntity.getTag());
                    jo.put("enable", imageEntity.isEnable() ? "启用" : "禁用");
                    jo.put("uploadTime", imageEntity.getCreateTime());
                    return jo;
                }).collect(Collectors.toList());
        ja.addAll(jos);
        return ja;
    }

    @ApiBinMsg("/lookupImage")
    public BinRRecord lookupImage(OOSService oosService, SqlSession sqlSession, UserMount userMount, @DataParam("imageId") Long imageId) {
        ImageMapper mapper = sqlSession.getMapper(ImageMapper.class);
        Map<String, Object> resultInfo = mapper.lookupImage(imageId, userMount.getUser().getUserId());
        JSONObject data = new JSONObject(resultInfo);
        byte[] image = oosService.download(data.getString("path"));
        data.remove("path");
        return new BinRRecord(data, image);
    }

    @ApiDataMsg("/deleteImage")
    public Code deleteImage(SqlSession sqlSession, UserMount userMount, @DataParam("imageId") Long imageId) {
        ImageMapper mapper = sqlSession.getMapper(ImageMapper.class);
        int res = mapper.deleteImage(imageId, userMount.getUser().getUserId());
        if(res == 1)
            return Code.OK;
        return Code.REQUEST_FAIL;
    }

    @ApiDataMsg("/createChain")
    public JSONObject createChain(SqlSession sqlSession, UserMount userMount, @DataParam("imageId") Long imageId) {
        ImageMapper mapper = sqlSession.getMapper(ImageMapper.class);
        ImageEntity imageEntity = mapper.selectImageOnce(imageId);
        //TODO: 做一次鉴权
        ChainEntity chainEntity = new ChainEntity();
        chainEntity.setChainId(YitIdHelper.nextId());
        String chainAddr = UUID.randomUUID().toString();
        chainEntity.setChainAddr(chainAddr);
        chainEntity.setImageId(imageId);
        chainEntity.setDelete(false);
        mapper.insertChain(chainEntity);
        JSONObject result = new JSONObject();
        String imageUrl = httpUrl + chainAddr;
        String thumUrl = imageUrl + "?thumbnail";
        result.put("imageUrl", imageUrl);
        result.put("thumUrl", thumUrl);
        return result;
    }

    @ApiBinMsg("/uploadImage")
    public Code uploadImage(OOSService oosService, SqlSession sqlSession, byte[] image, UserMount userMount,
                            @DataParam("name") String name, @DataParam("tag") String tag, @DataParam("type") String type) throws IOException {

        long id = YitIdHelper.nextId();
        long imageId = YitIdHelper.nextId();
        ImageMapper mapper = sqlSession.getMapper(ImageMapper.class);
        LocalDateTime nowLocalDateTime = LocalDateTime.now();
        String path = nowLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy.MM/dd HH.mm ")) + id + "."+type;
        oosService.upload(image, path);

        BufferedImage bi = ImageIO.read(new ByteArrayInputStream(image));
        FileEntity file = new FileEntity();
        file.setFileId(id);
        file.setImageId(imageId);
        file.setPath(path);
        file.setSize(image.length);
        file.setImageWidth(bi.getWidth());
        file.setImageLength(bi.getHeight());
        file.setUploadPath(path);
        mapper.insertFile(file);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        BufferedImage bufferedImage = Thumbnails.of(bi)
                .size(280, 280).outputFormat(type).asBufferedImage();
        if (!ImageIO.write(bufferedImage, type, os)) {
            type = "png";
            if (!ImageIO.write(bufferedImage, type, os)) {
                return Code.REQUEST_FAIL;
            }
        }

        byte[] thumImg = os.toByteArray();
        bi = ImageIO.read(new ByteArrayInputStream(thumImg));
        long thumId = YitIdHelper.nextId();
        nowLocalDateTime = LocalDateTime.now();
        path = nowLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy.MM/dd HH.mm ")) + thumId + "."+type;
        oosService.upload(thumImg, path);
        file.setFileId(thumId);
        file.setImageId(imageId);
        file.setPath(path);
        file.setSize(thumImg.length);
        file.setImageWidth(bi.getWidth());
        file.setImageLength(bi.getHeight());
        file.setUploadPath(path);
        mapper.insertFile(file);

        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setImageId(imageId);
        imageEntity.setUserId(userMount.getUser().getUserId());
        imageEntity.setThumbnailNum(thumId);
        imageEntity.setImageNum(id);
        imageEntity.setName(name);
        imageEntity.setTag(tag);
        imageEntity.setEnable(true);
        imageEntity.setDelete(false);
        mapper.insertImage(imageEntity);

        return Code.OK;
    }

}
