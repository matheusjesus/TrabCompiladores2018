package AST;

import Lexer.Symbol;

public class VariableInt extends Variable{
    private int intnum;
    
    public VariableInt(String nome, Symbol tipo) {
        super(nome, tipo);
    }
    
    public void setNumero(int num){
        this.intnum = num;
    }
    
    public int getNumero(){
        return this.intnum;
    }
}
