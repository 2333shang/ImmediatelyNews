package com.shang.immediatelynews.entities;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author shang
 * @since 2020-03-28
 */
public class Top implements Serializable{

    private static final long serialVersionUID=1L;

    private String id;

    /**
     * 新闻ID
     */
    private String newsId;

    /**
     * 是否轮播新闻(0是普通头条，1是轮播新闻)
     */
    private String resousel;

    /**
     * 上头条时间
     */
    private Date topTime;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 更新时间
     */
    private Date updatetime;

    /**
     * 创建人
     */
    private String creater;

    /**
     * 更新人
     */
    private String updater;

    /**
     * 申请状态(0为申请失败，1为等待申请，2为申请成功)
     */
    private String applyStatus;

    /**
     * 关联的新闻实体
     */
    private Content content;

    public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getResousel() {
        return resousel;
    }

    public void setResousel(String resousel) {
        this.resousel = resousel;
    }

    public Date getTopTime() {
        return topTime;
    }

    public void setTopTime(Date topTime) {
        this.topTime = topTime;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public String getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(String applyStatus) {
        this.applyStatus = applyStatus;
    }
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Top other = (Top) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
    public String toString() {
        return "Top{" +
        "id=" + id +
        ", newsId=" + newsId +
        ", resousel=" + resousel +
        ", topTime=" + topTime +
        ", createtime=" + createtime +
        ", updatetime=" + updatetime +
        ", creater=" + creater +
        ", updater=" + updater +
        ", applyStatus=" + applyStatus +
        "}";
    }
    
}
