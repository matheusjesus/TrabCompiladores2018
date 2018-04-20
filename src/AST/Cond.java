package AST;

import Lexer.Symbol;

public class Cond {
    private Expr expr1;
    private Symbol compop;
    private Expr expr2;
    
    public Cond(Expr expr1, Symbol compop, Expr expr2){
        this.expr1 = expr1;
        this.expr2 = expr2;
        this.compop = compop;
    }
    
    //cond -> expr compop expr
    public void genC(PW pw){
        expr1.genC(pw);
        pw.print(" " + compop.toString() + " ");
        expr2.genC(pw);
        
    }
}
