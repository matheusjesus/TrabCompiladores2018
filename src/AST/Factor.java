package AST;

import java.util.ArrayList;

public class Factor {
    private Primary primary;
    private Call_expr call;
    private ArrayList<Factor_tail> tail; 
    
    public Factor(Primary primary, Call_expr call, ArrayList<Factor_tail> tail){
        this.primary = primary;
        this.call = call;
        this.tail = tail;
    }
    
    public void genC(){
        
    }
    
}
