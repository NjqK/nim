package com.example.common.dubbo.myprotobuf;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.serialize.ObjectInput;
import org.apache.dubbo.common.serialize.ObjectOutput;
import org.apache.dubbo.common.serialize.Serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * {@link org.apache.dubbo.serialize.hessian}
 * @author kuro
 * @version v1.0
 * @date 20-3-14 下午3:31
 **/
public class Hessian2WithProtobufSerialization implements Serialization {

    @Override
    public byte getContentTypeId() {
        return 17;
    }

    @Override
    public String getContentType() {
        return "x-application/hessian2-spec";
    }

    @Override
    @Adaptive
    public ObjectOutput serialize(URL url, OutputStream output) throws IOException {
        return new Hessian2WithSpecialObjectOutput(output, ProtobufSerializeFactory.INSTANCE);
    }

    @Override
    @Adaptive
    public ObjectInput deserialize(URL url, InputStream input) throws IOException {
        return new Hessian2WithSpecialObjectInput(input, ProtobufSerializeFactory.INSTANCE);
    }
}