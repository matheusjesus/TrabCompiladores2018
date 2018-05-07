package AST;

import java.util.ArrayList;


public class Write_stmt extends Stmt{
    private final ArrayList<Id> id_list;
    
    public Write_stmt(ArrayList<Id> id_list){
        this.id_list = id_list;
    }
    
    @Override
    public void genC(PW pw, boolean ident) {
        for(Id i : id_list){
            pw.println("print(\"%\", "+i.getId()+");", true);
        }
    }
}
