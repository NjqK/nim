package com.example.chat.dao.manager.impl;

import com.example.chat.dao.manager.MsgInfoDaoManager;
import com.example.chat.dao.mappers.MsgInfoMapper;
import com.example.chat.entity.domain.MsgInfo;
import com.example.chat.entity.dto.MsgInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-9 下午2:34
 **/
@Slf4j
@Component
public class MsgInfoDaoManagerImpl implements MsgInfoDaoManager {

    @Resource
    private MsgInfoMapper msgInfoMapper;

    @Override
    public long addMsgInfo(MsgInfoDto msgInfo) {
        // TODO 插入数据
        MsgInfo msgDO = new MsgInfo();
        msgDO.setGuid(msgInfo.getGuid());
        msgDO.setFromUid(msgInfo.getFromUid());
        msgDO.setToUid(msgInfo.getToUid());
        msgDO.setMsgData(msgInfo.getMsg().toByteArray());
        return 0;
    }
}
