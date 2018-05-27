package AST;

import java.util.ArrayList;


public class Stmt_list{
    private final ArrayList<Stmt> stmtlist;
    

    public Stmt_list(ArrayList<Stmt> stmtlist) {
        this.stmtlist = stmtlist;
    }
  
    //stmt_list -> stmt stmt_tail | empty
    public void genC(PW pw) {
        if(stmtlist != null){
            for(Stmt s : stmtlist) {
                s.genC(pw, true);
            }
        }
    }
    
    
}