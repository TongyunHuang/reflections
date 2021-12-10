package protocol.Vfs;

import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

/**
 * Implement URLStreamHandlerFactory
 * Custom url protocol factory for vfszip, vfsfile
 */
public class CustomURLSteamHandlerFactory implements URLStreamHandlerFactory {
    //CS427 Issue link: https://github.com/ronmamo/reflections/issues/338

    /**
     * Handle url stream base on protocol type
     * @param protocol protocol type
     * @return corresponding handler
     */
    @Override
    public URLStreamHandler createURLStreamHandler(String protocol) {
        if ("vfszip".equals(protocol) || "vfsfile".equals(protocol)) {
            return new VfszipHandler();
        }
        if ("vfs".equals(protocol)) {
            return new VfsHandler();
        }
        if ("bundle".equals(protocol)) {
            return new BundleHandler();
        }
        return null;
    }
}
