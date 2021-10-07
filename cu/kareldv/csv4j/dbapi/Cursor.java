package cu.kareldv.csv4j.dbapi;

import cu.kareldv.csv4j.functional.ForEachLoop;

/**
 * Interfaz con los metodos necesario para acceder a los elementos de la tabla, lo mas semejante posible a las otras bases de datos
 * <pre>
 * ...
 * Database datab = ...
 * 
 * Cursor mCursor = datab.select(new YourFavoriteFilter());
 * 
 * if(mCursor.moveToFirst())
 * do{
 * 
 *     //Obtiene el texto de la columna 0
 *     String id = mCursor.get(0);
 *     
 *     //Obtiene el texto de la columna 1
 *     //Y lo convierte en un entero
 *     int count = mCursor.getIneger(1);
 * 
 * }while(mCursor.moveToNext());
 * ...
 * </pre>
 * @author Karel
 */
public interface Cursor {
    /**
     * Función leer los elementos de este cursor uno por uno de forma simple utilizando funciones lambda:
     *  <pre>
     *  ...
     *  mCursor.forEach( (columns, row) -&gt; {
     * 
     *      System.out.print("Row #"+row+": ");
     *      System.out.println(Arrays.toString(columns));
     * 
     *      //TODO:Haz algo aqui
     * 
     *  } );
     *  ...</pre>
     * @param loop  
     */
    public void forEach(ForEachLoop loop);
    /**
     * Devuelve los nombres de las columnas
     */
    public String[] columnNames();
    /**
     * Devuelve la cantidad de Filas que tiene este Cursor
     */
    public int getRowsCount();
    /**
     * Mueve este Cursor hasta el principio, para poder 
     * @return  true - Si este Cursor tiene mas de 1 elemento
     */
    public boolean moveToFirst();
    /**
     * Mueve este cursor un lugar adelante
     * @return  true - si no esta al final de la tabla
     */
    public boolean moveToNext();
    /**
     * Mueve este cursor un lugar detras
     * @return  true - si no esta al principio
     */
    public boolean moveToPrevious();
    /**
     * Muee este cursor hacia adelante ( 1, 32 ...) o hacia detras ( -1, -20 ...) la cantidad de veces especificada en {@code stepCount}
     * @param stepCount
     * @return 
     */
    public boolean move(int stepCount);
    /**
     * Obtiene los valores de todas las columnas de la fila actual
     */
    public String[] columns();
    /**
     * Obtiene el valor de la columna {@code column}
     * @param column    Numero de columna
     * @return          El valor de es columna
     */
    public String get(int column);
    /**
     * Obtiene el valor con {@link #get(int) } y lo convierte en un Integer
     */
    public default int getInteger(int column){
        return Integer.parseInt(get(column));
    }
    /**
     * Obtiene el valor con {@link #get(int) } y lo convierte en un Float
     */
    public default float getFloat(int column){
        return Float.parseFloat(get(column));
    }
    /**
     * Obtiene el valor con {@link #get(int) } y lo convierte en un Long
     */
    public default long getLong(int column){
        return Long.parseLong(get(column));
    }
    /**
     * Obtiene el valor con {@link #get(int) } y lo convierte en un Double
     */
    public default double getDouble(int column){
        return Double.parseDouble(get(column));
    }
    /**
     * Obtiene el valor con {@link #get(int) } y lo convierte en un Short
     */
    public default short getShort(int column){
        return Short.parseShort(get(column));
    }
    /**
     * Obtiene el valor con {@link #get(int) } y lo convierte en un Byte
     */
    public default byte getByte(int column){
        return Byte.parseByte(get(column));
    }
}
