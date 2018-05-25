import Lexer.*;
import Error.*;
import AST.*;
import java.util.*;
import AuxComp.*;

public class Compiler {
    private Lexer lexer;
    private CompilerError error;
    private SymbolTable symtable;

    // para geracao de codigo
    public static final boolean GC = false; 

    public Program compile( char []p_input ) {
        error = new CompilerError(null);
        lexer = new Lexer(p_input, error);
        error.setLexer(lexer);
        lexer.nextToken();
        
        symtable = new SymbolTable();
        
        Program p = program();
        return p;
    }
   
    
/******************** PROGRAM ********************/
    
    // program -> PROGRAM id BEGIN pgm_body END
    public Program program(){
        if(lexer.token != Symbol.PROGRAM){
            error.signal("O programa deve comecar com PROGRAM! Linha: \n" + lexer.getLineNumber());
        }
        lexer.nextToken();
        Id ident = id();
        
        if(lexer.token != Symbol.BEGIN){
            error.signal("Declaracao BEGIN nao encontrada! Linha: " + lexer.getLineNumber());
        }
        lexer.nextToken();
        Pgm_body body = pgm_body();
        
        if(lexer.token != Symbol.END){
            error.signal("Declaracao END nao encontrada ao fim do programa! Linha: " + lexer.getLineNumber());
        }
        lexer.nextToken();
        
        return new Program(ident, body);
    }
    
    // id -> IDENTIFIER
    public Id id(){
        if(lexer.token != Symbol.IDENT){
            error.signal("Identificador fora dos padroes! Linha: " + lexer.getLineNumber());
        }
        Id ident = new Id(lexer.getStringValue());
        lexer.nextToken();
        
        return ident;
    }

    //pgm_body -> decl func_declarations
    public Pgm_body pgm_body(){
        Decl decl = decl(true);
        
        Func_declarations fdecl = func_declarations();
        
        return new Pgm_body(decl, fdecl);
    }
    
    //decl -> string_decl_list {decl} | var_decl_list {decl} | empty
    //global == 0 para variavel local . global == 1 variavel global
    public Decl decl(boolean global){
        String_decl_list strlist = null;
        Var_decl_list varlist = null;
        do{
            //decl();
            if(lexer.token == Symbol.STRING){
                strlist = string_decl_list(global);
            }
        
            if(lexer.token == Symbol.INT || lexer.token == Symbol.FLOAT){
                varlist = var_decl_list(global);
            }
        } while(lexer.token == Symbol.STRING || lexer.token == Symbol.INT || lexer.token == Symbol.FLOAT);

        
        return new Decl(strlist, varlist);
    }


/******************** GLOBAL STRING DECLARATION ********************/
    
    //string_decl_list -> string_decl {string_decl_tail}
    public String_decl_list string_decl_list(boolean global){
        ArrayList<VariableStr> strlist;
        strlist = string_decl(global);
        
        if(lexer.token == Symbol.STRING){
            strlist = string_decl_tail(strlist, global);
        }
        
        return new String_decl_list(strlist);
    }
    
    //string_decl -> STRING id := str ; | empty
    public ArrayList<VariableStr> string_decl(boolean global){
        ArrayList<VariableStr> strlist = new ArrayList();
        VariableStr novo;
        Id idnovo = null;
        String strnovo = null;
        
        if(lexer.token == Symbol.STRING){
            lexer.nextToken();

            idnovo = id();
           
            if(lexer.token != Symbol.ASSIGN){
                error.signal("Um sinal de atribuicao era esperado na linha " + lexer.getLineNumber());
            }
            lexer.nextToken();
            
            strnovo = str();

            if(lexer.token != Symbol.SEMICOLON){
                error.signal("Um ponto e virgula era esperado antes da linha " + lexer.getLineNumber());
            }
            lexer.nextToken();
        }
        
        novo = new VariableStr(idnovo.getId(), strnovo);

        if(global){ 
            if(symtable.getInGlobal(idnovo.getId()) != null)
                error.signal("Variavel ja declarada. Linha "+lexer.getCurrentLine());
            
            symtable.putInGlobal(idnovo.getId(), Symbol.STRING);
        } else {
            if(symtable.getInLocal(idnovo.getId()) != null)
                error.signal("Variavel ja declarada. Linha " + lexer.getCurrentLine());
          
            symtable.putInLocal(idnovo.getId(), Symbol.STRING);
        }
        strlist.add(novo);
        
        return strlist;
    }
    
    //str -> STRINGLITERAL
    public String str(){
        String str;
        
        if(lexer.token != Symbol.STRINGLITERAL){
            error.signal("Esperado um STRINGLITERAL na linha " + lexer.getLineNumber());
        }
        
        str = lexer.getStringValue();
        
        lexer.nextToken();
        
        return str;
    }
    
