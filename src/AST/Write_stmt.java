package AST;

import java.util.ArrayList;


public class Write_stmt extends Stmt{
    private ArrayList<Id> id_list;
    
    public Write_stmt(ArrayList<Id> id_list){
        this.id_list = id_list;
    }
    
    private void genC(){
        
    }
}
