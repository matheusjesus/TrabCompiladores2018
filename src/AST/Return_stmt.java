package AST;

class Return_stmt {
    private Expr expr;
    
    public Return_stmt(Expr expr){
        this.expr = expr;
    }
    
    private void genC(){
        //return expr;
    }
}
