package cu.kareldv.csv4j;

import cu.kareldv.csv4j.dbapi.Cursor;
import cu.kareldv.csv4j.exceptions.CSVException;
import cu.kareldv.csv4j.exceptions.SyntaxException;
import cu.kareldv.csv4j.util.Source;
import cu.kareldv.csv4j.util.Utils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Clase superior donde se tiene acceso a crear/abrir bases de datos CSV
 * @see Database
 * @see Cursor
 * @author Karel
 */
public final class Databases {

    Databases(){}
    
    /**
     * Abre una base de datos de solo lectura, lanza una excepción si ocurre algun error
     * @param data          El Archivo/Stream donde leer la base de datos
     * @return              La base de datos abierta
     * @throws Exception    Si ocurre un error de IO
     */
    public static final Database openReadonly(Source data) throws Exception{
        return parse(data, false);
    }
    
    /**
     * Abre una base de datos esitable, lanza una excepción si ocurre algun error
     * @param data          El Archivo/Stream donde leer la base de datos
     * @return              La base de datos abierta
     * @throws Exception    Si ocurre un error de IO
     */
    public static final Database openWritable(Source data) throws Exception{
        return parse(data, true);
    }
    
    /**
     * Crea una base de datos editable y vacia
     * @param headers   Cabeceras de la base de datos
     * @return          Devuelve una base de datos editable y vacia
     * @see Database#insert(java.lang.String[]) 
     */
    public static final Database createEmpty(String[] headers){
        Utils.nonNull(headers, "The headers cannot be null");
        return new ImplDatabase(new ImplCursor(headers), true);
    }
    
    /**
     * Caracter que separa los elementos de la base de datos
     */
    public static char  SEPARATOR= ',';
    /**
     * Caracter que define los elementos de una tabla que tienen espacios, comas
     */
    public static char  QUOTATION= '"';
    /**
     * Caracter de escape para poder agregar Comillas y comas a los elementos de la base de datos
     */
    public static char  ESCAPE   = '\\';
    /**
     * Condicional de si se debe lanzar un error cuando se encuentren lineas vacias o con una cantidad de columnas mayor/menor que las de la tabla (De ser asi, los casos anteriores son ignorados)
     */
    public static boolean   EXIT_ON_INVALID_COUNT = true;
    /**
     * Condicional de si se deberia añadir soporte para las lineas de comentario (Inician con #, para compatibilidad), si no se reconocen como elementos de la tabla
     */
    public static boolean   ALLOW_COMMENTS = false;
    /**
     * Condicional de si se deberia añadir soporte para leer linear con una coma al final (Para compatibilidad), si no se lanzara un error de sintaxis inválida
     */
    public static boolean   ALLOW_ENDING_COMMA_COMPATIBILITY = false;
    
    
    
    
    
    private static Database parse(Source src, boolean editable) throws Exception{
        Lock lock = new ReentrantLock();
        lock.lock();
        
        int line = 0;
        String l = null;
        try{
            Scanner sc = new Scanner(src.getReader());
            
            if(!sc.hasNextLine())throw new CSVException("The input is Empty");
            
            String tmpLn_0 = null;
            if(ALLOW_COMMENTS)
            while(sc.hasNextLine()){
                String ln = sc.nextLine().trim();
                if(ln.startsWith("#")){
                    continue;
                }else{
                    tmpLn_0=ln;
                    break;
                }
            }
            else{
                tmpLn_0=sc.nextLine();
            }
            
            String[] headers = split(tmpLn_0);
            if(headers.length==0)throw new CSVException("The header chart is empty!");
            
            
            int headCount = headers.length;
            ImplCursor curs = new ImplCursor(headers);
            
            String val[] = null;
            
            line = 0;
            
            while(sc.hasNextLine()){
                //Nueva Linea
                l = sc.nextLine().trim();
                
                if(ALLOW_COMMENTS&&l.startsWith("#")){
                    continue;
                }
                
                if(ALLOW_ENDING_COMMA_COMPATIBILITY&&l.endsWith(",")){
                    l=l.substring(0, l.length()-1);
                }
                
                //Linea en blanco, si hace falta poner una line con espacios, usen comillas
                if(l.isBlank()){
                    if(EXIT_ON_INVALID_COUNT){
                        throw new SyntaxException("Found an empty line at "+line);
                    }else{
                        line++;
                        continue;
                    }
                }
                
                //Divide en columnas
                val=split(l);
                
                //La linea no tiene la cantidad necesaria de columnas
                if(val.length!=headCount){
                    if(EXIT_ON_INVALID_COUNT){
                        throw new SyntaxException("There is a line without the required count of columns!, at line "+line);
                    }else{
                        line++;
                        continue;
                    }
                }
                
                curs.addItem(val);
                
                line++;
            }
            
            return new ImplDatabase(curs, editable);
        }catch(Throwable t){
            System.err.println("Error at:");
            System.err.println(line+"# "+l);
            throw new CSVException(t);
        }finally{
            lock.unlock();
        }
    }
    
