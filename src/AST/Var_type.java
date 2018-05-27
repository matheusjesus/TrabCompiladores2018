package AST;

import Lexer.*;

public class Var_type {
    private final String nome;
    private final Symbol tipo;
    
    public Var_type(String nome, Symbol tipo) {
        this.nome = nome;
        this.tipo = tipo;
    }
    
    public Symbol getTipo(){
        return this.tipo;
    }
    
    public String getNome(){
        return this.nome;
    }
    
    public void genC(PW pw){
        pw.print(getNome(), false);
    }
}