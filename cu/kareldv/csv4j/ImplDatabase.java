package cu.kareldv.csv4j;

import cu.kareldv.csv4j.Database;
import cu.kareldv.csv4j.dbapi.Cursor;
import cu.kareldv.csv4j.dbapi.Where;
import cu.kareldv.csv4j.exceptions.CSVError;
import cu.kareldv.csv4j.util.Output;
import static cu.kareldv.csv4j.util.Utils.*;
import java.io.PrintWriter;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 *
 * @author Karel
 */
final class ImplDatabase implements Database {
    private ImplCursor items;
    private boolean editable;
    private Lock selectLock = new ReentrantLock(),
            insertLock = new ReentrantLock(),
            writeLock = new ReentrantLock();

    public ImplDatabase(ImplCursor items, boolean editable) {
        nonNull(items, "The default cursor cannot be Null");
        this.items = items;
        this.editable = editable;
    }
    
    @Override
    public Cursor select(Where where) {
        selectLock.lock();
        try{
            return new ImplCursor(items, where);
        }finally{
            selectLock.unlock();
        }
    }

    @Override
    public void insert(String[] rowValues, int row) {
        insertLock.lock();
        try{
            nonNull(rowValues, "The values to be inserted cannot be null");
            checkRange(row, allItemsCount(), 0, true);
            
            if(!editable)throw new CSVError("The database is not writable!");
            
            items.data.add(row, rowValues);
        }finally{
            insertLock.unlock();
        }
    }

    @Override
    public Cursor allItems() {
        return items;
    }
    
    @Override
    public int allItemsCount(){
        return items.getRowsCount();
    }

    @Override
    public boolean isWritable() {
        return editable;
    }

    @Override
    public void write(Output out) throws Exception{
        writeLock.lock();
        try{
            PrintWriter wr = new PrintWriter(out.getWriter());
            
            boolean firstColumn=true;
            for (String arg : items.columnNames()) {
                if(!firstColumn){
                    wr.write(Databases.SEPARATOR);
                }else firstColumn=false;

                arg=arg.replace(Databases.ESCAPE+"", (Databases.ESCAPE+""+Databases.ESCAPE));

                if(arg.indexOf(' ')!=-1){
                    arg=arg.replace(Databases.QUOTATION+"", (Databases.ESCAPE+""+Databases.QUOTATION));
                    wr.print(Databases.QUOTATION+arg+Databases.QUOTATION);
                }else{
                    arg=arg.replace(Databases.SEPARATOR+"", Databases.ESCAPE+""+Databases.SEPARATOR);
                    wr.print(arg);
                }
            }
            
            if(allItemsCount()!=0){
                wr.println();
            }
            
            boolean firstRow = true;
            firstColumn = true;
            
            if(items.moveToFirst())
            do{
                
                if(!firstRow){
                    wr.println();
                }else firstRow=false;
                
                String[] args = items.columns();
                
                firstColumn=true;
                for (String arg : args) {
                    if(!firstColumn){
                        wr.write(Databases.SEPARATOR);
                    }else firstColumn=false;
                    
                    arg=arg.replace(Databases.ESCAPE+"", (Databases.ESCAPE+""+Databases.ESCAPE));
                    
                    if(arg.indexOf(' ')!=-1){
                        arg=arg.replace(Databases.QUOTATION+"", (Databases.ESCAPE+""+Databases.QUOTATION));
                        wr.print(Databases.QUOTATION+arg+Databases.QUOTATION);
                    }else wr.print(arg);
                }
                
            }while(items.moveToNext());
            
            wr.flush();
        }finally{
            writeLock.unlock();
        }
    }
    
    class AllWhere implements Where{

        @Override
        public boolean matches(String[] rowData, int row) {
            return true;
        }
        
    }
}
