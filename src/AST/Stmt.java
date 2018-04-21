package AST;

import Lexer.Symbol;

public class Stmt {
    private int tipo;
    private Read_stmt read;
    private Assign_expr assign;
    private Write_stmt write;
    private Return_stmt ret;
    private If_stmt if_stmt;
    private For_stmt for_stmt;
    private Call_expr callexpr;
    
    public Stmt(Call_expr callexpr, Assign_expr assign, Read_stmt read, Write_stmt write, Return_stmt return_stmt, If_stmt if_stmt, For_stmt for_stmt){
        this.callexpr = callexpr;
        this.assign = assign;
        this.read = read;
        this.write = write;
        this.ret = return_stmt;
        this.if_stmt = if_stmt;
        this.for_stmt = for_stmt;
        
        if(callexpr != null){
            tipo = 0;
        }
        else if(assign != null){
            tipo = 1;
        }
        else if(read != null){
            tipo = 2;
        }
        else if(write != null){
            tipo = 3;
        }
        else if(return_stmt != null){
            tipo = 4;
        }
        else if(if_stmt != null){
            tipo = 5;
        }
        else if(for_stmt != null){
            tipo = 6;
        }
    }
   
    public void genC(PW pw){
       
        if(tipo == 0){
           callexpr.genC(pw);
        }
        else if(tipo == 1){
            assign.genC(pw);
        }
        else if(tipo == 2){
            read.genC(pw);
        }
        else if(tipo == 3){
            write.genC(pw);
        }
        else if(tipo == 4){
            ret.genC(pw);
        }
        else if(tipo == 5){
            if_stmt.genC(pw);
        }
        else if(tipo == 6){
            for_stmt.genC(pw);
        }
   } 
}
