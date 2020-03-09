package com.example.chat.entity.dto;

import com.example.proto.common.common.Common;
import lombok.Data;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-9 下午2:35
 **/
@Data
public class MsgInfoDto {

    private Long guid;
    private Long toUid;
    private Long fromUid;
    private Common.Msg msg;
}
