package cu.kareldv.csv4j.functional;

import cu.kareldv.csv4j.dbapi.Cursor;

/**
 * Interfaz para ser usada como expresion lambda  en {@link Cursor#forEach(cu.kareldv.csv4j.functional.ForEachLoop) }
 * @author Karel
 */
public interface ForEachLoop {
    /**
     * Consume un valor de el Cursor
     */
    public void consume(String[] columns, int row);
}