    //string_decl_tail -> string_decl {string_decl_tail}
    public ArrayList<VariableStr> string_decl_tail(ArrayList<VariableStr> strlist, boolean global){
        VariableStr novo;
        Id idnovo;
        String strnovo;
        
        while(lexer.token == Symbol.STRING){
            lexer.nextToken();
            
            idnovo = id();
            
            if(lexer.token != Symbol.ASSIGN){
                error.signal("Um sinal de atribuicao era esperado na linha " + lexer.getLineNumber());
            }
            lexer.nextToken();
            
            strnovo = str();

            if(lexer.token != Symbol.SEMICOLON){
                error.signal("Um ponto e virgula era esperado antes da linha " + lexer.getLineNumber());
            }
            lexer.nextToken();
            
            novo = new VariableStr(idnovo.getId(), strnovo);
            
            if(global){ 
                if(symtable.getInGlobal(idnovo.getId()) != null)
                    error.signal("Variavel ja declarada. Linha "+lexer.getCurrentLine());

                symtable.putInGlobal(idnovo.getId(), Symbol.STRING);
            } else {
                if(symtable.getInLocal(idnovo.getId()) != null)
                    error.signal("Variavel ja declarada. Linha " + lexer.getCurrentLine());

                symtable.putInLocal(idnovo.getId(), Symbol.STRING);
            }
                strlist.add(novo);
        }
        
        return strlist;
    }
    

/******************** VARIABLE DECLARATION ********************/    
    
    //var_decl_list -> var_decl {var_decl_tail}
    public Var_decl_list var_decl_list(boolean global){
        ArrayList<Var_type> lv;
        Var_decl_list varlist;
        
        lv = var_decl(global);
        lv = var_decl_tail(lv, global);
        
        varlist = new Var_decl_list(lv);
        return varlist;
    }
    
    //var_decl -> var_type id_list ; | empty
    public ArrayList<Var_type> var_decl(boolean global){
        Symbol tipo;
        ArrayList<Id> idlist;
        ArrayList<Var_type> lv = new ArrayList();
        Var_type v;
        
        while(lexer.token == Symbol.FLOAT || lexer.token == Symbol.INT){
            tipo = var_type();

            idlist = id_list();

            if(lexer.token != Symbol.SEMICOLON){
                error.signal("Um ponto e virgula era esperado antes da linha " + lexer.getLineNumber());
            }
            lexer.nextToken();
            
            for(Id i : idlist){
                v = new Var_type(i.getId(), tipo);
                
                //analisador lexico
                if(global){ 
                    if(symtable.getInGlobal(i.getId()) != null)
                        error.signal("Variavel "+i.getId()+" ja declarada.!");

                    symtable.putInGlobal(i.getId(), v.getTipo());
                } else {
                    if(symtable.getInLocal(i.getId()) != null)
                        error.signal("Variavel "+ i.getId() +" ja declarada!");
                    
                    symtable.putInLocal(i.getId(), v.getTipo());
                }
               
                lv.add(v);
            }
        }
        
        return lv;
    }
    
    //var_type -> FLOAT | INT
    public Symbol var_type(){
        if(lexer.token == Symbol.FLOAT){
            lexer.nextToken();
            return Symbol.FLOAT;
        }
        else if(lexer.token == Symbol.INT){
            lexer.nextToken();
            return Symbol.INT;
        }
        else{
           error.signal("Uma declaracao de INT ou FLOAT era esperada na linha " + lexer.getLineNumber());
        }
        return Symbol.INT;
    }
    
    //any_type -> var_type | VOID
    public Symbol any_type(){
        if(lexer.token != Symbol.VOID){
            return var_type();
        }
        else if(lexer.token == Symbol.VOID){
            lexer.nextToken();
            
            return Symbol.VOID;
        }
        else{
           error.signal("Uma declaracao de VOID era esperada na linha " + lexer.getLineNumber());
        }
        
        return null;
    }
    
    //id_list -> id id_tail
    public ArrayList<Id> id_list(){
        ArrayList<Id> vl = new ArrayList();
        
        vl.add(id());
        vl = id_tail(vl);
        
        return vl;
    }
    
    //id_tail -> , id id_tail | empty
    public ArrayList<Id> id_tail(ArrayList<Id> vl){
        int comma = 0;
        
        while(lexer.token == Symbol.COMMA){
            comma = 1;
            lexer.nextToken();
            
            if(lexer.token == Symbol.IDENT){
                if(comma == 0){
                    error.signal("Uma virgula era esperada na linha " + lexer.getLineNumber());
                }
                else{
                    vl.add(id());
                }
            }
        }
        
        return vl;
    }
    
    //var_decl_tail -> var_decl {var_decl_tail}
    public ArrayList<Var_type> var_decl_tail(ArrayList<Var_type> lv, boolean global){
        ArrayList<Var_type> lvaux;
        Symbol s;
        //int i;
        
        while(lexer.token == Symbol.FLOAT || lexer.token == Symbol.INT){
            lvaux = var_decl(true);
            for(Var_type v : lvaux){
                lv.add(v);
            }
            
        }
        
        return lv;
    }
    

/******************** FUNCTION PARAMETER LIST ********************/      
    
    //param_decl_list -> param_decl param_decl_tail
    public Param_decl_list param_decl_list(){
        Param_decl_list paramlist;
        ArrayList<Param_decl> parlist;
        
        parlist = param_decl();
        parlist = param_decl_tail(parlist);
        
        return new Param_decl_list(parlist);
    }
    
