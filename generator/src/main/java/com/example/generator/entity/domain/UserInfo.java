package com.example.generator.entity.domain;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table t_user_info
 */
public class UserInfo implements Serializable {
    /**
     * Database Column Remarks:
     *   主键ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user_info.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * Database Column Remarks:
     *   用户id
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user_info.uid
     *
     * @mbg.generated
     */
    private Long uid;

    /**
     * Database Column Remarks:
     *   用户名字
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user_info.u_name
     *
     * @mbg.generated
     */
    private String uName;

    /**
     * Database Column Remarks:
     *   0未删除 1已删除
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user_info.is_delete
     *
     * @mbg.generated
     */
    private Boolean isDelete;

    /**
     * Database Column Remarks:
     *   创建时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user_info.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     * Database Column Remarks:
     *   修改时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user_info.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_user_info
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user_info.id
     *
     * @return the value of t_user_info.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user_info.id
     *
     * @param id the value for t_user_info.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user_info.uid
     *
     * @return the value of t_user_info.uid
     *
     * @mbg.generated
     */
    public Long getUid() {
        return uid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user_info.uid
     *
     * @param uid the value for t_user_info.uid
     *
     * @mbg.generated
     */
    public void setUid(Long uid) {
        this.uid = uid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user_info.u_name
     *
     * @return the value of t_user_info.u_name
     *
     * @mbg.generated
     */
    public String getuName() {
        return uName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user_info.u_name
     *
     * @param uName the value for t_user_info.u_name
     *
     * @mbg.generated
     */
    public void setuName(String uName) {
        this.uName = uName == null ? null : uName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user_info.is_delete
     *
     * @return the value of t_user_info.is_delete
     *
     * @mbg.generated
     */
    public Boolean getIsDelete() {
        return isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user_info.is_delete
     *
     * @param isDelete the value for t_user_info.is_delete
     *
     * @mbg.generated
     */
    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user_info.create_time
     *
     * @return the value of t_user_info.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user_info.create_time
     *
     * @param createTime the value for t_user_info.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user_info.update_time
     *
     * @return the value of t_user_info.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user_info.update_time
     *
     * @param updateTime the value for t_user_info.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}