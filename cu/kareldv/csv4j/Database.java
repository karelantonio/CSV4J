package cu.kareldv.csv4j;

import cu.kareldv.csv4j.dbapi.Cursor;
import cu.kareldv.csv4j.dbapi.Where;
import cu.kareldv.csv4j.dbimpl.AnyWhere;
import cu.kareldv.csv4j.dbimpl.IsEmptyWhere;
import cu.kareldv.csv4j.dbimpl.NumberWhere;
import cu.kareldv.csv4j.exceptions.CSVError;
import cu.kareldv.csv4j.util.Output;

/**
 * Interfaz que contiene los metodos fundamentales para el acceso a la base de datos en formato CSV
 * @see Cursor
 * @author Karel
 */
public interface Database {
    
    /**
     * Filtra entre todos los valores de la base de datos los que cumplan con los requisitos de {@link Where#matches(java.lang.String[], int) }
     * @param where Objeto con el que filtrar: {@link AnyWhere} {@link IsEmptyWhere} {@link MultiWhere} {@link NumberWhere} {@link Where}
     * @return      EL {@code Cursor} con el que se accede a los resultados
     */
    public Cursor select(Where where);
    /**
     * Inserta los datos especificados a la tabla
     * @throws CSVError Si la tabla es de solo-lectura {@link #isWritable() }
     * @param rowValues Valores de las columnas, si tiene un tamaño menor o mayor que la cantidad de cabeceras (O columnas) lanzara una excepcion
     * @param row       Numero de columna {@link #insert(java.lang.String[]) }
     */
    public void insert(String[] rowValues, int row);
    /**
     * Inserta los datos especificados en {@code rowValues}, si tienen un tamaño distinto a la cantidad de cabeceras (o columnas) lanzara una excepcion
     * @param rowValues Valores de las columnas
     */
    public default void insert(String[] rowValues){
        insert(rowValues, 0);
    }
    /**
     * Devuelve todos los elementos de esta tabla
     * @return  Cursor con todos los elementos de esta tabla
     */
    public Cursor allItems();
    /**
     * Cantidad de elementos en esta table: !!No confundir con cantidad de cabecerar o columnas
     */
    public int allItemsCount();
    /**
     * Devuelve si la tabla se puede editar, util para evitar errores
     */
    public boolean isWritable();
    /**
     * Escribe esta table en formato CSV. NO UTILIZA NINGUNO DE ESTOS ELEMENTOS:
     * + Compatibilidad con Coma (Separador {@link Databases#SEPARATOR}) al final de linea
     * + Compatibilidad de comentarios (# al inicio de cada linea {@link Databases#ALLOW_COMMENTS})
     * @param out           Archivo/Stream de salida
     * @throws Exception    Si ocurre un error de IO
     */
    public void write(Output out) throws Exception;
}
