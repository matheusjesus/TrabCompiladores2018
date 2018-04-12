package AST;

import java.util.ArrayList;

public class Param_decl_list {
    ArrayList<Param_decl> parlist;

    public Param_decl_list(ArrayList<Param_decl> parlist){
        this.parlist = parlist;
    }
    
    public void genC(){
        //chamar genC() de cada elemento da lista, tudo entre ()
    }
}