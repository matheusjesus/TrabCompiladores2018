package AST;

import java.util.ArrayList;

public class Factor {
    private final Primary primary;
    private final Call_expr call;
    private final ArrayList<Factor_tail> tail; 
    
    public Factor(Primary primary, Call_expr call, ArrayList<Factor_tail> tail){
        this.primary = primary;
        this.call = call;
        this.tail = tail;
    }

    public void genC(PW pw){
        if(primary != null) 
            primary.genC(pw);
        else if (call != null)
            call.genC(pw, false);
        
        if (tail != null){
            for(Factor_tail f: tail)
                f.genC(pw);
        }
    }
}
