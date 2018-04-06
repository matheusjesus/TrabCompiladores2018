package AST;

import Lexer.*;

public class Variable {
    private String nome;
    private Symbol tipo;
    
    public Variable(String nome, Symbol tipo) {
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