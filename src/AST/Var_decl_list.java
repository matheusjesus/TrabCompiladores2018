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
            //pw.print("int ");
            //intlist.get(0).genC();
            
            for(i=1;i<intlist.size();i++){
                //pw.print(", ");
                //intlist.get(i).genC();
            }
            
//            pw.println(";");
        }
        
        if(!(floatlist.isEmpty())){
            //pw.print("float ");
            //floatlist.get(0).genC();
            
            for(i=1;i<floatlist.size();i++){
                //pw.print(", ");
                //floatlist.get(i).genC();
            }
            
            //pw.println(";\n");
        }
    }
}
