package AST;

public class Pgm_body {
    private Decl decl;
    private Func_declarations fdecl;
    
    public Pgm_body(Decl decl, Func_declarations fdecl){
        this.decl = decl;
        this.fdecl = fdecl;
    }
    
    public void genC(){
        System.out.println("Corpo:\n");
    }
}
