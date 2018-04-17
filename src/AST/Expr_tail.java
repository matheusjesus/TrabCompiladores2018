package AST;

import Lexer.Symbol;
import java.util.ArrayList;

class Expr_tail {
    private Symbol addop;
    private Factor factor;
    
    public Expr_tail(Symbol addop, Factor factor){
        this.addop = addop;
        this.factor = factor;
    }
    
    public void genC(){
        
    }
}
