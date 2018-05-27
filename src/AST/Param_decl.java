package AST;

import Lexer.Symbol;

public class Param_decl {
    private Symbol tipo;
    private String nome;
    
    public Param_decl(Symbol tipo, String nome){
        this.tipo = tipo;
        this.nome = nome;
    }
    
    public Symbol getTipo(){
        return this.tipo;
    }
    
    /*
    param_decl -> var_type id
    genC(): printar o tipo e o nome
    */
    public void genC(PW pw) {
        pw.print(tipo.toString() + " " + nome, false);
    }
}
