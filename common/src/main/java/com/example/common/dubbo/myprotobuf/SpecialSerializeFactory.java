package com.example.common.dubbo.myprotobuf;

import java.io.IOException;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-14 下午3:40
 **/
public interface SpecialSerializeFactory {

    /**
     * 是否支持序列化
     *
     * @param type the type
     * @return true, if successful
     */
    boolean supportDeserialize(int type);

    /**
     * 反序列化
     *
     * @param input the input
     * @return the object
     * @throws IOException I/O exception
     */
    Object parse(Hessian2WithSpecialObjectInput input) throws IOException;

    /**
     * 序列化
     *
     * @param output the output
     * @param obj the obj
     * @return true, if successful
     * @throws IOException I/O exception
     */
    boolean trySerializeObject(Hessian2WithSpecialObjectOutput output, Object obj) throws IOException;
}
