package AST;

import Lexer.Symbol;
import java.util.ArrayList;


public class Stmt_list extends Stmt{
    private ArrayList<Stmt> stmtlist;
    private Read_stmt read;
    private Assign_expr assign;
    private Write_stmt write;
    private Return_stmt ret;
    private If_stmt if_stmt;
    private For_stmt for_stmt;
    

    public Stmt_list(ArrayList<Stmt> stmtlist) {
        this.stmtlist = stmtlist;
    }
  
    //stmt_list -> stmt stmt_tail | empty
    //stmt -> assign_stmt | read_stmt | write_stmt | return_stmt | if_stmt | for_stmt

    @Override
    public void genC(PW pw) {
        if(stmtlist != null){
            for(Stmt s : stmtlist) {
                if(s.equals(read))
                    read.genC(pw);
                else if(s.equals(assign))
                    assign.genC(pw);
                else if(s.equals(write))
                    write.genC(pw);
                else if(s.equals(ret))
                    ret.genC(pw);
                else if(s.equals(if_stmt))
                    if_stmt.genC(pw);
                else if(s.equals(for_stmt))
                    for_stmt.genC(pw);
            }
        }
    }
    
    
}