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
public class User implements Serializable{

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realname;

    /**
     * 性别
     */
    private String gender;

    /**
     * 生日
     */
    private Date birthdate;

    /**
     * 所属公司ID
     */
    private String companyId;
    
    /**
     * 所属公司名称
     */
    private String companyName;

    /**
     * 职务(0为普通员工，1为管理员)
     */
    private String status;

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
     * 0:登陆失败
     * 1:登陆成功
     */
    private String loginMessage;

    public Attachment headIcon;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getLoginMessage() {
		return loginMessage;
	}

	public void setLoginMessage(String loginMessage) {
		this.loginMessage = loginMessage;
	}

	public Attachment getHeadIcon() {
		return headIcon;
	}

	public void setHeadIcon(Attachment headIcon) {
		this.headIcon = headIcon;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Override
    public String toString() {
        return "User{" +
        "id=" + id +
        ", username=" + username +
        ", password=" + password +
        ", realname=" + realname +
        ", gender=" + gender +
        ", birthdate=" + birthdate +
        ", companyId=" + companyId +
        ", companyName=" + companyName +
        ", status=" + status +
        ", createtime=" + createtime +
        ", updatetime=" + updatetime +
        ", creater=" + creater +
        ", updater=" + updater +
        ", deleteFlag=" + deleteFlag +
        ", headIcon=" + headIcon +
        "}";
    }
}
