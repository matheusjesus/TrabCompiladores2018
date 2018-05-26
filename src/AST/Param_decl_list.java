package AST;

import java.util.ArrayList;

public class Param_decl_list {
    ArrayList<Param_decl> parlist;

    public Param_decl_list(ArrayList<Param_decl> parlist){
        this.parlist = parlist;
    }
    
    public ArrayList<Param_decl> getParlist(){
        return this.parlist;
    }
    /* ({param_decl_list}) */
    public void genC(PW pw){
        int i;
        
        parlist.get(0).genC(pw);
        for(i=1;i<parlist.size();i++){
            pw.print(", ", false);
            parlist.get(i).genC(pw);
        }
    }
}