package AST;

import Lexer.Symbol;

public class Expr_tail {
    private final Symbol addop;
    private final Factor factor;
    
    public Expr_tail(Symbol addop, Factor factor){
        this.addop = addop;
        this.factor = factor;
    }
    
    public void genC(PW pw){
        pw.print(""+addop.toString()+" ", false);
        factor.genC(pw);        
    }
}
