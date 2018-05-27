package AST;

import Lexer.Symbol;

public class Func_aux {
    private Symbol tipo;
    private Param_decl_list decl_list;
    
    public Func_aux(Symbol tipo, Param_decl_list decl_list){
        this.tipo = tipo;
        this.decl_list = decl_list;
    }
    
    public Symbol getTipo(){
        return this.tipo;
    }
    
    public Param_decl_list getParam(){
        return this.decl_list;
    }
    
    
}
