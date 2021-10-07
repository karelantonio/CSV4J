package cu.kareldv.csv4j.dbimpl;

import cu.kareldv.csv4j.dbapi.Where;

/**
 * Filtro que devuelve si la cadena que esta en la columna especificada en el constructor es un numero (Entero o de punto flotante)
 * @see NumberType
 * @author Karel
 */
public final class NumberWhere implements Where{
    private NumberType type;
    private int column;

    public NumberWhere(NumberType type, int column) {
        this.type = type;
        this.column = column;
    }

    public NumberWhere(int column) {
        this.type = NumberType.INTEGER;
        this.column = column;
    }

    @Override
    public boolean matches(String[] rowData, int row) {
        //Verificar que exista la columna
        if(row>=rowData.length){
            return false;
        }
        
        //Verificar que sea un numero
        return type.matches(rowData[this.column]);
    }
    
    public static enum NumberType{
        /**
         * Si el Numero especificado es un entero
         */
        INTEGER("[-,+]?[0-9]*"),
        /**
         * Si el Numero especificado es un numero de punto flotante
         */
        FLOAT("[-,+]?[1-9][0-9]*.[0-9]+");
        
        private String regex;

        private NumberType(String regex) {
            this.regex = regex;
        }
        
        public boolean matches(String data){
            return data.matches(regex);
        }
    }
}
