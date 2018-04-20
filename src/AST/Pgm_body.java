package AST;

public class Pgm_body {
    private Decl decl;
    private Func_declarations fdecl;
    
    public Pgm_body(Decl decl, Func_declarations fdecl){
        this.decl = decl;
        this.fdecl = fdecl;
    }
    
    public void genC(PW pw){
        if(decl != null){
            System.out.println("[body]_decl -> decl");
            decl.genC(pw);
        }
        if(fdecl != null){
            System.out.println("[body]_fdecl->Func_declarations");
            fdecl.genC(pw);

        }
    }
}