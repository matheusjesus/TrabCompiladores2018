package AST;

public class Primary {
    private Expr expr = null;
    private Id id = null;
    private int intege;
    private float flutuante;
    private int tipo; //1 para expr, 2 para id, 3 para int, 4 para float
    
    public Primary(Expr expr){
        this.expr = expr;
        this.id = null;
        this.tipo = 1;
    }
    
    public Primary(Id id){
        this.expr = null;
        this.id = id;
        this.tipo = 2;
    }
    
    public Primary(int num){
        this.expr = null;
        this.id = null;
        this.intege = num;
        this.tipo = 3;
    }
    
    public Primary(float num){
        this.expr = null;
        this.id = null;
        this.flutuante = num;
        this.tipo = 4;
    }
    
    public void genC(PW pw){
        switch(tipo){
            case 1:
                pw.print("(");
                expr.genC(pw);
                pw.print(")");
                break;
            case 2:
                pw.print(id.getId());
                break;
            case 3:
                System.out.println("/////////////////////");
                pw.print(""+intege);
                break;
            case 4:
                pw.print(""+flutuante);
                break;
        }
    } 
}
