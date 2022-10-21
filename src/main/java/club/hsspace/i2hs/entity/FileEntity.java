package club.hsspace.i2hs.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * @ClassName: FileEntity
 * @CreateTime: 2022/8/1
 * @Comment: 文件实体
 * @Author: Qing_ning
 * @Mail: 1750359613@qq.com
 */
public class FileEntity {

    private Long fileId;

    private Long imageId;

    private String path;

    private Integer size;

    private Integer imageWidth;

    private Integer imageLength;

    private String uploadPath;

    private LocalDateTime createTime;

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imagId) {
        this.imageId = imagId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(Integer imageWidth) {
        this.imageWidth = imageWidth;
    }

    public Integer getImageLength() {
        return imageLength;
    }

    public void setImageLength(Integer imageLength) {
        this.imageLength = imageLength;
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
