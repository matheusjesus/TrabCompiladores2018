package AST;

import Lexer.Symbol;

public class Param_decl {
    Symbol tipo;
    String nome;
    
    public Param_decl(Symbol tipo, String nome){
        this.tipo = tipo;
        this.nome = nome;
    }
    
    //genC(): printar o tipo e o nome
}
