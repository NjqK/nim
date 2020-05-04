package com.example.chat.dao.manager.impl;

import com.example.chat.dao.manager.MsgInfoDaoManager;
import com.example.chat.dao.mappers.MsgInfoMapper;
import com.example.chat.entity.domain.MsgInfoMongo;
import com.example.chat.entity.dto.MsgInfoDto;
import com.example.chat.entity.example.MsgInfoExample;
import com.example.common.IsDeleteEnum;
import com.example.common.util.ListUtil;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

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

    @Autowired
    private MongoTemplate mongoTemplate;

    public MsgInfoExample.Criteria getCriteria(MsgInfoExample example) {
        MsgInfoExample.Criteria criteria = example.createCriteria();
        return criteria.andIsDeleteEqualTo(IsDeleteEnum.NO.getValue());
    }

    @Override
    public long addMsgInfo(MsgInfoDto msgInfo) {
        try {
            MsgInfoMongo msgInfoMongo = msgInfoToMsgInfoMongo(msgInfo);
            mongoTemplate.insert(msgInfoMongo);
            return 1;
        } catch (Exception e) {
            log.error("MsgInfoDaoManagerImpl addMsgInfo occurred error:{}", e);
            return 0;
        }
    }

    @Override
    public long addMsgInfos(List<MsgInfoDto> msgInfos) {
        // TODO 分批次
        List<MsgInfoMongo> list = new ArrayList<>(msgInfos.size());
        for (MsgInfoDto msgInfo : msgInfos) {
            try {
                MsgInfoMongo msgInfoMongo = msgInfoToMsgInfoMongo(msgInfo);
                list.add(msgInfoMongo);
            } catch (Exception e) {
                log.error("MsgInfoDaoManagerImpl addMsgInfo occurred error:{}", e);
                return 0;
            }
        }
        Collection<MsgInfoMongo> saved = mongoTemplate.insertAll(list);
        return saved.size();
    }

    private MsgInfoMongo msgInfoToMsgInfoMongo(MsgInfoDto msgInfo) throws InvalidProtocolBufferException {
        MsgInfoMongo msgDO = new MsgInfoMongo();
        msgDO.setGuid(msgInfo.getGuid());
        msgDO.setFromUid(msgInfo.getFromUid());
        msgDO.setToUid(msgInfo.getToUid());
        String body = JsonFormat.printer().print(msgInfo.getMsgBody());
        msgDO.setMsgData(body);
        msgDO.setMsgType(msgInfo.getMsgType());
        msgDO.setMsgContentType(msgInfo.getMsgContentType());
        msgDO.setSendTime(new Date());
        return msgDO;
    }

    @Override
    public List<MsgInfoMongo> getUnreadMsg(long uid, long maxGuid) {
        Query query = new Query();
        query.fields().include("fromUid");
        query.fields().include("msgType");
        query.fields().include("msgContentType");
        query.fields().include("msgData");
        query.fields().include("guid");
        query.fields().include("sendTime");
        query.addCriteria(Criteria.where("toUid").is(uid));
        query.addCriteria(Criteria.where("guid").gt(maxGuid));
        List<MsgInfoMongo> msgInfoMongoList = mongoTemplate.find(query, MsgInfoMongo.class);
        if (ListUtil.isEmpty(msgInfoMongoList)) {
            return Collections.EMPTY_LIST;
        }
        return msgInfoMongoList;
    }
}
