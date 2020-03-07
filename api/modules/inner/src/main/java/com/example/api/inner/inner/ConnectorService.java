
package com.example.api.inner.inner;

import com.example.proto.inner.inner.Inner.GetNodeAddresssReq;
import com.example.proto.inner.inner.Inner.GetNodeAddresssResp;

/**
 * @Type ConnectorService.java
 * @Desc The ConnectorService
 * @author jay
 * @date  12:00:00
 * @version
 */
public interface ConnectorService {

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
 * 2019-08-08 jay create
 */
