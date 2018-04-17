package AST;

public class Func_body {
    private Decl declaration;
    private Stmt_list stmt_list;
    
    public Func_body(Decl declaration, Stmt_list stmt_list){
        this.declaration = declaration;
        this.stmt_list = stmt_list;
    }
    
    public void genC(){
        
    }
}
