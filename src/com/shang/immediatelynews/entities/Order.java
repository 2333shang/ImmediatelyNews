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
public class Order implements Serializable{

    private static final long serialVersionUID=1L;

    private String id;

    /**
     * 关注的公司ID
     */
    private String orderCompany;

    /**
     * 关注人ID
     */
    private String orderUser;

    /**
     * 关注时间
     */
    private Date orderTime;

    /**
     * 是否关注(0是不关注，1是关注)
     */
    private String orderFlag;

    /**
     * 创建时间
     */
    private Date createtime;
    
    private Company company;
    /**
     * 更新时间
     */
    private Date updatetime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderCompany() {
        return orderCompany;
    }

    public void setOrderCompany(String orderCompany) {
        this.orderCompany = orderCompany;
    }

    public String getOrderUser() {
        return orderUser;
    }

    public void setOrderUser(String orderUser) {
        this.orderUser = orderUser;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderFlag() {
        return orderFlag;
    }

    public void setOrderFlag(String orderFlag) {
        this.orderFlag = orderFlag;
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
    
    public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Override
    public String toString() {
        return "Order{" +
        "id=" + id +
        ", orderCompany=" + orderCompany +
        ", orderUser=" + orderUser +
        ", orderTime=" + orderTime +
        ", orderFlag=" + orderFlag +
        ", createtime=" + createtime +
        ", updatetime=" + updatetime +
        ", company=" + company +
        "}";
    }
}