    //param_decl -> var_type id
    public ArrayList<Param_decl> param_decl(){
        ArrayList<Param_decl> parlist = new ArrayList();
        Param_decl novo;
        Symbol tipo;
        Id id;
        
        tipo = var_type();
        id = id();

        symtable.putInLocal(id.getId(), tipo);
        
        novo = new Param_decl(tipo, id.getId());
        parlist.add(novo);
        
        return parlist;
    }
   
    //param_decl_tail -> , param_decl param_decl_tail | empty
    public ArrayList<Param_decl> param_decl_tail(ArrayList<Param_decl> parlist){
        int comma = 0;
        Param_decl novo;
        Symbol tipo;
        Id id;
        
        if(lexer.token == Symbol.COMMA){
            comma = 1;
            lexer.nextToken();
        }
        
        while(lexer.token == Symbol.FLOAT || lexer.token == Symbol.INT){
            if(comma == 0){
                error.signal("Uma virgula era esperada na linha " + lexer.getLineNumber());
            }
            
            tipo = var_type();
            id = id();

            if(symtable.getInLocal(id.getId()) != null){
                error.signal("Variavel ja declarada nesta funcao!" + lexer.getCurrentLine());
            }
            symtable.putInLocal(id.getId(), tipo);
            
            novo = new Param_decl(tipo, id.getId());
            parlist.add(novo);
        }
        
        return parlist;
    }
    
    
/******************** FUNCTION DECLARATIONS ********************/      

    //func_declarations -> func_decl {func_decl_tail}
    public Func_declarations func_declarations(){
        ArrayList<Func_decl>         funcoes = new ArrayList();
        boolean main;
        
        main = func_decl(funcoes);
        
        if(funcoes == null){
            System.out.println("erroooo!");
        }
        
        if(lexer.token == Symbol.FUNCTION){
            func_decl_tail(funcoes, main);
        }
        
        if(symtable.getInGlobal("main") == null){
            error.signal("Funcao 'main' nao encontrada no programa.");
        }
        
        return new Func_declarations(funcoes);
    }
    
    //func_decl -> FUNCTION any_type id ({param_decl_list}) BEGIN func_body END | empty
    public boolean func_decl(ArrayList<Func_decl> funcoes){
        Func_decl novo;
        Id id;
        Symbol tipo;
        Param_decl_list paramlist = null;
        Func_body corpo;
        Func_aux func_aux;
        boolean main = false;
                
        if(lexer.token == Symbol.FUNCTION){
            lexer.nextToken();

            tipo = any_type();
            id = id();
            
            symtable.putInLocal("funcao atual", tipo);
            
            //fazemos verificacao do nome na tabela hash.
            if(symtable.get(id.getId()) != null){
                error.signal("Funcao ja declarada! Linha " + lexer.getLineNumber());
            }
            
            
            //se for main, verifica se nao tem parametro
            if(id.getId().compareTo("main") == 0){
                if(lexer.token != Symbol.LPAR){
                    error.signal("Espera-se '('! Linha " + lexer.getLineNumber());
                }
                lexer.nextToken();

                if(lexer.token != Symbol.RPAR){
                    error.signal("Funcao 'main' nao suporta parametros! Espera-se ')'. Linha " + lexer.getLineNumber());
                }
                lexer.nextToken();
                main = true;
            }
            //nao é main
            else {    
                if(lexer.token != Symbol.LPAR){
                    error.signal("Os parametros da funcao devem estar entre parenteses! Linha " + lexer.getLineNumber());
                }
                lexer.nextToken();

                if(lexer.token == Symbol.INT || lexer.token == Symbol.FLOAT){
                    paramlist = param_decl_list();
                }

                if(lexer.token != Symbol.RPAR){
                    error.signal("Os parametros da funcao devem estar entre parenteses! Linha " + lexer.getLineNumber());
                }
                lexer.nextToken();
            }
            
            
            //colocando funcaux na hash global
            func_aux = new Func_aux(tipo, paramlist);
            symtable.putInGlobal(id.getId(), func_aux);
            
            
            //corpo
            if(lexer.token != Symbol.BEGIN){
                error.signal("A funcao deve comecar com BEGIN! Linha " + lexer.getLineNumber());
            }
            lexer.nextToken();

            corpo = func_body();

            if(lexer.token != Symbol.END){
                error.signal("Esperado uma declaracao END na linha " + lexer.getLineNumber());
            }
            lexer.nextToken(); 
            
            
            //limpar tabela de variaveis locais
            symtable.removeLocalIdent();
            
            novo = new Func_decl(tipo, id.getId(), paramlist, corpo);
            
            funcoes.add(novo);
        }
        
        return main;
    }
    
