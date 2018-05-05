package AST;

import java.util.ArrayList;

public class Call_expr{
    private Id id;
    private ArrayList<Expr> expr_list;
    
    public Call_expr(Id id){
        this.id = id;
        this.expr_list = null;
    }
    
    public Call_expr(Id id, ArrayList<Expr> expr_list){
        this.id = id;
        this.expr_list = expr_list;
    }

    public void genC(PW pw, boolean ident) {
        pw.print(id.getId()+"(", ident);
        if(expr_list != null) {
            for(int i = 0; i < expr_list.size() - 1; i++){
                expr_list.get(i).genC(pw);
                pw.print(", ", false);
                
            }
            expr_list.get(expr_list.size()-1).genC(pw);
        }
        //pw.println(")", false);
        if(ident == true)
            pw.println(");", false);
        else
            pw.print(")", false);
    }

    public Id getId() {
        return id;
    }

    public ArrayList<Expr> getList(){
        return this.expr_list;
    }
}
