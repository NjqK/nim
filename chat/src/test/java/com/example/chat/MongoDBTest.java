package com.example.chat;

import com.example.chat.entity.domain.MsgInfoMongo;
import com.example.proto.common.common.Common;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={ChatApplication.class})// 指定启动类
public class MongoDBTest {

    @Autowired
    private MongoTemplate mongoTemplate;


    /**
     * 查询所有信息
     */
    @Test
    public void findAll() throws InvalidProtocolBufferException {
        MsgInfoMongo msgInfoMongo = new MsgInfoMongo();
        msgInfoMongo.setGuid(11L);
        msgInfoMongo.setFromUid(21L);
        msgInfoMongo.setToUid(55L);
        msgInfoMongo.setMsgContentType(Common.MsgContentType.TEXT_VALUE);
        msgInfoMongo.setMsgType(Common.MsgType.BYE_VALUE);

        Common.Body msg = Common.Body.newBuilder().setContent("msgsmg").build();
        String print = JsonFormat.printer().print(msg);
        msgInfoMongo.setMsgData(print);
        mongoTemplate.insert(msgInfoMongo);
    }

    /**
     * 新增信息
     */
    @Test
    public void save() {

    }

    /**
     * 修改信息
     */
    @Test
    public void update() {

    }

    /**
     * 删除信息
     */
    @Test
    public void delete() {
    }
}