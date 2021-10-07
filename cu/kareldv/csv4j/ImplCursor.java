package cu.kareldv.csv4j;

import cu.kareldv.csv4j.dbapi.Cursor;
import cu.kareldv.csv4j.dbapi.Where;
import cu.kareldv.csv4j.functional.ForEachLoop;
import java.util.ArrayList;
import static cu.kareldv.csv4j.util.Utils.*;
import java.util.Arrays;
import java.util.Iterator;
/**
 *
 * @author Karel
 */
class ImplCursor implements Cursor{
    ArrayList<String[]> data;
    private String[] columnNames;
    private int pos = 0;

    public ImplCursor(String[] headers) {
        nonNull(headers);
        columnNames=headers;
        data=new ArrayList<>();
    }
    
    public ImplCursor(Cursor other, Where filter){
        this(other.columnNames());
        nonNull(other, "The other cursor cannot be null");
        nonNull(filter, "The Where filter cannot be null");
        
        if(!other.moveToFirst())return;
        
        String[] row=null;
        
        do{
            row = other.columns();
            
            if(filter.matches(row, pos)){
                addItem(row);
            }
        }while(other.moveToNext());
    }
    
    void addItem(String[] row){
        nonNull(row, "The data cannot be null");
        data.add(row);
    }
    
    void addItems(String[]... rows){
        nonNull(rows, "The data cannot be null");
        data.addAll(Arrays.asList(rows));
    }
    
    void delItem(String[] item){
        nonNull(item, "The data cannot be null");
        data.remove(item);
    }
    
    void delItems(String[]... item){
        nonNull(item, "The data cannot be null");
        data.removeAll(Arrays.asList(item));
    }

    @Override
    public void forEach(ForEachLoop loop) {
        if(moveToFirst())
        do{
            loop.consume(columns(), pos);
        }while(moveToNext());
    }

    @Override
    public String[] columnNames() {
        return columnNames;
    }
    
    @Override
    public int getRowsCount() {
        return data.size();
    }

    @Override
    public boolean moveToFirst() {
        if(data.isEmpty()){
            return false;
        }else{
            pos=0;
            return true;
        }
    }

    @Override
    public boolean moveToNext() {
        if(pos+1<data.size()){
            pos++;
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean moveToPrevious() {
        if(pos-1>=0){
            pos--;
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean move(int stepCount) {
        int value = pos+stepCount;
        if(value<data.size()&&value>=0){
            pos=value;
            return true;
        }else{
            return false;
        }
    }

    @Override
    public String[] columns() {
        return data.get(pos);
    }

    @Override
    public String get(int column) {
        return columns()[column];
    }
}
