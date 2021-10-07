package cu.kareldv.csv4j.dbimpl;

import cu.kareldv.csv4j.dbapi.Where;

/**
 * Filtro que siempre devuelve {@code true}
 * @author Karel
 */
public final class AnyWhere implements Where{

    @Override
    public boolean matches(String[] rowData, int row) {
        return true;
    }
    
}
