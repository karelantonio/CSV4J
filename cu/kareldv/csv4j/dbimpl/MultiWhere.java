package cu.kareldv.csv4j.dbimpl;

import cu.kareldv.csv4j.dbapi.Where;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Filtro que contiene varios filtros y utiliza operadores para conocer el resultado (AND, OR, XOR, NOT)
 * @author Karel
 */
public final class MultiWhere implements Where{
    private ArrayList<Where> others;
    private Operator operator;
    private boolean shouldContinue = true;

    public MultiWhere(Operator operator, Where... others) {
        this.operator = operator;
        this.others=new ArrayList<>();
        this.others.addAll(Arrays.asList(others));
    }

    @Override
    public boolean matches(String[] rowData, int row) {
        boolean isFirst = true,
                actual = false,
                result;
        
        for (Where other : others) {
            result = other.matches(rowData, row);
            
            if(!isFirst){
                
                actual=operator.apply(actual, result);
                
                if(!operator.shouldContinue(actual)){
                    return actual;
                }
                
            }else isFirst=false;
        }
        
        return actual;
    }
    
    public static enum Operator{
        AND,
        OR,
        XOR,
        NOT;
        
        public boolean apply(boolean old, boolean newer){
            switch(this){
                case AND:
                    return old&&newer;
                case OR:
                    return old||newer;
                case XOR:
                    return old^newer;
                case NOT:
                    return old!=newer;
            }
            return false;
        }
        
        public boolean shouldContinue(boolean actual){
            switch(this){
                case AND:
                    return actual;
                case OR:
                case XOR:
                case NOT:
                    return true;
            }
            return true;
        }
    }
}
