package com.example.chat.config;

import com.example.chat.entity.domain.MsgInfoMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.stereotype.Component;

@Component
public class MongoConfig {
    @Autowired
    private MongoTemplate template;

    @EventListener(ApplicationReadyEvent.class)
    public void initIndicesAfterStartup() {
        IndexOperations userIndexOps = template.indexOps(MsgInfoMongo.class);
        userIndexOps.ensureIndex(new Index().on("guid", Sort.Direction.ASC).unique());
        userIndexOps.ensureIndex(new Index().on("toUid", Sort.Direction.ASC));
    }
}
