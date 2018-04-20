package AST;

import Lexer.Symbol;
import java.util.ArrayList;

public class Expr_tail {
    private Symbol addop;
    private Factor factor;
    
    public Expr_tail(Symbol addop, Factor factor){
        this.addop = addop;
        this.factor = factor;
    }
    
    public void genC(PW pw){
        pw.print("("+addop.toString()+" ");
        factor.genC(pw);
        pw.print(")");
            
        
    }
}
