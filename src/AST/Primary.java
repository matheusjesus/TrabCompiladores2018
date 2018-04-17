package AST;

class Primary {
    private Expr expr;
    private Id id;
    private int integer;
    private float flutuante;
    private int tipo;
    
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
        this.integer = num;
        this.tipo = 3;
    }
    
    public Primary(float num){
        this.expr = null;
        this.id = null;
        this.flutuante = num;
        this.tipo = 4;
    }
    
    public void genC(){
        //perguntar cada opcao de escrita de qual tipo eh
    } 
}
