package com.shang.immediatelynews.entities;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author shang
 * @since 2020-03-14
 */
public class Attachment implements Serializable{

    private static final long serialVersionUID=1L;

    /**
     * 附件ID
     */
    private String id;

    /**
     * 附件名称
     */
    private String fileName;

    /**
     * 附件路径
     */
    private String filePath;

    /**
     * 附件类型(0为头像，1为新闻图片，2为轮播封面图，3为视频封面)
     */
    private String attachmentType;

    /**
     * 业务主键
     */
    private String businesskey;

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

    /**
     * 上传是否成功(0为失败，1为成功)
     */
    private String uploaded;
    
    /**
     * 附件地址
     */
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public String getBusinesskey() {
        return businesskey;
    }

    public void setBusinesskey(String businesskey) {
        this.businesskey = businesskey;
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

    public String getUploaded() {
		return uploaded;
	}

	public void setUploaded(String uploaded) {
		this.uploaded = uploaded;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
    public String toString() {
        return "Attachment{" +
        "id=" + id +
        ", fileName=" + fileName +
        ", filePath=" + filePath +
        ", attachmentType=" + attachmentType +
        ", businesskey=" + businesskey +
        ", createtime=" + createtime +
        ", updatetime=" + updatetime +
        ", creater=" + creater +
        ", updater=" + updater +
        ", deleteFlag=" + deleteFlag +
        ", uploaded=" + uploaded +
        ", url=" + url +
        "}";
    }
}
