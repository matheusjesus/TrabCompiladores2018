package AST;

import Lexer.Symbol;

public class If_stmt extends Stmt {
    private Cond cond;
    private Stmt_list stmt_list;
    private Else_part else_part;
     
    public If_stmt(Cond cond, Stmt_list stmt_list, Else_part else_part){
        this.cond = cond;
        this.stmt_list = stmt_list;
        this.else_part = else_part;
    }
    
    public void genC(){
        
    }
}
