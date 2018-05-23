package AST;

import Lexer.Symbol;
import AuxComp.*;

public class Expr {
    private Expr_conteudo conteudo;
    private Expr expresq, exprdir;
    private Symbol tipo = null;
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
    
    public Symbol getTipo(SymbolTable symtable){
        Symbol tipoesq = null, tipodir = null, tipoconteudo;
        Call_expr call;
        Func_aux funcaux;
        
        if(expresq != null){
            tipoesq = expresq.getTipo(symtable);
            tipodir = expresq.getTipo(symtable);
        }
        
        tipoconteudo = conteudo.getTipo();
        call = conteudo.call;
        
        if(tipoesq == Symbol.FLOAT || tipodir == Symbol.FLOAT || tipoconteudo == Symbol.FLOAT){
            this.tipo = Symbol.FLOAT;        
        }else if (tipoconteudo == Symbol.IDENT){
            this.tipo = (Symbol) symtable.getInLocal(conteudo.getId());
            
            if(this.tipo == null){
                this.tipo = (Symbol) symtable.getInGlobal(conteudo.getId());
            }
            
            if(this.tipo == null){
                System.out.println("Errinho esquisito!");
            }
        }else if(call != null){
            funcaux = (Func_aux) symtable.getInGlobal(call.getId().getId());
            
            if(funcaux == null){
                System.out.println("funcao nao encontrada!");
            }
            
            this.tipo = funcaux.getTipo();
        }
        else if(tipoconteudo == Symbol.INT){
            this.tipo = tipoconteudo;
        }
        
        return this.tipo;
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
