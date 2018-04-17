package AST;

import java.util.ArrayList;

public class Read_stmt extends Stmt{
    private ArrayList<Id> id_list;
    
    public Read_stmt(ArrayList<Id> id_list){
        this.id_list = id_list;
    }
    
    private void genC(){
        
    }
}