    //func_decl_tail -> func_decl {func_decl_tail}
    public void func_decl_tail(ArrayList<Func_decl> funcoes, boolean main){
        Func_decl novo;
        Id id;
        Symbol tipo;
        Param_decl_list paramlist = null;
        Func_body corpo;
        Func_aux func_aux;
        
        while(lexer.token == Symbol.FUNCTION){
            
            if(main){
                error.signal("A funcao main deve ser a ultima funcao descrita!");
            }
            
            lexer.nextToken();

            tipo = any_type();
            id = id();
            
            symtable.putInLocal("funcao atual", tipo);
            
            //fazemos verificacao do nome na tabela hash.
            if(symtable.get(id.getId()) != null){
                error.signal("Funcao ja declarada! Linha " + lexer.getLineNumber());
            }
                     
            
            //se for main, verifica se nao tem parametro
            if(id.getId().compareTo("main") == 0){
                if(lexer.token != Symbol.LPAR){
                    error.signal("Espera-se '('! Linha " + lexer.getLineNumber());
                }
                lexer.nextToken();

                if(lexer.token != Symbol.RPAR){
                    error.signal("Funcao 'main' nao suporta parametros! Espera-se ')'. Linha " + lexer.getLineNumber());
                }
                lexer.nextToken();
                main = true;
            }
            //nao é main
            else {    
                if(lexer.token != Symbol.LPAR){
                    error.signal("Os parametros da funcao devem estar entre parenteses! Linha " + lexer.getLineNumber());
                }
                lexer.nextToken();

                if(lexer.token == Symbol.INT || lexer.token == Symbol.FLOAT){
                    paramlist = param_decl_list();
                }

                if(lexer.token != Symbol.RPAR){
                    error.signal("Os parametros da funcao devem estar entre parenteses! Linha " + lexer.getLineNumber());
                }
                lexer.nextToken();
            }
            
            
            //colocando funcaux na hash global
            func_aux = new Func_aux(tipo, paramlist);
            symtable.putInGlobal(id.getId(), func_aux);
            
            
            //corpo
            if(lexer.token != Symbol.BEGIN){
                error.signal("A funcao deve comecar com BEGIN! Linha " + lexer.getLineNumber());
            }
            lexer.nextToken();

            corpo = func_body();

            if(lexer.token != Symbol.END){
                error.signal("A funcao deve terminar com END! Linha " + lexer.getLineNumber());
            }
            lexer.nextToken(); 
            
            
            //limpar tabela de variaveis locais
            symtable.removeLocalIdent();
            
            
            //montar objeto final e colocar na lista
            novo = new Func_decl(tipo, id.getId(), paramlist, corpo);
            
            funcoes.add(novo);
        }
    }
    
    //func_body -> decl stmt_list
    public Func_body func_body(){
        Decl decl;
        Stmt_list stmt_list;
        
        decl = decl(false);
        stmt_list = stmt_list();
        
        return new Func_body(decl, stmt_list);
    }
    

/******************** STATEMENT LIST ********************/      

    //stmt_list -> stmt stmt_tail | empty
    public Stmt_list stmt_list(){
        ArrayList<Stmt> stmtlist = new ArrayList();
        
        if(lexer.token == Symbol.IDENT || lexer.token == Symbol.READ || lexer.token == Symbol.WRITE || lexer.token == Symbol.RETURN || lexer.token == Symbol.IF || lexer.token == Symbol.FOR){
            stmt(stmtlist);
            
            stmt_tail(stmtlist);
        }
        
        return new Stmt_list(stmtlist);
    }
    
    //stmt_tail -> stmt stmt_tail | empty
    public void stmt_tail(ArrayList<Stmt> stmtlist){
        while(lexer.token == Symbol.IDENT || lexer.token == Symbol.READ || lexer.token == Symbol.WRITE || lexer.token == Symbol.RETURN || lexer.token == Symbol.IF || lexer.token == Symbol.FOR){
            stmt(stmtlist);
        }
    }
    
    //stmt -> assign_stmt | read_stmt | write_stmt | return_stmt | if_stmt | for_stmt
    public void stmt(ArrayList<Stmt> stmtlist){
        Symbol symaux;
        
        if(lexer.token == Symbol.IDENT){
            symaux = lexer.checkNextToken();
            
            if(symaux == Symbol.LPAR){
                stmtlist.add(call_stmt());                     
            }
            else{
                stmtlist.add(assign_stmt());
            }            
        }else if (lexer.token == Symbol.READ){
            stmtlist.add(read_stmt());
            
        }else if(lexer.token == Symbol.WRITE){
            stmtlist.add(write_stmt());
            
        }else if(lexer.token == Symbol.RETURN){
            stmtlist.add(return_stmt());
            
        }else if(lexer.token == Symbol.IF){
            stmtlist.add(if_stmt());
            
        }else if(lexer.token == Symbol.FOR){
            stmtlist.add(for_stmt());
        }
    }
    
    public Call_expr call_stmt(){
        Call_expr call_expr = call_expr();
        
        if(lexer.token != Symbol.SEMICOLON){
            error.signal("Um ponto e virgula era esperado antes da linha " + lexer.getLineNumber());
        }
        lexer.nextToken();
        
        return call_expr;
    }
/******************** BASIC STATEMENTS ********************/ 
    
    //assign_stmt -> assign_expr ;
    public Assign_expr assign_stmt(){
        Assign_expr assexpr;
        
        assexpr = assign_expr();
        
        if(lexer.token != Symbol.SEMICOLON){
            error.signal("Um ponto e virgula era esperado antes da linha " + lexer.getLineNumber());
        }
        lexer.nextToken();
        
        return assexpr;
    }

