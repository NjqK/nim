package com.example.common.dubbo.myprotobuf;

import com.google.protobuf.MessageLite;

import java.io.IOException;

/**
 * {@link org.apache.dubbo.serialize.hessian}
 * @author kuro
 * @version v1.0
 * @date 20-3-14 下午3:38
 **/
public class ProtobufSerializeFactory implements SpecialSerializeFactory {

    public static final ProtobufSerializeFactory INSTANCE = new ProtobufSerializeFactory();

    @Override
    public boolean supportDeserialize(int type) {
        if (type == ProtobufUtil.TYPE_PROTOBUF) {
            return true;
        }
        return false;
    }

    @Override
    public Object parse(Hessian2WithSpecialObjectInput input) throws IOException {
        String className = input.readUTF();
        byte[] data = input.readBytes();
        return ProtobufUtil.parseFrom(className, data);
    }

    @Override
    public boolean trySerializeObject(Hessian2WithSpecialObjectOutput output, Object obj)
            throws IOException {
        int type = ProtobufUtil.TYPE_NORMAL;
        boolean isMessageLiteBuilder = false;
        if (obj instanceof MessageLite.Builder) {
            isMessageLiteBuilder = true;
            type = ProtobufUtil.TYPE_PROTOBUF;
        } else if (obj instanceof MessageLite) {
            type = ProtobufUtil.TYPE_PROTOBUF;
        }
        if (type == ProtobufUtil.TYPE_PROTOBUF) {
            // 反序列化的时候用
            output.writeInt(ProtobufUtil.TYPE_PROTOBUF);
            // 类名
            output.writeUTF(obj.getClass().getName());
            if (isMessageLiteBuilder) {
                output.writeBytes(((MessageLite.Builder) obj).build().toByteArray());
            } else {
                output.writeBytes(((MessageLite) obj).toByteArray());
            }
            return true;
        }
        return false;
    }

}