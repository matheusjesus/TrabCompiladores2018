package AST;

import java.util.ArrayList;

public class Expr {
    private Factor factor;
    private ArrayList<Expr_tail> tail;
    
    public Expr(Factor factor){
        this.factor = factor;
        this.tail = null;
    }
    
    public Expr(Factor factor, ArrayList<Expr_tail> tail){
        this.factor= factor;
        this.tail = tail;
    }
    
    public void genC(){
        //se o tail == null -> nao chamar o genc do tail, apenas do factor
    }
}
