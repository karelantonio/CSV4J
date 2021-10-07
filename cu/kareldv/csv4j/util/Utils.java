package cu.kareldv.csv4j.util;

import cu.kareldv.csv4j.exceptions.NullError;
import cu.kareldv.csv4j.exceptions.RangeError;

/**
 * Clase interna
 * @author Karel
 */
public final class Utils {
    Utils(){}
    
    public static void nonNull(Object o){
        if(o==null)throw new NullError("Null!!");
    }
    
    public static void nonNull(Object o, String text){
        if(o==null)throw new NullError(text);
    }
    
    public static void checkRange(int val, int max, int min){
        checkRange(val, max, min, true);
    }
    
    public static void checkRange(int val, int max, int min, boolean inclusive){
        if(inclusive){
            if(val>max){
                throw new RangeError("Range error, expected "+min+"-"+max+" (Inclusive), got "+val);
            }else if(val<min){
                throw new RangeError("Range error, expected "+min+"-"+max+" (Inclusive), got "+val);
            }
        }else{
            if(val>=max){
                throw new RangeError("Range error, expected "+min+"-"+max+" (Exclusive), got "+val);
            }else if(val<=min){
                throw new RangeError("Range error, expected "+min+"-"+max+" (Exclusive), got "+val);
            }
        }
    }
    
    public static void rangeError(int expected){
        throw new RangeError("Range error, expected "+expected);
    }
    
    public static void rangeError(String msg){
        throw new RangeError(msg);
    }
}
