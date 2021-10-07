package cu.kareldv.csv4j.dbimpl;

import cu.kareldv.csv4j.dbapi.Where;

/**
 * Filtro que devuelve si la cadena de la columna especificada en el constructor esta vacía
 * @author Karel
 */
public final class IsEmptyWhere implements Where{
    private int column;

    public IsEmptyWhere() {
    }

    @Override
    public boolean matches(String[] rowData, int row) {
        if(rowData.length<=column){
            return false;
        }
        
        return rowData[column].isBlank();
    }
}