    //assign_expr -> id := expr
    public Assign_expr assign_expr(){       
        Expr expr = null;
        Id id = null;
        Symbol tipo = null;
        
        id = id();  
        
        if(lexer.token != Symbol.ASSIGN){
            error.signal("Esperado uma simbolo de designacao na linha " + lexer.getLineNumber());
        }
        lexer.nextToken();
        
        expr = expr();

        tipo = (Symbol) symtable.getInLocal(id.getId());
        if(tipo == null){
            tipo = (Symbol) symtable.getInLocal(id.getId());
        }
        
        if(tipo == null){
            error.signal("Variavel "+ id.getId() +" nao declarada!");
        }
        
        if(tipo != expr.getTipo(symtable)){
            error.signal("Os tipos das variaveis de atribuicao sao incompativeis! Tentando passar "+ expr.getTipo(symtable) +" para "+ tipo +".");
        }
        
        return new Assign_expr(id, expr);
    }
    
    //read_stmt -> READ ( id_list );
    public Read_stmt read_stmt(){
        ArrayList<Id> idlist;
        Symbol sym = null;
        
        if(lexer.token != Symbol.READ){
            error.signal("Esperado uma declaracao de READ na linha " + lexer.getLineNumber());
        }
        lexer.nextToken();
        
        if(lexer.token != Symbol.LPAR){
            error.signal("Os parametros devem estar entre parenteses! Linha " + lexer.getLineNumber());
        }
        lexer.nextToken();

        idlist = id_list();
        
        
        //verificacao hash:
        for(Id id: idlist){
            sym = (Symbol) symtable.getInGlobal(id.getId());
            
            if(sym == null){
                sym = (Symbol) symtable.getInLocal(id.getId());
            }
            
            if(sym == null){
                error.signal("Variavel "+ id.getId() +" nao declarada!");
            }
            else if(sym == Symbol.STRING){
                error.signal("Variavel de tipo string nao pode ter seu valor alterado!");
            }
        }
        
        
        
        if(lexer.token != Symbol.RPAR){
            error.signal("Os parametros devem estar entre parenteses! Linha " + lexer.getLineNumber());
        }
        lexer.nextToken();

        if(lexer.token != Symbol.SEMICOLON){
            error.signal("Esperado ponto e virgula na linha " + lexer.getLineNumber() + " ou anterior a ela.");
        }
        lexer.nextToken();
        
        return new Read_stmt(idlist, symtable);
    }
    
    //write_stmt -> WRITE ( id_list );
    public Write_stmt write_stmt(){
        ArrayList<Id> idlist;
        Symbol sym = null;
        
        if(lexer.token != Symbol.WRITE){
            error.signal("Esperado uma declaracao de READ na linha " + lexer.getLineNumber());
        }
        lexer.nextToken();
        
        if(lexer.token != Symbol.LPAR){
            error.signal("Os parametros devem estar entre parenteses! Linha " + lexer.getLineNumber());
        }
        lexer.nextToken();

        idlist = id_list();
        
        //analise semantica
        for(Id id: idlist){
            sym = (Symbol) symtable.getInGlobal(id.getId());
            
            if(sym == null){
                sym = (Symbol) symtable.getInLocal(id.getId());
                
                if(sym == null){
                    error.signal("Variavel "+ id.getId() +" nao declarada!");
                }
            }
        }
        
        if(lexer.token != Symbol.RPAR){
            error.signal("Os parametros devem estar entre parenteses! Linha " + lexer.getLineNumber());
        }
        lexer.nextToken();

        if(lexer.token != Symbol.SEMICOLON){
            error.signal("Esperado ponto e virgula na linha " + lexer.getLineNumber() + " ou anterior a ela.");
        }
        lexer.nextToken();        
        
        return new Write_stmt(idlist, symtable);
    }
    
    //return_stmt -> RETURN expr;
    public Return_stmt return_stmt(){
        Expr expr;
        Symbol tipofuncao;
        
        if(lexer.token != Symbol.RETURN){
            error.signal("Esperado declaracao RETURN na linha " + lexer.getLineNumber());
        }
        lexer.nextToken();
        
        expr = expr();
        
        tipofuncao = (Symbol) symtable.getInLocal("funcao atual");
        if(tipofuncao == Symbol.VOID){
            error.signal("Funcoes do tipo VOID nao devem possuir retorno!");
        }
        else if(tipofuncao != expr.getTipo(symtable)){
            error.signal("Tipo de retorno incompativel! Esperado "+ symtable.getInLocal("funcao atual") +".");
        }
        
        if(lexer.token != Symbol.SEMICOLON){
            error.signal("Esperado ponto e virgula na linha " + lexer.getLineNumber() + " ou anterior a ela.");
        }
        lexer.nextToken();
        
        return new Return_stmt(expr);
    }
    
    
/******************** EXPRESSIONS ********************/ 

    //expr -> factor expr_tail
    public Expr expr(){
        Factor fact;
        ArrayList<Expr_tail> tail;
        Expr expresq = null, exprdir = null, fexpr;
        Expr_conteudo cont = null;
        
        fact = factor();        
        tail = expr_tail();
        
        fexpr = fact_expr(fact);
        
        if(!tail.isEmpty()){    //tem exprtail
            expresq = fexpr;
            
            cont = new Expr_conteudo(tail.get(0).getAddop());
            exprdir = add_expr(tail);
            return new Expr(cont, expresq, exprdir);
        }
        else{
            return fexpr;
        }
    }
    
