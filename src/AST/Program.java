package AST;

public class Program {
    private Id ident;
    private Pgm_body body;
    
    public Program(Id ident, Pgm_body body){
        this.ident = ident;
        this.body = body;
    }
    
    public void genC(){
        System.out.println("*********INICIO*********\n\n");
        ident.genC();
        body.genC();
        System.out.println("\n*********FIM*********\n");
    }
}
