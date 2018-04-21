package AST;
import java.util.*;

public class Func_declarations {
    ArrayList<Func_decl> funcoes;

    public Func_declarations(ArrayList<Func_decl> funcoes){
        this.funcoes = funcoes;
    }

    //func_declarations -> func_decl {func_decl_tail}

    /*for: printar funcao por funcao, declaracao e depois body */
    public void genC(PW pw){
        for(Func_decl f : funcoes){
            f.genC(pw);
        }
    }
}
