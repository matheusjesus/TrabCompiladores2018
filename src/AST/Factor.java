package AST;

import java.util.ArrayList;

public class Factor {
    private Primary primary;
    private Call_expr call;
    private ArrayList<Factor_tail> tail; 
    
    public Factor(Primary primary){
        this.primary = primary;
        this.call = null;
        tail = null;
    }
    
    public Factor(Call_expr call_expr){
        this.primary = null;
        this.call = call_expr;
        tail = null;
    }

    public Factor(Call_expr call_expr, ArrayList<Factor_tail> tail){
        this.primary = primary;
        this.call = call_expr;
        this.tail = tail;
    }
    
    public Factor(Primary primary, ArrayList<Factor_tail> tail){
        this.primary = primary;
        this.call = null;
        this.tail = tail;
    }
    
    public void genC(){
        
    }
    
}
