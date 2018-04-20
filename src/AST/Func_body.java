package AST;

public class Func_body {
    private final Decl declaration;
    private final Stmt_list stmt_list;
    
    public Func_body(Decl declaration, Stmt_list stmt_list){
        this.declaration = declaration;
        this.stmt_list = stmt_list;
    }
    
    public void genC(PW pw){
        declaration.genC(pw);
        stmt_list.genC(pw);
    }
}
