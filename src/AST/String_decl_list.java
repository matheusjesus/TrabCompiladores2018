package AST;

import java.util.ArrayList;

public class String_decl_list {
    private ArrayList<VariableStr> strlist;
    
    public String_decl_list(ArrayList<VariableStr> strlist){
        this.strlist = strlist;
    }
    /*
    printar char *str1, *str2, *str3...;
    chamar genC(); de cada VariableStr da lista;
    */
    public void genC(PW pw){
        for(VariableStr str : strlist){
            pw.println("char * " + str.getNome() + ";", true);
            str.genC(pw);
        }
        
    }
}
