package AST;
import Lexer.Symbol;
import java.util.*;
        
public class Var_decl_list {
    ArrayList<VariableInt> intlist = null;
    ArrayList<VariableFloat> floatlist = null;
    
    public Var_decl_list(ArrayList<Var_type> lv){
        int i;
        VariableInt auxint = null;
        VariableFloat auxfloat = null;
        
        this.intlist = new ArrayList();
        this.floatlist = new ArrayList();        
        for(Var_type v : lv){
            if(v.getTipo() == Symbol.INT){
                auxint = new VariableInt(v.getNome(), v.getTipo());
                this.intlist.add(auxint);
            }
            
            if(v.getTipo() == Symbol.FLOAT){
                auxfloat = new VariableFloat(v.getNome(), v.getTipo());
                this.floatlist.add(auxfloat);
            }
        }
        
    }
    
    public void genC(PW pw){
        int i;
        if(!(intlist.isEmpty())){
            pw.print("int ", true);
            for(i = 0; i < intlist.size() - 1; i ++){
                intlist.get(i).genC(pw);
                pw.print(", ", false);
            }
            intlist.get(intlist.size()-1).genC(pw);
            pw.println(";", false);
        }
        
        if(!(floatlist.isEmpty())){
            pw.print("float ", true);
            floatlist.get(0).genC(pw);
            
            for(i=1; i < floatlist.size(); i++){
                pw.print(", ", false);
                floatlist.get(i).genC(pw);
            }
            
            pw.println(";\n", false);
        }
    }
}
