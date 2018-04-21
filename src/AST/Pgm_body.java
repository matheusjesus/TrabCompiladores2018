package AST;

public class Pgm_body {
    private final Decl decl;
    private final Func_declarations fdecl;
    
    public Pgm_body(Decl decl, Func_declarations fdecl){
        this.decl = decl;
        this.fdecl = fdecl;
    }
    
    public void genC(PW pw){
        if(decl != null)
            decl.genC(pw);
            
        if(fdecl != null)
            fdecl.genC(pw);

    }
}