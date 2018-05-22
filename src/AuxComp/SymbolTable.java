package AuxComp;

import java.util.Hashtable;

public class SymbolTable {
    public Hashtable globalTable, localTable;
    
    public SymbolTable(){
        globalTable = new Hashtable();
        localTable = new Hashtable();
    }
    
    public Object putInGlobal(String key, Object valor){
        return globalTable.put(key, valor);
    }
    
    public Object putInLocal(String key, Object valor){
        return localTable.put(key, valor);
    }
    
    public Object getInGlobal(Object key){
        return globalTable.get(key);
    }
    
    public Object getInLocal(Object key){
        return localTable.get(key);
    }
    
    public Object get(String key){
        Object result;
        
        if((result = localTable.get(key)) != null){
            return result;
        }
        else{
            return globalTable.get(key);
        }
    }
    
    public void removeLocalIdent(){
        localTable.clear();
    }            
}