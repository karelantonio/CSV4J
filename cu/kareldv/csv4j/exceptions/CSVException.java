package cu.kareldv.csv4j.exceptions;

/**
 * Excepcion base, uso interno
 * @author Karel
 */
public class CSVException extends Exception {

    public CSVException() {
    }

    public CSVException(String message) {
        super(message);
    }

    public CSVException(String message, Throwable cause) {
        super(message, cause);
    }

    public CSVException(Throwable cause) {
        super(cause);
    }

    public CSVException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
