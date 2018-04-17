package AST;

import Lexer.Symbol;
import java.util.ArrayList;

class Factor_tail {
    private Symbol mulop;
    private Primary primary;
    private Call_expr call;
    
    public Factor_tail(Symbol mulop, Primary primary){
        this.mulop = mulop;
        this.primary = primary;
        this.call = null;
    }
    
    public Factor_tail(Symbol mulop, Call_expr call_expr){
        this.mulop = mulop;
        this.primary = null;
        this.call = call_expr;
    }
    
    public void genC(){
        
    }
}
