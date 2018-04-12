package AST;

public class Decl {
    String_decl_list stringlist;
    Var_decl_list varlist;
    
    public Decl(String_decl_list stringlist, Var_decl_list varlist){
       this.stringlist = stringlist;
       this.varlist = varlist;
    }
    
    public void genC(PW pw){
        stringlist.genC(pw);
        varlist.genC(pw);
    }
}
