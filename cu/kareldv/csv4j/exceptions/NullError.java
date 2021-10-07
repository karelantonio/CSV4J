package cu.kareldv.csv4j.exceptions;

/**
 * Null - Uso interno
 * @author Karel
 */
public class NullError extends CSVError{

    public NullError() {
    }

    public NullError(String message) {
        super(message);
    }

    public NullError(String message, Throwable cause) {
        super(message, cause);
    }

    public NullError(Throwable cause) {
        super(cause);
    }

    public NullError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
