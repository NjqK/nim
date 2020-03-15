package com.example.common.dubbo.myprotobuf;

import org.apache.dubbo.common.serialize.hessian2.Hessian2ObjectInput;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * {@link org.apache.dubbo.serialize.hessian}
 * @author kuro
 * @version v1.0
 * @date 20-3-14 下午3:37
 **/
public class Hessian2WithSpecialObjectInput extends Hessian2ObjectInput {

    private List<SpecialSerializeFactory> factoryList;

    public Hessian2WithSpecialObjectInput(InputStream is) {
        super(is);
    }

    /**
     * Instantiates a new hessian2 with special object input.
     *
     * @param is the is
     * @param factories the factories
     */
    public Hessian2WithSpecialObjectInput(InputStream is, SpecialSerializeFactory... factories) {
        super(is);
        if (null != factories) {
            factoryList = new ArrayList<SpecialSerializeFactory>();
            factoryList.addAll(Arrays.asList(factories));
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
    public Object readObject() throws IOException {
        // get the object tag
        int type = super.readInt();
        if (null != factoryList) {
            for (SpecialSerializeFactory factory : factoryList) {
                if (factory.supportDeserialize(type)) {
                    return factory.parse(this);
                }
            }
        }
        return super.readObject();
    }

    @Override
    public <T> T readObject(Class<T> cls) throws IOException, ClassNotFoundException {
        return (T) readObject();
    }

    @Override
    public <T> T readObject(Class<T> cls, Type type) throws IOException, ClassNotFoundException {
        return (T) readObject();
    }
}
