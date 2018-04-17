package AST;

public class Assign_stmt {
    private Id id;
    private Expr expr;
    
    public Assign_stmt(Id id, Expr expr){
        this.id = id;
        this.expr = expr;
    }
    
    private void genC(){
        
    }
}
