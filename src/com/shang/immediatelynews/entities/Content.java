package com.shang.immediatelynews.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author shang
 * @since 2020-03-14
 */
public class Content implements Serializable{

    private static final long serialVersionUID=1L;

    /**
     * 新闻编号
     */
    private String id;

    /**
     * 新闻标题
     */
    private String title;

    /**
     * 撰稿人ID
     */
    private String authorId;
    
    /**
     * 撰稿人姓名
     */
    private String authorName;

    /**
     * 发送时间
     */
    private Date sendtime;

    /**
     * 文章内容
     */
    private String content;

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
     * 公司ID
     */
    private String companyId;
    
    /**
     * 公司ID
     */
    private String companyName;

    /**
     * 新闻类型(0为文章，1为视频)
     */
    private String newsType;

    /**
     * 新闻链接
     */
    private String newsUrl;

    /**
     * 是否删除
     */
    private String deleteFlag;

    private Collect collect;
    
    private List<Attachment> pics;
    
    private String applyStatus;
    
    private User user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public Date getSendtime() {
        return sendtime;
    }

    public void setSendtime(Date sendtime) {
        this.sendtime = sendtime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getNewsType() {
        return newsType;
    }

    public void setNewsType(String newsType) {
        this.newsType = newsType;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
    
    public List<Attachment> getPics() {
		return pics;
	}

	public void setPics(List<Attachment> pics) {
		this.pics = pics;
	}
	
    public Collect getCollect() {
		return collect;
	}

	public void setCollect(Collect collect) {
		this.collect = collect;
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
		Content other = (Content) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
    public String toString() {
        return "Content{" +
        "id=" + id +
        ", title=" + title +
        ", authorId=" + authorId +
        ", authorName=" + authorName +
        ", sendtime=" + sendtime +
        ", content=" + content +
        ", createtime=" + createtime +
        ", updatetime=" + updatetime +
        ", creater=" + creater +
        ", updater=" + updater +
        ", companyId=" + companyId +
        ", companyName=" + companyName +
        ", newsType=" + newsType +
        ", newsUrl=" + newsUrl +
        ", applyStatus=" + applyStatus +
        ", deleteFlag=" + deleteFlag +
        ", pics=" + pics +
        "}";
    }
}
