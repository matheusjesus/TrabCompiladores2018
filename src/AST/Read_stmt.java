package AST;

import AuxComp.SymbolTable;
import Lexer.Symbol;
import java.util.ArrayList;

public class Read_stmt extends Stmt{
    private ArrayList<Id> id_list;
    private char typelist[];
    
    public Read_stmt(ArrayList<Id> id_list, SymbolTable symtable){
        int i;
        
        typelist = new char[id_list.size()];
        
        this.id_list = id_list;
        
        for(i = 0; i < id_list.size(); i++){
            if(symtable.getInLocal(id_list.get(i).getId()) ==  Symbol.INT){
                this.typelist[i] = 'd';
            }
            else{
                this.typelist[i] = 'f';
            }
        }
    }
    
    //read_stmt -> READ ( id_list );
    @Override
    public void genC(PW pw, boolean ident) {
        int i;
        for(i = 0; i < id_list.size(); i++){
            pw.println("scanf(\"%"+ typelist[i] +"\", &"+id_list.get(i).getId()+");", true);
        }

    }

    public Symbol setTipo() {
        return Symbol.READ;
    }
}
