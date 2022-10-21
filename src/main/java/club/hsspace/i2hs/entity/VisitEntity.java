package club.hsspace.i2hs.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * @ClassName: VisitEntity
 * @CreateTime: 2022/8/16
 * @Comment: 访问日志
 * @Author: Qing_ning
 * @Mail: 1750359613@qq.com
 */
public class VisitEntity {

    private static final Logger logger = LoggerFactory.getLogger(VisitEntity.class);

    private Long logId;

    private String url;

    private boolean success;

    private String ip;

    private Long chainId;

    private LocalDateTime createTime;

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getChainId() {
        return chainId;
    }

    public void setChainId(Long chainId) {
        this.chainId = chainId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
