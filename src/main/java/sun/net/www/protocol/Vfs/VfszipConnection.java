package sun.net.www.protocol.Vfs;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Custom url protocol for vfszip
 */
public class VfszipConnection extends URLConnection{
    /**
     * @param u url specified
     */
    //CS427 Issue link: https://github.com/ronmamo/reflections/issues/338
    public VfszipConnection(URL u){
        super(u);
    }

    /**
     * @throws IOException
     */
    //CS427 Issue link: https://github.com/ronmamo/reflections/issues/338
    @Override
    public void connect() throws IOException{
        throw new UnsupportedOperationException(
                "The connect() method is not supported"
        );
    }

    /**
     * @throws IOException
     */
    //CS427 Issue link: https://github.com/ronmamo/reflections/issues/338
    @Override
    public Object getContent() throws IOException{
        throw new UnsupportedOperationException(
                "The getContent() method is not supported"
        );
    }

    /**
     * @throws IOException
     */
    //CS427 Issue link: https://github.com/ronmamo/reflections/issues/338
    @Override
    public InputStream getInputStream() throws IOException {
        throw new UnsupportedEncodingException(
                "The getInputStream() method is not supported"
        );
    }




}