    public Expr fact_expr(Factor fact){
        int tipoprim;
        boolean factail = true;
        Expr expresq = null, exprdir = null;
        Expr_conteudo cont = null;
        Symbol sym = null;
        
        if(fact.getTail().isEmpty()){
            factail = false;
        }
        
        if(fact.getCall() != null){ //se for call_expr
            cont = new Expr_conteudo(fact.getCall());
            if(factail){
                expresq = new Expr(cont);
            }
        }
        else{   //se for primary
            tipoprim = fact.getPrimary().getTipo();

            switch (tipoprim){
                case 1: //expr  //possivel problema!!!!??
                    expresq = fact.getPrimary().getExpr();
                    expresq.setPar(true);

                    if(!factail){
                        return expresq;
                    }
                    break;
                case 2: //id
                    cont = new Expr_conteudo(fact.getPrimary().getId());
                    expresq = new Expr(cont);
                    
                    //analise semantica: verificar se nao eh string
                    sym = (Symbol) symtable.getInLocal(cont.getId());
                    if(sym == null){
                        sym = (Symbol) symtable.getInGlobal(cont.getId());
                        if(sym == null){
                            error.signal("Variavel "+ cont.getId() +" nao declarada!");
                        }
                    }       
                    
                    if(sym == Symbol.STRING){
                        error.signal("Variavel de tipo string nao pode ser usado em operacoes matematicas ou atribuicoes!");
                    }
                    
                    break;
                case 3: //int
                    cont = new Expr_conteudo(fact.getPrimary().getInt());
                    expresq = new Expr(cont);
                    break;
                case 4: //float
                    cont = new Expr_conteudo(fact.getPrimary().getFloat());
                    expresq = new Expr(cont);
                    break;
            }
        }
        
        if(factail){ //eh mulop e outra expr
            cont = new Expr_conteudo(fact.getTail().get(0).getMulop());
            exprdir = mult_expr(fact.getTail());
            return new Expr(cont, expresq, exprdir);
        }
        
        return new Expr(cont, null, null);
    }
    
    //funcao para montar as Expr do factor tail:
    public Expr mult_expr(ArrayList<Factor_tail> tail){
        int tipoprim;
        Factor_tail ftail;
        Expr expresq = null, exprdir = null;
        Expr_conteudo cont = null;
        Symbol sym = null;
        
        ftail = tail.get(0);
        
        if(ftail.getCall() != null){    //eh call_expr
            cont = new Expr_conteudo(ftail.getCall());
            if(tail.size() == 1){
                expresq = new Expr(cont);
            }
        }
        else{   //eh primary
            tipoprim = ftail.getPrimary().getTipo();

            switch (tipoprim){
                case 1: //expr  //problema??!!
                    expresq = ftail.getPrimary().getExpr();
                    expresq.setPar(true);
                    
                    if(tail.size() == 1){
                        return expresq;
                    }
                    break;
                case 2: //id
                    cont = new Expr_conteudo(ftail.getPrimary().getId());
                    expresq = new Expr(cont);
                    
                    //analise semantica: verificar se nao eh string
                    sym = (Symbol) symtable.getInLocal(cont.getId());
                    if(sym == null){
                        sym = (Symbol) symtable.getInGlobal(cont.getId());
                        if(sym == null){
                            error.signal("Variavel "+ cont.getId() +" nao declarada!");
                        }
                    }       
                    
                    if(sym == Symbol.STRING){
                        error.signal("Variavel de tipo string nao pode ser usado em operacoes matematicas ou atribuicoes!");
                    }
                    
                    break;
                case 3: //int
                    cont = new Expr_conteudo(ftail.getPrimary().getInt());
                    expresq = new Expr(cont);
                    break;
                case 4: //float
                    cont = new Expr_conteudo(ftail.getPrimary().getFloat());
                    expresq = new Expr(cont);
                    break;
            }
        }
        
        if(tail.size() == 1){ //eh o ultimo da lista
            return new Expr(cont, null, null);
        }
        else{
            cont = new Expr_conteudo(tail.get(1).getMulop());
            tail.remove(0);
            exprdir = mult_expr(tail);
        }
        
        return new Expr(cont, expresq, exprdir); 
    }
    
    //funcao para montar as Expr do expr_tail:
    public Expr add_expr(ArrayList<Expr_tail> tail){
        Expr_conteudo cont;
        Expr exprdir, expresq, fexpr;
        
        fexpr = fact_expr(tail.get(0).getFactor());
        
        if(tail.size() != 1){ //nao eh o ultimo
            expresq = fexpr;
            
            cont = new Expr_conteudo(tail.get(1).getAddop());
            tail.remove(0);
            exprdir = add_expr(tail);
            
            return new Expr(cont, expresq, exprdir);
        }
        else{
            return fexpr;
        }
    }
    
