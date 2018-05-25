package AST;

import AuxComp.SymbolTable;
import Lexer.Symbol;
import java.util.ArrayList;


public class Write_stmt extends Stmt{
    private ArrayList<Id> id_list;
    private char typelist[];
    
    public Write_stmt(ArrayList<Id> id_list, SymbolTable symtable){
        int i;
        Symbol tipo;
        
        typelist = new char[id_list.size()];
        
        this.id_list = id_list;
        
        for(i = 0; i < id_list.size(); i++){
            tipo = (Symbol) symtable.getInLocal(id_list.get(i).getId());
            if(tipo ==  Symbol.INT){
                this.typelist[i] = 'd';
            }
            else if(tipo == Symbol.FLOAT){
                this.typelist[i] = 'f';
            }else{
                this.typelist[i] = 's';
            }
        }    }
    
    @Override
    public void genC(PW pw, boolean ident) {
        int i;
        for(i=0;i<id_list.size();i++){
            pw.println("print(\"%"+ typelist[i] +"\", "+id_list.get(i).getId()+");", true);
        }
    }
}
