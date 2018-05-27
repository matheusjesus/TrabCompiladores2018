package AST;

import Lexer.Symbol;

public class VariableFloat extends Var_type{
    private float floatnum;
    
    public VariableFloat(String nome, Symbol tipo) {
        super(nome, tipo);
    }
    
    public void setNumero(float num){
        this.floatnum = num;
    }
    
    public float getNumer(){
        return this.floatnum;
    }
}