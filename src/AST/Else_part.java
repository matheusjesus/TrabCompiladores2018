package AST;

public class Else_part {
    private final Stmt_list stmt_list;
    
    public Else_part(Stmt_list stmt_list){
        this.stmt_list = stmt_list;
    }
    
    public void genC(PW pw){
        if(stmt_list != null){
//            pw.print("\n", false);
            pw.print("else", false);
            pw.println("{", false);
            pw.add();
            stmt_list.genC(pw);
            pw.sub();
            pw.println("}", true);
        }
            
    }
}
