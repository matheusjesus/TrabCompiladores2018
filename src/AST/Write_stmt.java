package AST;

import java.util.ArrayList;


public class Write_stmt{
    private ArrayList<Id> id_list;
    
    public Write_stmt(ArrayList<Id> id_list){
        this.id_list = id_list;
    }
    
    public void genC(PW pw) {
        for(Id i : id_list){
            System.out.println("write");
            pw.println("print(\""+i.getId()+"\");");
        }
    }
}
