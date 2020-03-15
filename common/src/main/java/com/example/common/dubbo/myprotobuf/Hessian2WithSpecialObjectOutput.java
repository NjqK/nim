package com.example.common.dubbo.myprotobuf;

import org.apache.dubbo.common.serialize.hessian2.Hessian2ObjectOutput;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link org.apache.dubbo.serialize.hessian}
 * @author kuro
 * @version v1.0
 * @date 20-3-14 下午3:37
 **/
public class Hessian2WithSpecialObjectOutput extends Hessian2ObjectOutput {

    private List<SpecialSerializeFactory> factoryList;

    /**
     * Instantiates a new hessian2 with special object output.
     *
     * @param os the os
     */
    public Hessian2WithSpecialObjectOutput(OutputStream os) {
        super(os);
    }

    /**
     * Instantiates a new hessian2 with special object output.
     *
     * @param os the os
     * @param factories the factories
     */
    public Hessian2WithSpecialObjectOutput(OutputStream os, SpecialSerializeFactory... factories) {
        super(os);
        if (null != factories) {
            factoryList = new ArrayList<SpecialSerializeFactory>();
            for (SpecialSerializeFactory factory : factories) {
                factoryList.add(factory);
            }
        }
    }

    /**
     * Adds the serialize factory.
     *
     * @param factory the factory
     */
    public void addSerializeFactory(SpecialSerializeFactory factory) {
        if (null == factoryList) {
            factoryList = new ArrayList<SpecialSerializeFactory>();
        }
        factoryList.add(factory);
    }

    @Override
    public void writeObject(Object obj) throws IOException {
        if (null != factoryList) {
            for (SpecialSerializeFactory factory : factoryList) {
                if (factory.trySerializeObject(this, obj)) {
                    return;
                }
            }
        }
        // Using hession2 serializing for other object
        super.writeInt(ProtobufUtil.TYPE_NORMAL);
        super.writeObject(obj);
    }

}
