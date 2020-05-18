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
public class Company implements Serializable{

    private static final long serialVersionUID=1L;

    /**
     * 公司ID
     */
    private String id;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 上级公司ID
     */
    private String parentId;

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
     * 是否删除
     */
    private String deleteFlag;

    private List<Content> content;
    
    private List<Content> videoContent;
    
    private Attachment headIcon;

    private Order order;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public List<Content> getContent() {
		return content;
	}

	public void setContent(List<Content> content) {
		this.content = content;
	}

	public List<Content> getVideoContent() {
		return videoContent;
	}

	public void setVideoContent(List<Content> videoContent) {
		this.videoContent = videoContent;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Attachment getHeadIcon() {
		return headIcon;
	}

	public void setHeadIcon(Attachment headIcon) {
		this.headIcon = headIcon;
	}
	@Override
    public String toString() {
        return "Company{" +
        "id=" + id +
        ", companyName=" + companyName +
        ", parentId=" + parentId +
        ", createtime=" + createtime +
        ", updatetime=" + updatetime +
        ", creater=" + creater +
        ", updater=" + updater +
        ", deleteFlag=" + deleteFlag +
        ", content=" + content +
        ", videoContent=" + videoContent +
        "}";
    }
}
