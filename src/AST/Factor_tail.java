package AST;

import Lexer.Symbol;
import java.util.ArrayList;

public class Factor_tail {
    private Symbol mulop;
    private Primary primary;
    private Call_expr call;
    
    public Factor_tail(Symbol mulop, Primary primary, Call_expr call_expr){
        this.mulop = mulop;
        this.primary = primary;
        this.call = call_expr;
    }
    
    public void genC(){
        
    }
}
