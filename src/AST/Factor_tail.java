package AST;

import Lexer.Symbol;

public class Factor_tail {
    private final Symbol mulop;
    private final Primary primary;
    private final Call_expr call;
    
    public Factor_tail(Symbol mulop, Primary primary, Call_expr call_expr){
        this.mulop = mulop;
        this.primary = primary;
        this.call = call_expr;
    }
    
    public void genC(PW pw){
        pw.print("("+mulop.toString()+" ");
        if(primary != null)
            primary.genC(pw);
        else
            call.genC(pw);
        pw.print(")");
        
    }
}
