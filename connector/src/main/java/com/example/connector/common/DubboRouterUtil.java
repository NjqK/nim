package com.example.connector.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.registry.Registry;
import org.apache.dubbo.registry.RegistryFactory;

/**
 * @author kuro
 * @version V1.0
 * @date 2020-04-28 11:30 AM
 **/
@Slf4j
public class DubboRouterUtil {

    private static volatile DubboRouterUtil INSTANCE;

    private static final RegistryFactory REGISTRY_FACTORY =
            ExtensionLoader.getExtensionLoader(RegistryFactory.class).getAdaptiveExtension();

    private final URL zkUrl;

    private DubboRouterUtil(String url) {
        if (!StringUtils.isEmpty(url)) {
            zkUrl = URL.valueOf("zookeeper://" + url);
        } else {
            log.error(this.getClass().getName()+", url is empty");
            throw new RuntimeException(this.getClass().getName()+", url can not be empty");
        }
    }

    public static void init(String url) {
        if (INSTANCE == null) {
            synchronized (DubboRouterUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DubboRouterUtil(url);
                }
            }
        }
    }

    public static DubboRouterUtil getINSTANCE() {
        return INSTANCE;
    }

    public void addRouteRule(String serviceName, String serviceVersion, String routeRule) {
        Registry registry = REGISTRY_FACTORY.getRegistry(zkUrl);
        URL url = URL.valueOf("condition://0.0.0.0/" + serviceName
                + "?category=routers&dynamic=false&version=" + serviceVersion
                + "&rule=" + routeRule);
        registry.register(url);
    }

    public void deleteRouteRule(String ip, String serviceName, String serviceVersion, String routeRule) {
        Registry registry = REGISTRY_FACTORY.getRegistry(zkUrl);
        URL url = URL.valueOf("condition://0.0.0.0/" +  serviceName
                + "?category=routers&dynamic=false&version=" + serviceVersion
                + "&rule=" + routeRule);
        registry.unregister(url);
    }
}
