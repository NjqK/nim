
package com.example.api.inner.inner;

import com.example.proto.inner.inner.Inner.RouteMsgReq;
import com.example.proto.inner.inner.Inner.RouteMsgResp;

/**
 * @Type PushService.java
 * @Desc The PushService
 * @author kuro
 * @date  12:00:00
 * @version
 */
public interface PushService {

    /**
     * 找到用户所在netty节点，转发消息
     *
     * @param routeMsgReq the RouteMsgReq
     * @return RouteMsgResp
     */
    RouteMsgResp routeMsg(RouteMsgReq routeMsgReq);

}

/**
 * Revision history
 * -------------------------------------------------------------------------
 *
 * Date Author Note
 * -------------------------------------------------------------------------
 * 2020-03-08 kuro create
 */
