package AST;

import java.util.ArrayList;

public class Read_Stmt extends Stmt{
    private ArrayList<Id> id_list;
    
    public Read_Stmt(ArrayList<Id> id_list){
        this.id_list = id_list;
    }
    
    private void genC(){
        
    }
}
