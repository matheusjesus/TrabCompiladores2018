package AST;

import Lexer.Symbol;

public class If_stmt extends Stmt {
    private Expr expr1;
    private Symbol compop;
    private Expr expr2;
    private Stmt_list stmt;
    private Else_part else_part;
     
    public If_stmt(Expr expr1, Symbol compop, Expr expr2, Stmt_list stmt, Else_part else_part){
        this.expr1 = expr1;
        this.expr2 = expr2;
        this.compop = compop;
        this.stmt = stmt;
        this.else_part = else_part;
    }
    
    public void genC(){
        
    }
}
