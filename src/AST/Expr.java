package AST;

public class Expr {
    private Expr_conteudo conteudo;
    private Expr expresq, exprdir;
    private boolean parenteses;
    
    public Expr(Expr_conteudo conteudo, Expr expresq, Expr exprdir){
        this.conteudo = conteudo;
        this.expresq = expresq;
        this.exprdir = exprdir;
        this.parenteses = false;
    }
    
    public Expr(Expr_conteudo conteudo){
        this.conteudo = conteudo;
        this.expresq = null;
        this.exprdir = null;
        this.parenteses = false;
    }

 
    public void genC(PW pw){
        if(parenteses){
            pw.print("(", false);
        }
        
        if(expresq != null && exprdir != null){
            expresq.genC(pw);
            conteudo.genC(pw);
            exprdir.genC(pw);
        }
        else{
            conteudo.genC(pw);
        }
        
        if(parenteses){
            pw.print(")", false);
        }
    }
    
    public void setPar(boolean par){
        this.parenteses = par;
    }
}
