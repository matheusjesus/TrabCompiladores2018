package AST;

import Lexer.Symbol;

public class If_stmt{
    private Cond cond;
    private Stmt_list stmt_list;
    private Else_part else_part;
     
    public If_stmt(Cond cond, Stmt_list stmt_list, Else_part else_part){
        this.cond = cond;
        this.stmt_list = stmt_list;
        this.else_part = else_part;
    }
    //if_stmt -> IF ( cond ) THEN stmt_list else_part ENDIF

    public void genC(PW pw) {
        pw.print("if (");
        cond.genC(pw);
        pw.println(") {");
        pw.add();
        stmt_list.genC(pw);
        if (else_part == null){
            pw.println("}");
            pw.sub();
        }
        else {
            pw.println("} else {");
            else_part.genC(pw);
            pw.println("}");
            pw.sub();
        }
    }
}
