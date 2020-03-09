package com.example.chat.entity.domain;

import java.util.Date;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table t_msg_info
 *
 * @mbg.generated do_not_delete_during_merge
 */
public class MsgInfo {
    /**
     * Database Column Remarks:
     *   主键ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_msg_info.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * Database Column Remarks:
     *   消息id
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_msg_info.guid
     *
     * @mbg.generated
     */
    private Long guid;

    /**
     * Database Column Remarks:
     *   目标用户
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_msg_info.to_uid
     *
     * @mbg.generated
     */
    private Long toUid;

    /**
     * Database Column Remarks:
     *   发送者
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_msg_info.from_uid
     *
     * @mbg.generated
     */
    private Long fromUid;

    /**
     * Database Column Remarks:
     *   消息类型
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_msg_info.msg_type
     *
     * @mbg.generated
     */
    private Integer msgType;

    /**
     * Database Column Remarks:
     *   消息内容类型
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_msg_info.msg_content_type
     *
     * @mbg.generated
     */
    private Integer msgContentType;

    /**
     * Database Column Remarks:
     *   0未删除 1已删除
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_msg_info.is_delete
     *
     * @mbg.generated
     */
    private Boolean isDelete;

    /**
     * Database Column Remarks:
     *   创建时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_msg_info.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     * Database Column Remarks:
     *   更新时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_msg_info.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * Database Column Remarks:
     *   消息数据
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_msg_info.msg_data
     *
     * @mbg.generated
     */
    private byte[] msgData;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_msg_info.id
     *
     * @return the value of t_msg_info.id
     *
     * @mbg.generated
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
     * @mbg.generated
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
     * @mbg.generated
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
     * @mbg.generated
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
     * @mbg.generated
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
     * @mbg.generated
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
     * @mbg.generated
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
     * @mbg.generated
     */
    public void setFromUid(Long fromUid) {
        this.fromUid = fromUid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_msg_info.msg_type
     *
     * @return the value of t_msg_info.msg_type
     *
     * @mbg.generated
     */
    public Integer getMsgType() {
        return msgType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_msg_info.msg_type
     *
     * @param msgType the value for t_msg_info.msg_type
     *
     * @mbg.generated
     */
    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_msg_info.msg_content_type
     *
     * @return the value of t_msg_info.msg_content_type
     *
     * @mbg.generated
     */
    public Integer getMsgContentType() {
        return msgContentType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_msg_info.msg_content_type
     *
     * @param msgContentType the value for t_msg_info.msg_content_type
     *
     * @mbg.generated
     */
    public void setMsgContentType(Integer msgContentType) {
        this.msgContentType = msgContentType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_msg_info.is_delete
     *
     * @return the value of t_msg_info.is_delete
     *
     * @mbg.generated
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
     * @mbg.generated
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
     * @mbg.generated
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
     * @mbg.generated
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
     * @mbg.generated
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
     * @mbg.generated
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
     * @mbg.generated
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
     * @mbg.generated
     */
    public void setMsgData(byte[] msgData) {
        this.msgData = msgData;
    }
}