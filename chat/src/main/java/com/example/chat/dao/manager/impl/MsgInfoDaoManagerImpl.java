package com.example.chat.dao.manager.impl;

import com.example.chat.dao.manager.MsgInfoDaoManager;
import com.example.chat.dao.mappers.MsgInfoMapper;
import com.example.chat.entity.domain.MsgInfo;
import com.example.chat.entity.dto.MsgInfoDto;
import com.example.chat.entity.example.MsgInfoExample;
import com.example.common.CommonConstants;
import com.example.common.IsDeleteEnum;
import com.example.common.util.ListUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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

    public MsgInfoExample.Criteria getCriteria(MsgInfoExample example) {
        MsgInfoExample.Criteria criteria = example.createCriteria();
        return criteria.andIsDeleteEqualTo(IsDeleteEnum.NO.getValue());
    }

    @Override
    public long addMsgInfo(MsgInfoDto msgInfo) {
        MsgInfo msgDO = new MsgInfo();
        msgDO.setGuid(msgInfo.getGuid());
        msgDO.setFromUid(msgInfo.getFromUid());
        msgDO.setToUid(msgInfo.getToUid());
        msgDO.setMsgData(msgInfo.getMsgBody().toByteArray());
        msgDO.setMsgType(msgInfo.getMsgType());
        msgDO.setMsgContentType(msgInfo.getMsgContentType());
        return msgInfoMapper.insertSelective(msgDO);
    }

    @Override
    public List<MsgInfo> getUnreadMsg(long uid, long maxGuid) {
        MsgInfoExample msgInfoExample = new MsgInfoExample();
        MsgInfoExample.Criteria criteria = getCriteria(msgInfoExample);
        criteria.andToUidEqualTo(uid);
        criteria.andGuidGreaterThan(maxGuid);
        // TODO 考虑客户端分页拉
        List<MsgInfo> msgInfos = msgInfoMapper.selectByExampleSelective(msgInfoExample, MsgInfo.Column.fromUid,
                MsgInfo.Column.msgType, MsgInfo.Column.msgContentType, MsgInfo.Column.msgData, MsgInfo.Column.guid);
        if (!ListUtil.isEmpty(msgInfos)) {
            return msgInfos;
        }
        return null;
    }
}
