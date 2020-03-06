
package com.example.api.inner.inner;

import com.example.proto.inner.inner.Inner.GetNodeAddresssReq;
import com.example.proto.inner.inner.Inner.GetNodeAddresssResp;

/**
 * @Type ClusterNodeAddressService.java
 * @Desc The ClusterNodeAddressService
 * @author jay
 * @date  12:00:00
 * @version
 */
public interface ClusterNodeAddressService {

    /**
     * The getNodeAddress
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
