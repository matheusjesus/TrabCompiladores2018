package AST;

import Lexer.*;

public class Var_type {
    private String nome;
    private Symbol tipo;
    
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
}