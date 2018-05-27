package AST;

import Lexer.Symbol;
import java.util.ArrayList;

public class Func_decl {
    private Symbol tipo;
    private String nome;
    private Param_decl_list paramlist;
    private Func_body corpo;
    private ArrayList<Var_type> varlist;
    
    public Func_decl(Symbol tipo, String nome, Param_decl_list paramlist, Func_body corpo){
        this.tipo = tipo;
        this.nome = nome;
        this.paramlist = paramlist;
        this.corpo = corpo;
    }
    
    public String getName(){
        return this.nome;
    }
    
    public Symbol getTipo(){
        return this.tipo;
    }
    
    public void genC(PW pw){
        /* chamar genC() de Param_decl_list na hora dos parametros!*/
        pw.print(tipo.toString() + " "+nome+"(", true);
        if(paramlist != null)
            paramlist.genC(pw);
        pw.println(") {", false);
        pw.add();
        corpo.genC(pw);
        pw.print("\n", false);
        pw.sub();
        pw.println("}", true);
        pw.print("\n", false);
    }
}
