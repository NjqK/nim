
package com.example.api.inner.inner;

import com.example.proto.inner.inner.Inner.GetNodeAddresssReq;
import com.example.proto.inner.inner.Inner.GetNodeAddresssResp;
import com.example.proto.inner.inner.Inner.RemoveConnectionsReq;
import com.example.proto.inner.inner.Inner.RemoveConnectionsResp;

/**
 * @Type ConnectorService.java
 * @Desc The ConnectorService
 * @author kuro
 * @date  12:00:00
 * @version
 */
public interface ConnectorService {

    /**
     * 迁移服务器长连接
     *
     * @param removeConnectionsReq the RemoveConnectionsReq
     * @return RemoveConnectionsResp
     */
    RemoveConnectionsResp removeConnections(RemoveConnectionsReq removeConnectionsReq);

    /**
     * 获取可用的connector节点信息
     *
     * @param getNodeAddresssReq the GetNodeAddresssReq
     * @return GetNodeAddresssResp
     */
    GetNodeAddresssResp getNodeAddress(GetNodeAddresssReq getNodeAddresssReq);

}

/**
 * Revision history
 * -------------------------------------------------------------------------
 *
 * Date Author Note
 * -------------------------------------------------------------------------
 * 2020-03-08 kuro create
 */
