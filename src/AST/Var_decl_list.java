package AST;
import Lexer.Symbol;
import java.util.*;
        
public class Var_decl_list {
    ArrayList<VariableInt> intlist;
    ArrayList<VariableFloat> floatlist;
    
    public Var_decl_list(ArrayList<Var_type> lv){
        int i;
        VariableInt auxint;
        VariableFloat auxfloat;
        
        intlist = new ArrayList();
        floatlist = new ArrayList();        
        
        for(i=0;i<lv.size();i++){
            if(lv.get(i).getTipo() == Symbol.INT){
                auxint = new VariableInt(lv.get(i).getNome(), lv.get(i).getTipo());
                intlist.add(auxint);
            }
            else if(lv.get(i).getTipo() == Symbol.FLOAT){
                auxfloat = new VariableFloat(lv.get(i).getNome(), lv.get(i).getTipo());
                floatlist.add(auxfloat);
            }
        }
    }
    
    public void genC(PW pw){
        int i;
      
        if(!(intlist.isEmpty())){
            pw.print("int ", true);
            intlist.get(0).genC(pw);

            for(i=1;i<intlist.size();i++){
                pw.print(", ", false);
                intlist.get(i).genC(pw);
            }

            pw.println(";", false);
        }
        
        if(!(floatlist.isEmpty())){
            pw.print("float ", true);
            floatlist.get(0).genC(pw);
            
            for(i=1;i<floatlist.size();i++){
                pw.print(", ", false);
                floatlist.get(i).genC(pw);
            }
            
            pw.println(";", false);
        }
    }
}
