package AST;

public class Else_part {
    private Stmt_list stmt_list;
    
    public Else_part(Stmt_list stmt_list){
        this.stmt_list = stmt_list;
    }
    
    public void genC(PW pw){
        if(stmt_list != null){
            pw.println("else");
            pw.println("{");
            pw.add();
            stmt_list.genC(pw);
            pw.println("}");
            pw.sub();
        }
            
    }
}
