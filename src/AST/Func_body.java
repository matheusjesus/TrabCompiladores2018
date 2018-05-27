package AST;

public class Func_body {
    private final Decl declaration;
    private final Stmt_list stmt_list;
    
    public Func_body(Decl declaration, Stmt_list stmt_list){
        this.declaration = declaration;
        this.stmt_list = stmt_list;
    }
    
    public Stmt_list getStmt(){
        return stmt_list;
    }
    
    public void genC(PW pw){
        if(declaration != null)
            declaration.genC(pw);
        if(stmt_list != null)
            stmt_list.genC(pw);
    }
}
