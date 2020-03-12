package com.example.chat.dao.manager.impl;

import com.example.chat.dao.manager.MsgReadDaoManager;
import com.example.chat.dao.mappers.MsgReadMapper;
import com.example.chat.entity.domain.MsgRead;
import com.example.chat.entity.example.MsgReadExample;
import com.example.common.IsDeleteEnum;
import com.example.common.util.ListUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-12 下午5:54
 **/
@Slf4j
@Component
public class MsgReadDaoManagerImpl implements MsgReadDaoManager {

    @Resource
    private MsgReadMapper msgReadMapper;

    public MsgReadExample.Criteria getCriteria(MsgReadExample example) {
        MsgReadExample.Criteria criteria = example.createCriteria();
        return criteria.andIsDeleteEqualTo(IsDeleteEnum.NO.getValue());
    }

    @Override
    public long getMaxGuid(long uid) {
        MsgReadExample msgReadExample = new MsgReadExample();
        MsgReadExample.Criteria criteria = getCriteria(msgReadExample);
        criteria.andUidEqualTo(uid);
        msgReadExample.setOrderByClause("guid desc");
        // TODO limit 1条
        List<MsgRead> msgReads = msgReadMapper.selectByExample(msgReadExample);
        if (ListUtil.isEmpty(msgReads)) {
            return 0L;
        }
        return msgReads.get(0).getGuid();
    }
}
