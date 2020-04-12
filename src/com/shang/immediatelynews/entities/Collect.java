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
public class Collect implements Serializable{

    private static final long serialVersionUID=1L;

    private String id;

    /**
     * 新闻/视频ID
     */
    private String relatedId;

    /**
     * 关联用户
     */
    private String relationUser;

    /**
     * 收藏时间
     */
    private Date collectTime;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 更新时间
     */
    private Date updatetime;

    /**
     * 是否收藏(0是不收藏，1是收藏)
     */
    private String collectFlag;

    //关联的新闻实体
    private Content content;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(String relatedId) {
        this.relatedId = relatedId;
    }

    public String getRelationUser() {
        return relationUser;
    }

    public void setRelationUser(String relationUser) {
        this.relationUser = relationUser;
    }

    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
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

    public String getCollectFlag() {
        return collectFlag;
    }

    public void setCollectFlag(String collectFlag) {
        this.collectFlag = collectFlag;
    }

    public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	@Override
    public String toString() {
        return "Collect{" +
        "id=" + id +
        ", relatedId=" + relatedId +
        ", relationUser=" + relationUser +
        ", collectTime=" + collectTime +
        ", createtime=" + createtime +
        ", updatetime=" + updatetime +
        ", collectFlag=" + collectFlag +
        ", content=" + content +
        "}";
    }
}
