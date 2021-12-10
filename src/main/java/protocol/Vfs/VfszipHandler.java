package protocol.Vfs;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * Custom url protocol handler for vfszip
 */
public class VfszipHandler extends URLStreamHandler {

    /**
     * @param u url specified
     * @return vfszipConnection - subclass of URLConnection
     * @throws IOException
     */
    //CS427 Issue link: https://github.com/ronmamo/reflections/issues/338
    @Override
    protected  URLConnection openConnection(URL u) throws IOException{
        return new VfszipConnection(u);
    }
}