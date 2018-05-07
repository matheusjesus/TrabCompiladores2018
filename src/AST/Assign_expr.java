package AST;

public class Assign_expr extends Stmt{
    private final Id id;
    private final Expr expr;
    
    public Assign_expr(Id id, Expr expr){
        this.id = id;
        this.expr = expr;
    }

    @Override
    public void genC(PW pw, boolean ident) {
        pw.print(id.getId()+" = ", ident);
        expr.genC(pw);
        if(ident == true){
            pw.println(";", false);
        }
    }    
}
