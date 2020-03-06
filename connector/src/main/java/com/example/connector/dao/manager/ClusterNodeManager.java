package com.example.connector.dao.manager;

import com.example.connector.entity.cluster.ClusterNode;

/**
 * @author kuro
 * @version V1.0
 * @date 3/6/20 2:56 PM
 **/
public interface ClusterNodeManager {

    ClusterNode getLocalNode();
}