    //expr_tail -> addop factor expr_tail | empty
    public ArrayList<Expr_tail> expr_tail(){
        ArrayList<Expr_tail> tail = new ArrayList();
        Expr_tail novo;
        Symbol addop;
        Factor factor;
        
        while(lexer.token == Symbol.PLUS || lexer.token == Symbol.MINUS){            
            addop = addop();
            factor = factor();
            
            novo = new Expr_tail(addop, factor);
            
            tail.add(novo);
        }
        
        return tail;
    }
    
    //factor -> postfix_expr factor_tail
    public Factor factor(){
        Primary prim = null;
        Call_expr callexpr = null;
        ArrayList<Factor_tail> tail = null;
        
        Symbol auxsym;
        
        if(lexer.token == Symbol.IDENT){
        
            auxsym = lexer.checkNextToken();
        
            if(auxsym == Symbol.LPAR){
                callexpr = call_expr();
            }
            else{
                prim = primary();
            }
        }
        else{
            prim = primary();
        }
        
        tail = factor_tail();
        
        return new Factor(prim, callexpr, tail);
    }
    
    //factor_tail -> mulop postfix_expr factor_tail | empty
    public ArrayList<Factor_tail> factor_tail(){
        Factor_tail novo = null;
        ArrayList<Factor_tail> tail = new ArrayList();
        Symbol mulop, auxsym;
        Primary prim = null;
        Call_expr call = null;
        
        
        while(lexer.token == Symbol.MULT || lexer.token == Symbol.DIV){
            mulop = mulop();
            
            if(lexer.token == Symbol.IDENT){

                auxsym = lexer.checkNextToken();

                if(auxsym == Symbol.LPAR){
                    call = call_expr();
                }
                else{
                    prim = primary();
                }
            }
            else{
                prim = primary();
            }
            
            novo = new Factor_tail(mulop, prim, call);
            tail.add(novo);
        }
        
        return tail;
    }

    //call_expr -> id ( {expr_list} )
    public Call_expr call_expr(){
        Id id;
        ArrayList<Expr> exprlist = null;
        Func_aux funcaux;
        Param_decl_list params;
        
        id = id();
        
        if(lexer.token != Symbol.LPAR){
            error.signal("As expressoes devem estar entre parenteses! Linha " + lexer.getLineNumber());
        }
        lexer.nextToken();

        
        if(lexer.token != Symbol.RPAR){
            exprlist = expr_list();
        }
    
        if(lexer.token != Symbol.RPAR){
            error.signal("As expressoes devem estar entre parenteses! Linha " + lexer.getLineNumber());
        }
        lexer.nextToken();
        
        funcaux = (Func_aux) symtable.getInGlobal(id.getId());
        if(funcaux == null){
            error.signal("Funcao chamada nao declarada! Linha " + lexer.getLineNumber());
        }
        
        params = funcaux.getParam();
        
        //verificar se o tamanho dos parametros eh o mesmo
        if(exprlist.size() != params.getParlist().size()){
            error.signal("Tamanho dos parametros diferentes. Linha "+lexer.getCurrentLine());
        }
        
        //verificar se o tipo dos parametros eh o mesmo
        for(int i = 0; i < params.getParlist().size(); i++){
            if(params.getParlist().get(i).getTipo() != exprlist.get(i).getTipo(symtable)){
                error.signal("Tipo dos parametros diferentes. Linha "+lexer.getCurrentLine());
            }
        }
        
        
        
        return new Call_expr(id, exprlist);
    }

    //expr_list -> expr expr_list_tail
    public ArrayList<Expr> expr_list(){
        ArrayList<Expr> list = new ArrayList();
        Expr novo=null;
        
        novo = expr();
        list.add(novo);
        
        list = expr_list_tail(list);
        
        return list;
    }
    
    //expr_list_tail -> , expr expr_list_tail | empty
    public ArrayList<Expr> expr_list_tail(ArrayList<Expr> list){
        Expr novo = null;
        
        while(lexer.token == Symbol.COMMA){
            lexer.nextToken();
             
            novo = expr();
                
            list.add(novo);
        }
        
        return list;
    }
    
    //primary -> (expr) | id | INTLITERAL | FLOATLITERAL
    public Primary primary(){
        Expr expr = null;
        Id id = null;
        int numint;
        float numfloat;
        
        if(lexer.token == Symbol.LPAR){
            lexer.nextToken();
            
            expr = expr();
            
            if(lexer.token != Symbol.RPAR){
                error.signal("Esperado um fecha parenteses na linha " + lexer.getLineNumber());
            }
            lexer.nextToken();
            
            return new Primary(expr);
        }
        else if(lexer.token == Symbol.IDENT){
            id = id();
            
            return new Primary(id);
        }
        else if(lexer.token == Symbol.INTLITERAL){
            numint = lexer.getIntNumber();
            lexer.nextToken();
            
            return new Primary(numint);
        }
        else if(lexer.token == Symbol.FLOATLITERAL){
            numfloat = lexer.getFloatNumber();
            lexer.nextToken();
            
            return new Primary(numfloat);
        }
        
        return null;
    }
    
    //addop -> +|-
    public Symbol addop(){
        Symbol addop;
        if(lexer.token != Symbol.PLUS && lexer.token != Symbol.MINUS){
            error.signal("Um sinal de + ou - era esperado na linha " + lexer.getLineNumber());
        }
        addop = lexer.token;
                
        lexer.nextToken();
        
        return addop;
    }
    
