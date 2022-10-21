package club.hsspace.i2hs.entity;

import java.time.LocalDateTime;

/**
 * @ClassName: ChainEntity
 * @CreateTime: 2022/8/9
 * @Comment: 外链实体
 * @Author: Qing_ning
 * @Mail: 1750359613@qq.com
 */
public class ChainEntity {

    private Long chainId;

    private String chainAddr;

    private Long imageId;

    private Boolean delete;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;

    public Long getChainId() {
        return chainId;
    }

    public void setChainId(Long chainId) {
        this.chainId = chainId;
    }

    public String getChainAddr() {
        return chainAddr;
    }

    public void setChainAddr(String chainAddr) {
        this.chainAddr = chainAddr;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
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
