package AST;

import Lexer.Symbol;

public class Func_decl {
    Symbol tipo;
    String nome;
    Param_decl_list paramlist;
    Func_body corpo;
    
    public Func_decl(Symbol tipo, String nome, Param_decl_list paramlist, Func_body corpo){
        this.tipo = tipo;
        this.nome = nome;
        this.paramlist = paramlist;
        this.corpo = corpo;
    }
    
    public void genC(PW pw){
        /* chamar genC() de Param_decl_list na hora dos parametros!*/
        pw.print(tipo.toString() + " "+nome+"(");
        paramlist.genC(pw);
        pw.println(") {");
        pw.add();
        corpo.genC(pw);
        pw.print("\n");
        pw.println("}");
        pw.sub();
    }
}