    //mulop -> * | /
    public Symbol mulop(){
        Symbol mulop;
        
        if(lexer.token != Symbol.MULT && lexer.token != Symbol.DIV){
            error.signal("Um sinal de + ou - era esperado na linha " + lexer.getLineNumber());
        }
        mulop = lexer.token;
        
        lexer.nextToken();
        
        return mulop;
    }
    
    
/******************** COMPLEX STATEMENTS AND CONDITIONS ********************/ 

    //if_stmt -> IF ( cond ) THEN stmt_list else_part ENDIF
    public If_stmt if_stmt(){
        Cond cond = null;
        Stmt_list stmt_list = null;
        Else_part else_part = null;
        
        if(lexer.token != Symbol.IF){
            error.signal("Uma declaracao IF era esperada na linha " + lexer.getLineNumber());
        }
        lexer.nextToken();
    
        if(lexer.token != Symbol.LPAR){
            error.signal("As condicoes devem estar entre parenteses! Linha " + lexer.getLineNumber());
        }
        lexer.nextToken();

        cond = cond();
        
        if(lexer.token != Symbol.RPAR){
            error.signal("As expressoes devem estar entre parenteses! Linha " + lexer.getLineNumber());
        }
        lexer.nextToken();
        
        if(lexer.token != Symbol.THEN){
            error.signal("Uma declaracao ELSE era esperada na linha " + lexer.getLineNumber());
        }
        lexer.nextToken();
        
        stmt_list = stmt_list();
        
        if(lexer.token == Symbol.ELSE){
            else_part = else_part();
        }       
        
        
        if(lexer.token != Symbol.ENDIF){
            error.signal("Uma declaracao ENDIF era esperada na linha " + lexer.getLineNumber());
        }
        lexer.nextToken();
        
        return new If_stmt(cond, stmt_list, else_part);
    }
    
    //else_part -> ELSE stmt_list | empty
    public Else_part else_part(){
        Stmt_list stmtlist = null;
        
        if(lexer.token == Symbol.ELSE){
            lexer.nextToken();
            
            stmtlist = stmt_list();
        }
        
        return new Else_part(stmtlist);
    }
    
    //cond -> expr compop expr
    public Cond cond(){
        Expr expr1 = null;
        Expr expr2 = null;
        Symbol compop;
        
        expr1 = expr();
        
        compop = compop();
        
        expr2 = expr();
        
        if(expr1.getTipo(symtable) == Symbol.STRING || expr2.getTipo(symtable) == Symbol.STRING){
            error.signal("Comparacoes nao podem ser feitas com strings!");
        }
        
        
        
        return new Cond(expr1, compop, expr2);
    }
    
    //compop -> < | > | =
    public Symbol compop(){
        Symbol compop = null;
        if(lexer.token == Symbol.LT || lexer.token == Symbol.GT || lexer.token == Symbol.EQUAL){
            compop = lexer.token;
            lexer.nextToken();
        }
        else{
            error.signal("Uma operacao de comparacao era esperada na linha " + lexer.getLineNumber());
        }
        
        return compop;
    }
    
    //for_stmt -> FOR ({assig_expr}; {cond}; {assign_expr}) stmt_list ENDFOR
    public For_stmt for_stmt(){
        Assign_expr assign1 = null, assign2 = null;
        Cond cond = null;
        Stmt_list stmtlist = null;
        
        if(lexer.token != Symbol.FOR){
            error.signal("Uma declaracao FOR era esperada na linha " + lexer.getLineNumber());
        }
        lexer.nextToken();
        
    
        //abre parenteses
        if(lexer.token != Symbol.LPAR){
            error.signal("Os parametros devem estar entre parenteses! Linha " + lexer.getLineNumber());
        }
        lexer.nextToken();
        
        
        //designacao
        if(lexer.token != Symbol.SEMICOLON){
            assign1 = assign_expr();
        }
                
        if(lexer.token != Symbol.SEMICOLON){
            error.signal("Esperado ponto e virgula na linha " + lexer.getLineNumber() + " ou anterior a ela.");
        }
        lexer.nextToken();
        
        
        //condicao de parada
        if(lexer.token != Symbol.SEMICOLON){
            cond = cond();
        }
        
        if(lexer.token != Symbol.SEMICOLON){
            error.signal("Esperado ponto e virgula na linha " + lexer.getLineNumber() + " ou anterior a ela.");
        }
        lexer.nextToken();
        
        
        //operacao na itereacao
        if(lexer.token != Symbol.RPAR){
            assign2 = assign_expr();
        }

        
        //fecha ṕarenteses
        if(lexer.token != Symbol.RPAR){
            error.signal("Os parametros devem estar entre parenteses! Linha " + lexer.getLineNumber());
        }
        lexer.nextToken();

        
        stmtlist = stmt_list();
        
        
        if(lexer.token != Symbol.ENDFOR){
            error.signal("Uma declaracao ENDFOR era esperada na linha " + lexer.getLineNumber());
        }
        lexer.nextToken();
        
        return new For_stmt(assign1, cond, assign2, stmtlist);
    }
}