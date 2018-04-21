package AST;

public class For_stmt{
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

    public void genC(PW pw) {
        pw.print("for( ");
        if (assign1 != null)
            assign1.genC(pw);
        pw.print("; ");
        if(cond != null)
            cond.genC(pw);
        pw.print(";");
        if(assign2 != null)
            assign2.genC(pw);
        pw.print(") {");
        pw.add();
        if(stmt_list != null)
            stmt_list.genC(pw);
        pw.print("}");
        pw.sub();

    }
}
