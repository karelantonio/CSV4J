package cu.kareldv.csv4j.exceptions;

/**
 * Range (Rango de numeros) - Interno
 * @author Karel
 */
public class RangeError extends CSVError{

    public RangeError() {
    }

    public RangeError(String message) {
        super(message);
    }

    public RangeError(String message, Throwable cause) {
        super(message, cause);
    }

    public RangeError(Throwable cause) {
        super(cause);
    }

    public RangeError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
