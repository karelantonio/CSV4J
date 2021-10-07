package cu.kareldv.csv4j.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Clase que especifica donde debe escribirse la tabla
 * @author Karel
 */
public class Output {
    private OutputStream stream;
    private Writer write;

    public Output(OutputStream stream) {
        this.stream = stream;
        this.write=new OutputStreamWriter(stream);
    }

    public Output(Writer write) {
        this.stream=null;
        this.write = write;
    }

    /**
     * Escribe al FileOutputStream de {@code file}
     * @throws Exception    Si ocurre algun error de IO
     */
    public Output(File file) throws Exception {
        this(new FileOutputStream(file));
    }
    
    /**
     * Escribe al FileOutputStream de {@code file}
     * @throws Exception    Si ocurre algun error de IO
     */
    public Output(String file) throws Exception{
        this(new FileOutputStream(file));
    }

    /**
     * Devuelve el Stream
     */
    public OutputStream getStream() {
        return stream;
    }

    /**
     * Devuelve el Writer
     */
    public Writer getWriter() {
        return write;
    }
}
