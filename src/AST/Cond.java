package AST;

import Lexer.Symbol;

public class Cond {
    private final Expr expr1;
    private final Symbol compop;
    private final Expr expr2;
    
    public Cond(Expr expr1, Symbol compop, Expr expr2){
        this.expr1 = expr1;
        this.expr2 = expr2;
        this.compop = compop;
    }
    
    //cond -> expr compop expr
    public void genC(PW pw){
        expr1.genC(pw);
        pw.print(" " + compop.toString() + " ", false);
        expr2.genC(pw);
        
    }
}
