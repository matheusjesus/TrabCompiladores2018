package AST;

public class Return_stmt{
    private Expr expr;
    
    public Return_stmt(Expr expr){
        this.expr = expr;
    }
    
    //return_stmt -> RETURN expr;
    
    public void genC(PW pw) {
        pw.print("return ");
        expr.genC(pw);
        pw.println(" ;");
    }
}
