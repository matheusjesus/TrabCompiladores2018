package AST;
import Lexer.Symbol;
import java.util.*;
        
public class Var_decl_list {
    ArrayList<VariableInt> intlist;
    ArrayList<VariableFloat> floatlist;
    
    public Var_decl_list(ArrayList<Variable> lv){
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
    
    public void genC(){
        int i;

        System.out.println("\n\nVAR_DECL_LIST:");
        
        if(!(intlist.isEmpty())){
            System.out.print("INT ");
            System.out.print(intlist.get(0).getNome());
            
            for(i=1;i<intlist.size();i++){
                System.out.print(", " + intlist.get(i).getNome());
            }
            
            System.out.println(";");
        }
        
        if(!(floatlist.isEmpty())){
            System.out.print("FLOAT ");
            System.out.print(floatlist.get(0).getNome());
            
            for(i=1;i<floatlist.size();i++){
                System.out.print(", " + floatlist.get(i).getNome());
            }
            
            System.out.println(";\n\n");
        }
    }
}
