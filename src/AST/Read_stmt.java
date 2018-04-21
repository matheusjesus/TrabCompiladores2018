package AST;

import Lexer.Symbol;
import java.util.ArrayList;

public class Read_stmt{
    private ArrayList<Id> id_list;
    
    public Read_stmt(ArrayList<Id> id_list){
        this.id_list = id_list;
    }
    
    //read_stmt -> READ ( id_list );

    public void genC(PW pw) {
        for(Id i : id_list){
            pw.println("scanf(\"%\","+i.getId()+");");
        }

    }

    public Symbol setTipo() {
        return Symbol.READ;
    }
}
