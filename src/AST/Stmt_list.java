package AST;

import java.util.ArrayList;


public class Stmt_list extends Stmt{
    private ArrayList<Stmt> stmtlist;
    
    public Stmt_list(ArrayList<Stmt> stmtlist){
        this.stmtlist = stmtlist;
    }
    
    
}