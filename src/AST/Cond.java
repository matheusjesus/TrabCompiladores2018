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
    
    public void genC(){
        
    }
}
