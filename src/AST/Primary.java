package AST;

public class Primary {
    private Expr expr = null;
    private Id id = null;
    private int inteiro;
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
        this.inteiro = num;
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
                pw.print("(", false);
                expr.genC(pw);
                pw.print(")", false);
                break;
            case 2:
                pw.print(id.getId(), false);
                break;
            case 3:
                pw.print(""+inteiro, false);
                break;
            case 4:
                pw.print(""+flutuante, false);
                break;
        }
    }
    
    public int getTipo(){
        return this.tipo;
    }
    
    public Expr getExpr(){
        return this.expr;
    }
    
    public Id getId(){
        return this.id;
    }
    
    public int getInt(){
        return this.inteiro;
    }
    
    public float getFloat(){
        return this.flutuante;
    }
}
