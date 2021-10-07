package cu.kareldv.csv4j.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Clase que especifica donde deberia leerse la tabla
 * @author Karel
 */
public class Source {
    private InputStream input;
    private Reader reader;

    public Source(InputStream input) {
        this.input = input;
        this.reader=new InputStreamReader(input);
    }

    public Source(Reader reader) {
        this.input=null;
        this.reader = reader;
    }

    /**
     * Lee el FileInputStream de {@code file}
     * @throws Exception    Si ocurre un error de IO
     */
    public Source(File file) throws Exception{
        this.input=new FileInputStream(file);
        this.reader=new InputStreamReader(input);
    }
    
    /**
     * Lee el FileInputStream de {@code file}
     * @throws Exception    Si ocurre un error de IO
     */
    public Source(String file) throws Exception{
        this(new File(file));
    }
    
    /**
     * Lee los bytes de {@code data} como un stream
     */
    public Source(byte[] data){
        this.input=new ByteArrayInputStream(data);
        this.reader=new InputStreamReader(input);
    }

    /**
     * Devuelve el Input Stream
     */
    public InputStream getInput() {
        return input;
    }

    /**
     * Devuelve el Reader
     */
    public Reader getReader() {
        return reader;
    }
}
