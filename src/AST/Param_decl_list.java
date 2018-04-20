package AST;

import java.util.ArrayList;

public class Param_decl_list {
    ArrayList<Param_decl> parlist;

    public Param_decl_list(ArrayList<Param_decl> parlist){
        this.parlist = parlist;
    }
    /* ({param_decl_list}) */
    public void genC(PW pw){
        //chamar genC() de cada elemento da lista, tudo entre ()
        pw.print("( ");
        if(parlist != null){
            for( int i = 0; i < parlist.size() - 1; i++){
                parlist.get(i).genC(pw);
                pw.print(", ");
            }
            parlist.get(parlist.size() - 1).genC(pw);
        }
        pw.print(" )");
    }
}