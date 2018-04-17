package AST;

public class Return_stmt extends Stmt{
    private Expr expr;
    
    public Return_stmt(Expr expr){
        this.expr = expr;
    }
    
    private void genC(){
        //return expr;
    }
}
