package com.example.chat.entity.domain;

import java.util.Date;

public class MsgInfo {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_msg_info.id
     *
     * @mbg.generated Mon Mar 09 14:43:26 CST 2020
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_msg_info.guid
     *
     * @mbg.generated Mon Mar 09 14:43:26 CST 2020
     */
    private Long guid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_msg_info.to_uid
     *
     * @mbg.generated Mon Mar 09 14:43:26 CST 2020
     */
    private Long toUid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_msg_info.from_uid
     *
     * @mbg.generated Mon Mar 09 14:43:26 CST 2020
     */
    private Long fromUid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_msg_info.is_delete
     *
     * @mbg.generated Mon Mar 09 14:43:26 CST 2020
     */
    private Boolean isDelete;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_msg_info.create_time
     *
     * @mbg.generated Mon Mar 09 14:43:26 CST 2020
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_msg_info.update_time
     *
     * @mbg.generated Mon Mar 09 14:43:26 CST 2020
     */
    private Date updateTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_msg_info.msg_data
     *
     * @mbg.generated Mon Mar 09 14:43:26 CST 2020
     */
    private byte[] msgData;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_msg_info.id
     *
     * @return the value of t_msg_info.id
     *
     * @mbg.generated Mon Mar 09 14:43:26 CST 2020
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_msg_info.id
     *
     * @param id the value for t_msg_info.id
     *
     * @mbg.generated Mon Mar 09 14:43:26 CST 2020
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_msg_info.guid
     *
     * @return the value of t_msg_info.guid
     *
     * @mbg.generated Mon Mar 09 14:43:26 CST 2020
     */
    public Long getGuid() {
        return guid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_msg_info.guid
     *
     * @param guid the value for t_msg_info.guid
     *
     * @mbg.generated Mon Mar 09 14:43:26 CST 2020
     */
    public void setGuid(Long guid) {
        this.guid = guid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_msg_info.to_uid
     *
     * @return the value of t_msg_info.to_uid
     *
     * @mbg.generated Mon Mar 09 14:43:26 CST 2020
     */
    public Long getToUid() {
        return toUid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_msg_info.to_uid
     *
     * @param toUid the value for t_msg_info.to_uid
     *
     * @mbg.generated Mon Mar 09 14:43:26 CST 2020
     */
    public void setToUid(Long toUid) {
        this.toUid = toUid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_msg_info.from_uid
     *
     * @return the value of t_msg_info.from_uid
     *
     * @mbg.generated Mon Mar 09 14:43:26 CST 2020
     */
    public Long getFromUid() {
        return fromUid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_msg_info.from_uid
     *
     * @param fromUid the value for t_msg_info.from_uid
     *
     * @mbg.generated Mon Mar 09 14:43:26 CST 2020
     */
    public void setFromUid(Long fromUid) {
        this.fromUid = fromUid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_msg_info.is_delete
     *
     * @return the value of t_msg_info.is_delete
     *
     * @mbg.generated Mon Mar 09 14:43:26 CST 2020
     */
    public Boolean getIsDelete() {
        return isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_msg_info.is_delete
     *
     * @param isDelete the value for t_msg_info.is_delete
     *
     * @mbg.generated Mon Mar 09 14:43:26 CST 2020
     */
    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_msg_info.create_time
     *
     * @return the value of t_msg_info.create_time
     *
     * @mbg.generated Mon Mar 09 14:43:26 CST 2020
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_msg_info.create_time
     *
     * @param createTime the value for t_msg_info.create_time
     *
     * @mbg.generated Mon Mar 09 14:43:26 CST 2020
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_msg_info.update_time
     *
     * @return the value of t_msg_info.update_time
     *
     * @mbg.generated Mon Mar 09 14:43:26 CST 2020
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_msg_info.update_time
     *
     * @param updateTime the value for t_msg_info.update_time
     *
     * @mbg.generated Mon Mar 09 14:43:26 CST 2020
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_msg_info.msg_data
     *
     * @return the value of t_msg_info.msg_data
     *
     * @mbg.generated Mon Mar 09 14:43:26 CST 2020
     */
    public byte[] getMsgData() {
        return msgData;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_msg_info.msg_data
     *
     * @param msgData the value for t_msg_info.msg_data
     *
     * @mbg.generated Mon Mar 09 14:43:26 CST 2020
     */
    public void setMsgData(byte[] msgData) {
        this.msgData = msgData;
    }
}