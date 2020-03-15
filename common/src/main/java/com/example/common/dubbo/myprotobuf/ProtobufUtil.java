package com.example.common.dubbo.myprotobuf;

import com.google.protobuf.MessageLite;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-14 下午3:39
 **/
public class ProtobufUtil {

    /**
     * normal java obj
     */
    public static final int TYPE_NORMAL = 0;

    /**
     * protobuf java obj
     */
    public static final int TYPE_PROTOBUF = 1;

    private static final String BUILDER_CLASS_SUFFIX = "$Builder";

    private static final Map<String, MessageLite.Builder> BUILDER_CACHE =
            new ConcurrentHashMap<String, MessageLite.Builder>();

    /**
     * 根据类名转化成对象
     *
     * @param className the class name
     * @param data      the data
     * @return the object
     * @throws IOException I/O exception
     */
    public static Object parseFrom(String className, byte[] data) throws IOException {
        try {
            boolean isBuilderClass = className.endsWith(BUILDER_CLASS_SUFFIX);
            if (isBuilderClass) {
                className = className.substring(0, className.length() - BUILDER_CLASS_SUFFIX.length());
            }
            MessageLite.Builder builder = getPbBuilder(className);
            builder.mergeFrom(data);
            if (isBuilderClass) {
                return builder;
            } else {
                return builder.buildPartial();
            }
        } catch (Exception ex) {
            if (ex instanceof IOException) {
                throw (IOException) ex;
            } else {
                throw new IOException(ex);
            }
        }
    }

    /**
     * 得到pb的Builder
     *
     * @param className the class name
     * @return 返回className类型的pb的Builder
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private static MessageLite.Builder getPbBuilder(String className)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        MessageLite.Builder builder = BUILDER_CACHE.get(className);
        if (null == builder) {
            Class messageClass = Class.forName(className);
            Method newBuilder = messageClass.getMethod("newBuilder");
            // XxxMessage.newBuilder() return XxxMessage.Builder
            builder = (MessageLite.Builder) newBuilder.invoke(null);
            BUILDER_CACHE.put(className, builder);
        }

        return builder.clone();
    }
}
