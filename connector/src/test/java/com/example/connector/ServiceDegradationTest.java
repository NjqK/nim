package com.example.connector;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.registry.Registry;
import org.apache.dubbo.registry.RegistryFactory;

/**
 * @author kuro
 * @version V1.0
 * @date 2020-04-28 10:52 AM
 **/
public class ServiceDegradationTest {
    public static void main(String[] args) {
        RegistryFactory registryFactory = ExtensionLoader.getExtensionLoader(RegistryFactory.class).getAdaptiveExtension();
        Registry registry = registryFactory.getRegistry(URL.valueOf("zookeeper://192.168.0.108:2181"));
        String ip = "0.0.0.0";
        String service = "com.example.api.inner.inner.ConnectorService";
        String rule = URL.encode("host != 192.168.0.106");
        String version = "1.0.0";
        URL url = URL.valueOf("condition://" + ip + "/" + service
                + "?category=routers&dynamic=false&version=" + version
                + "&rule=" + rule);
        System.out.println(url);
        //registry.register(url);
        registry.unregister(url);
    }
}
