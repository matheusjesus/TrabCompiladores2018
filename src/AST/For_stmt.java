package AST;

import Lexer.Symbol;

public class For_stmt {
    private Assign_stmt assign1;
    private Expr expr1;
    private Symbol compop;
    private Expr expr2;
    private Assign_stmt assign2;
    private Stmt_list stmt;
    
    public For_stmt(Assign_stmt assign1, Expr expr1, Symbol compop, Expr expr2, Assign_stmt assign2, Stmt_list stmt){
        this.assign1 = assign1;
        this.expr1 = expr1;
        this.compop = compop;
        this.expr2 = expr2;
        this.assign2 = assign2;
        this.stmt = stmt;
    }
    
    public void genC(){
        
    }
}