    private static String[] split(String nextLine) throws Exception {
        ArrayList<String> lines = new ArrayList<>();
        
        boolean isFirst = true;
        
        ArrayIterator it = new ArrayIterator(nextLine.toCharArray());
        
        while(it.hasNext()){
            consumeSpace(it);
            
            if(isFirst)isFirst=false;
            else{
                if(it.nextValue()!=SEPARATOR){
                    throw new CSVException("Expected "+SEPARATOR+" but found "+it.peekNext());
                }
                consumeSpace(it);
            }
            
            char c = it.nextValue();
            if(c==QUOTATION){
                lines.add(consumeQuotation(it));
            }else{
                it.beforeValue();
                lines.add(consumeWord(it));
            }
        }
        
        return lines.toArray(new String[lines.size()]);
    }
    
    private static String consumeWord(ArrayIterator it) throws Exception{
        String val = "";
        
        while(it.hasNext()){
            char c = it.nextValue();
            
            if(Character.isWhitespace(c)||c==SEPARATOR){
                if(val.isBlank())throw new CSVException("The value is empty, that means that a part of the table is: , ,, and thats not possible, try wrapping the text using \"\" (Or another quotation)");
                it.beforeValue();
                return val;
            }
            
            val+=c;
        };
        
        if(val.isBlank())throw new CSVException("The value is empty, that means that a part of the table is: , ,, and thats not possible, try wrapping the text using \"\" (Or another quotation)");
        return val;
    }
    
    private static String consumeQuotation(ArrayIterator it) throws Exception{
        String val = "";
        boolean isEscape = false;
        while(it.hasNext()){
            char n = it.nextValue();
            
            //N es igual a: ", si es de escape agregarlo al texto, si no terminar el bucle
            if(n==QUOTATION){
                if(isEscape){
                    val+=QUOTATION;
                    continue;
                }else{
                    return val;
                }
            }
            
            if(n==ESCAPE){
                //Escape del caracter de escape \\ = \
                if(isEscape){
                    val+=ESCAPE;
                    isEscape=false;
                    continue;
                }
                
                isEscape=true;
                continue;
            }
            
            val+=n;
        }
        
        throw new CSVException("Could not find the ending quote!");
    }
    
    private static void consumeSpace(ArrayIterator it){
        do{
            if(Character.isWhitespace(it.value()))return;
            
            char c = it.nextValue();
            if(!Character.isWhitespace(c)){
                //if(it.hasBefore())
                    it.beforeValue();
                return;
            }
        }while(it.hasNext());
    }
    
    private static class ArrayIterator {
        private char[] data;
        private int pos = 0;

        public ArrayIterator(char[] data) {
            this.data = data;
        }
        
        public boolean hasNext(){
            return pos<=data.length-1;
        }
        
        public char nextValue(){
            return data[pos++];
        }
        
        public boolean hasBefore(){
            return pos-1>=0;
        }
        
        public void beforeValue(){
            pos--;
        }
        
        public char value(){
            return data[pos];
        }
        
        public char peekNext(){
            try{
                return nextValue();
            }finally{
                beforeValue();
            }
        }
    }
}
