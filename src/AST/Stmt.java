package AST;

import Lexer.Symbol;

public class Stmt {
    private Symbol tipo;
    private Read_stmt read;
    private Assign_expr assign;
    private Write_stmt write;
    private Return_stmt ret;
    private If_stmt if_stmt;
    private For_stmt for_stmt;
   
    public void genC(PW pw){
       if(tipo.equals(Symbol.READ))
            read.genC(pw);
       else if(tipo.equals(Symbol.ASSIGN))
            assign.genC(pw);
       else if(tipo.equals(Symbol.WRITE))
            write.genC(pw);
       else if(tipo.equals(Symbol.RETURN))
            ret.genC(pw);
       else if(tipo.equals(Symbol.IF))
            if_stmt.genC(pw);
       else if(tipo.equals(Symbol.FOR))
            for_stmt.genC(pw);
   } 
}
