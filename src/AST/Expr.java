package AST;

import java.util.ArrayList;

public class Expr {
    private final Factor factor;
    private final ArrayList<Expr_tail> tail;
    
    public Expr(Factor factor){
        this.factor = factor;
        this.tail = null;
    }
    
    public Expr(Factor factor, ArrayList<Expr_tail> tail){
        this.factor= factor;
        this.tail = tail;
    }
    
    public void genC(PW pw){
        //se o tail == null -> nao chamar o genc do tail, apenas do factor
        if(factor != null)
            factor.genC(pw);
        if(tail != null){
            for(Expr_tail f : tail){
                f.genC(pw);
            }                
        }
            
    }
}
