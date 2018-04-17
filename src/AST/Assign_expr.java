package AST;

public class Assign_expr extends Stmt{
    private Id id;
    private Expr expr;
    
    public Assign_expr(Id id, Expr expr){
        this.id = id;
        this.expr = expr;
    }
    
    private void genC(){
        
    }
}
