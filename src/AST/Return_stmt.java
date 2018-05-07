package AST;

public class Return_stmt extends Stmt{
    private final Expr expr;
    
    public Return_stmt(Expr expr){
        this.expr = expr;
    }
    
    //return_stmt -> RETURN expr;
    
    @Override
    public void genC(PW pw, boolean ident) {
        pw.print("return ", true);
        expr.genC(pw);
        pw.println(" ;", false);
    }
}
