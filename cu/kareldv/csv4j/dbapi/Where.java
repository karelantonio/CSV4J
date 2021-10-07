package cu.kareldv.csv4j.dbapi;

import cu.kareldv.csv4j.Database;

/**
 * Clase que especifica un filtro para {@link Database#select(cu.kareldv.csv4j.dbapi.Where) }, puede ser usada como una funcion lambda
 * @author Karel
 */
public interface Where {
    /**
     * Filtra los datos dados via los argumentos
     * @param rowData   Los valores de las columnas
     * @param row       Numero de fila actual
     * @return          Si el valor deberia seleccionarse
     */
    public boolean matches(String[] rowData, int row);
}
