package com.example.chat.dao.manager;

import com.example.chat.entity.domain.MsgInfo;
import com.example.chat.entity.domain.MsgInfoMongo;
import com.example.chat.entity.dto.MsgInfoDto;

import java.util.List;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-9 下午2:29
 **/
public interface MsgInfoDaoManager {
    /**
     * 添加消息记录
     *
     * @param msgInfo
     * @return
     */
    long addMsgInfo(MsgInfoDto msgInfo);

    /**
     * 批量添加消息记录
     *
     * @param msgInfos
     * @return
     */
    long addMsgInfos(List<MsgInfoDto> msgInfos);

    /**
     * 通过guid找消息
     *
     * @param uid     用户id
     * @param maxGuid 最大的guid
     * @return 返回用户的所有msgId比maxGuid大的消息
     */
    List<MsgInfoMongo> getUnreadMsg(long uid, long maxGuid);
}
