package com.example.connector.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.connector.common.ListUtil;
import com.example.connector.dao.mappers.AppVersionMapper;
import com.example.connector.entity.domain.AppVersion;
import com.example.connector.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class DemoServiceImpl implements DemoService {

    @Resource
    private AppVersionMapper mapper;

    @Override
    public void talk() {
        List<AppVersion> appVersions = mapper.selectAll();
        System.out.println(JSON.toJSONString(appVersions));
        if (ListUtil.isEmpty(appVersions)) {
            AppVersion record = new AppVersion();
            record.setCountry("China");
            record.setAppName("Add");
            record.setVersionCode("11");
            record.setVersionId(1);
            record.setClientType("Android");
            record.setUpdateType("AppStore");
            record.setVersionStatus("1");
            record.setIsCurrentVersion(true);
            record.setIsLowestVersion(true);
            record.setIsDelete(0);
            int insert = mapper.insert(record);
            if (insert != 0) {
                System.out.println("insert successfully");
            }
            appVersions = mapper.selectAll();
            System.out.println(JSON.toJSONString(appVersions));
        }
    }
}
