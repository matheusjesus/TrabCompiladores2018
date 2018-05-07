package AST;

import Lexer.Symbol;

public class If_stmt extends Stmt{
    private Cond cond;
    private Stmt_list stmt_list;
    private Else_part else_part;
     
    public If_stmt(Cond cond, Stmt_list stmt_list, Else_part else_part){
        this.cond = cond;
        this.stmt_list = stmt_list;
        this.else_part = else_part;
    }
    //if_stmt -> IF ( cond ) THEN stmt_list else_part ENDIF

    @Override
    public void genC(PW pw, boolean ident) {
        pw.print("if (", true);
        cond.genC(pw);
        pw.println(") {", false);
        pw.add();
        stmt_list.genC(pw);
        pw.sub();
        if (else_part == null){
         //   pw.sub();
            pw.println("}", true);
        }
        else {
            pw.print("} ", true);
            else_part.genC(pw);
//            pw.println("}", true);
        }
    }
}
