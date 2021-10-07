package cu.kareldv.csv4j.exceptions;

/**
 * Error base, uso interno
 * @author Karel
 */
public class CSVError extends Error{

    public CSVError() {
    }

    public CSVError(String message) {
        super(message);
    }

    public CSVError(String message, Throwable cause) {
        super(message, cause);
    }

    public CSVError(Throwable cause) {
        super(cause);
    }

    public CSVError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
