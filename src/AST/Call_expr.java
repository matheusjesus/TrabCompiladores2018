package AST;

import java.util.ArrayList;

public class Call_expr extends Stmt{
    private Id id;
    private ArrayList<Expr> expr_list;
    
    public Call_expr(Id id){
        this.id = id;
        this.expr_list = null;
    }
    
    public Call_expr(Id id, ArrayList<Expr> expr_list){
        this.id = id;
        this.expr_list = expr_list;
    }
    
    private void genC(){
        
    }
}
