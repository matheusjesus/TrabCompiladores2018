package AST;

public class Decl {
    String_decl_list stringlist;
    Var_decl_list varlist;
    
    public Decl(String_decl_list stringlist, Var_decl_list varlist){
       this.stringlist = stringlist;
       this.varlist = varlist;
    }
    
    /*  decl -> string_decl_list {decl} | var_decl_list {decl} | empty */
    public void genC(PW pw){
        if(stringlist != null)
            stringlist.genC(pw);
        
        if(varlist != null)
            varlist.genC(pw);
            
        
    }
}
