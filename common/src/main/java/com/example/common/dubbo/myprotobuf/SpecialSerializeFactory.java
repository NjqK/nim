package com.example.common.dubbo.myprotobuf;

import java.io.IOException;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-14 下午3:40
 **/
public interface SpecialSerializeFactory {

    /**
     * Is support deserialize.
     *
     * @param type the type
     * @return true, if successful
     */
    boolean supportDeserialize(int type);

    /**
     * Parses the object
     *
     * @param input the input
     * @return the object
     * @throws IOException Signals that an I/O exception has occurred.
     */
    Object parse(Hessian2WithSpecialObjectInput input) throws IOException;

    /**
     * Try serialize object.
     *
     * @param output the output
     * @param obj the obj
     * @return true, if successful
     * @throws IOException Signals that an I/O exception has occurred.
     */
    boolean trySerializeObject(Hessian2WithSpecialObjectOutput output, Object obj)
            throws IOException;
}
