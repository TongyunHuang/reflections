package sun.net.www.protocol.Vfs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Issue: https://github.com/ronmamo/reflections/issues/338
 * Custom url protocol for vfs
 */
public class VfsConnection extends URLConnection{
    /**
     * @param u url specified
     */
    //CS427 Issue link: https://github.com/ronmamo/reflections/issues/338
    public VfsConnection(URL u){
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
        throw new UnsupportedEncodingException(
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
