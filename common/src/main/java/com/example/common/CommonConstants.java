package com.example.common;

import com.example.proto.common.common.Common;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-9 下午1:22
 **/
public class CommonConstants {

    public static final Common.ErrorMsg SUCCESS = Common.ErrorMsg.newBuilder()
            .setErrorCode(Common.ErrCode.SUCCESS)
            .setMsg("SUCCESS").build();

    public static final Common.ErrorMsg FAIL = Common.ErrorMsg.newBuilder()
            .setErrorCode(Common.ErrCode.FAIL)
            .setMsg("FAIL").build();
}