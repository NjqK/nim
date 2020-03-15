package com.example.connector.netty.ssl;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.security.KeyStore;

/**
 * 双向认证
 *
 * @author kuro
 * @version v1.0
 * @date 20-3-15 下午4:07
 **/
public class SSLContextFactory {

    private static final String PROTOCOL = "TLSv1.2";
    private static final SSLContext SERVER_CONTEXT;

    static {
        SSLContext serverContext = null;
        /**
         * 密码都用了同一个
         */
        String keyStorePassword = "nijiaqi123";
        try {
            // 密钥库
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(SSLContextFactory.class.getClassLoader().getResourceAsStream("ssl/server/kserver.keystore"),
                    keyStorePassword.toCharArray());
            // Set up key manager factory to use our key store
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, keyStorePassword.toCharArray());
            // 受信库
            KeyStore ts = KeyStore.getInstance("JKS");
            ts.load(SSLContextFactory.class.getClassLoader().getResourceAsStream("ssl/server/tserver.keystore"),
                    keyStorePassword.toCharArray());
            // set up trust manager factory to use our trust store
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ts);
            // Initialize the SSLContext to work with our key managers.
            serverContext = SSLContext.getInstance(PROTOCOL);
            serverContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        } catch (Exception e) {
            throw new Error("Failed to initialize the server-side SSLContext", e);
        }
        SERVER_CONTEXT = serverContext;
    }

    public static SSLContext getServerContext() {
        return SERVER_CONTEXT;
    }
}
