package AST;

import Lexer.Symbol;

public class Param_decl {
    Symbol tipo;
    String nome;
    
    public Param_decl(Symbol tipo, String nome){
        this.tipo = tipo;
        this.nome = nome;
    }
    /*
    param_decl -> var_type id
    genC(): printar o tipo e o nome
    */
    public void genC(PW pw) {
        pw.print(tipo.toString() + " " + nome);
    }
}
