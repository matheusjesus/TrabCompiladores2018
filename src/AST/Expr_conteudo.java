package AST;

import Lexer.Symbol;

public class Expr_conteudo {
    int tipo;
    Id id;
    Var_type var;
    int numint;
    float numfloat;
    Symbol operador;
    Call_expr call;
    
    public Expr_conteudo(Id id){
        this.id = id;
        tipo = 1;    
    }

    public Expr_conteudo(Var_type var){
        this.var = var;
        tipo = 2; 
    }
    
    public Expr_conteudo(int numint){
        this.numint = numint;
        tipo = 3;    
    }
    
    public Expr_conteudo(float numfloat){
        this.numfloat = numfloat;
        tipo = 4;    
    }
    
    public Expr_conteudo(Symbol operador){
        this.operador = operador;
        tipo = 5;
    }
    
    public Expr_conteudo(Call_expr call){
        this.call = call;
        tipo = 6;
    }
    
    public Symbol getTipo(){
        switch(tipo){
            case 1:
                return Symbol.IDENT;
            case 3:
                return Symbol.INT;
            case 4:
                return Symbol.FLOAT;
        }
        
        return null;
    }
    
    public void genC(PW pw){
        switch (tipo){
            case 1:
                pw.print(""+id.getId(), false);
                break;
            case 2:
                var.genC(pw);
                break;
            case 3:
                pw.print(""+numint, false);
                break;
            case 4:
                pw.print(""+numfloat, false);
                break;
            case 5:
                pw.print(" "+operador+" ", false);
                break;
            case 6:
                call.genC(pw, false);
                break;
        }
    }
}
