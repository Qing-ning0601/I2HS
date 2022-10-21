package club.hsspace.i2hs.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * @ClassName: ImageEntity
 * @CreateTime: 2022/8/1
 * @Comment: 图片实体
 * @Author: Qing_ning
 * @Mail: 1750359613@qq.com
 */
public class ImageEntity {

    private Long imageId;

    private Long userId;

    private Long thumbnailNum;

    private Long imageNum;

    private String name;

    private String tag;

    private boolean enable;

    private boolean delete;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getThumbnailNum() {
        return thumbnailNum;
    }

    public void setThumbnailNum(Long thumbnailNum) {
        this.thumbnailNum = thumbnailNum;
    }

    public Long getImageNum() {
        return imageNum;
    }

    public void setImageNum(Long imageNum) {
        this.imageNum = imageNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
