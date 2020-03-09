package com.example.chat.dao.manager;

import com.example.chat.entity.domain.MsgInfo;
import com.example.chat.entity.dto.MsgInfoDto;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-9 下午2:29
 **/
public interface MsgInfoDaoManager {
    /**
     * 添加消息记录
     * @param msgInfo
     * @return
     */
    long addMsgInfo(MsgInfoDto msgInfo);

    /**
     * 通过guid找消息
     * @param guid
     * @return
     */
    MsgInfo getMsgById(long guid);
}
