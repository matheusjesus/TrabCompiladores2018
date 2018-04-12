package AST;

import java.util.ArrayList;

public class String_decl_list {
    ArrayList<VariableStr> strlist;
    
    public String_decl_list(ArrayList<VariableStr> strlist){
        this.strlist = strlist;
    }
    
    public void genC(PW pw){
        //printar char *str1, *str2, *str3...;
        //chamar genC(); de cada VariableStr da lista;
    }
}
