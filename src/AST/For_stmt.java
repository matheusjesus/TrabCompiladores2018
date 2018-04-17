package AST;

import Lexer.Symbol;

public class For_stmt extends Stmt{
    private Assign_expr assign1;
    private Cond cond;
    private Assign_expr assign2;
    private Stmt_list stmt_list;
    
    public For_stmt(Assign_expr assign1, Cond cond, Assign_expr assign2, Stmt_list stmt_list){
        this.assign1 = assign1;
        this.cond = cond;
        this.assign2 = assign2;
        this.stmt_list = stmt_list;
    }
    
    public For_stmt(){
        
    }
    
    public void genC(){
        
    }
}
