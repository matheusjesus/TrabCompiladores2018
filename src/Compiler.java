import Lexer.*;
import Error.*;
import AST.*;
import java.util.*;

public class Compiler {

    // para geracao de codigo
    public static final boolean GC = false; 

    public Program compile( char []p_input ) {
        error = new CompilerError(null);
        lexer = new Lexer(p_input, error);
        error.setLexer(lexer);
        lexer.nextToken();
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
        Decl decl = decl();
        Func_declarations fdecl = func_declarations();
        
        return new Pgm_body(decl, fdecl);
    }
    
    //decl -> string_decl_list {decl} | var_decl_list {decl} | empty
    public Decl decl(){
        String_decl_list strlist = null;
        Var_decl_list varlist = null;
        
        if(lexer.token == Symbol.STRING){
            strlist = string_decl_list();
        }
        
        if(lexer.token == Symbol.INT || lexer.token == Symbol.FLOAT){
            varlist = var_decl_list();
        }
        
        while(lexer.token == Symbol.STRING || lexer.token == Symbol.INT || lexer.token == Symbol.FLOAT){
            decl();
        }
        
        return new Decl(strlist, varlist);
    }


/******************** GLOBAL STRING DECLARATION ********************/
    
    //string_decl_list -> string_decl {string_decl_tail}
    public String_decl_list string_decl_list(){
        ArrayList<VariableStr> strlist;
        strlist = string_decl();
        
        if(lexer.token == Symbol.STRING){
            strlist = string_decl_tail(strlist);
        }
        
        return new String_decl_list(strlist);
    }
    
    //string_decl -> STRING id := str ; | empty
    public ArrayList<VariableStr> string_decl(){
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
                error.signal("Um ponto e virgula era esperado na linha " + lexer.getLineNumber() + " ou anterior a ela.");
            }
            lexer.nextToken();
        }
        novo = new VariableStr(idnovo.getId(), strnovo);
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
    public ArrayList<VariableStr> string_decl_tail(ArrayList<VariableStr> strlist){
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
                error.signal("Um ponto e virgula era esperado na linha " + lexer.getLineNumber() + " ou anterior a ela.");
            }
            lexer.nextToken();
            
            novo = new VariableStr(idnovo.getId(), strnovo);
            strlist.add(novo);
        }
        
        return strlist;
    }
    

/******************** VARIABLE DECLARATION ********************/    
    
    //var_decl_list -> var_decl {var_decl_tail}
    public Var_decl_list var_decl_list(){
        ArrayList<Var_type> lv;
        Var_decl_list varlist;
        
        lv = var_decl();
        lv = var_decl_tail(lv);
        
        varlist = new Var_decl_list(lv);
        return varlist;
    }
    
    //var_decl -> var_type id_list ; | empty
    public ArrayList<Var_type> var_decl(){
        Symbol tipo;
        ArrayList<Id> idlist;
        ArrayList<Var_type> lv = new ArrayList();
        Var_type v;
        int i;
        
        if(lexer.token == Symbol.FLOAT || lexer.token == Symbol.INT){
            tipo = var_type();

            idlist = id_list();

            if(lexer.token != Symbol.SEMICOLON){
                error.signal("Um ponto e virgula era esperado na linha " + lexer.getLineNumber() + " ou anterior a ela.");
            }
            lexer.nextToken();
            
            for(i=0;i < idlist.size(); i++){
                v = new Var_type(idlist.get(i).getId(), tipo);
//                System.out.println(v.getNome() + "tipo: " + v.getTipo());
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
    public ArrayList<Var_type> var_decl_tail(ArrayList<Var_type> lv){
        ArrayList<Var_type> lvaux;
        int i;
        
        while(lexer.token == Symbol.FLOAT || lexer.token == Symbol.INT){
            lvaux = var_decl();
            
            for(i=0;i<lvaux.size();i++){
                lv.add(lvaux.get(i));
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

            novo = new Param_decl(tipo, id.getId());
            parlist.add(novo);
        }
        
        return parlist;
    }
    
    
/******************** FUNCTION DECLARATIONS ********************/      

    //func_declarations -> func_decl {func_decl_tail}
    public Func_declarations func_declarations(){
        ArrayList<Func_decl> funcoes = null;
        
        funcoes = func_decl();
        
        if(lexer.token == Symbol.FUNCTION){
            funcoes = func_decl_tail(funcoes);
        }
        
        return new Func_declarations(funcoes);
    }
    
    //func_decl -> FUNCTION any_type id ({param_decl_list}) BEGIN func_body END | empty
    public ArrayList<Func_decl> func_decl(){
        ArrayList<Func_decl> funcoes = new ArrayList();
        Func_decl novo;
        Id id;
        Symbol tipo;
        Param_decl_list paramlist = null;
        Func_body corpo;
        
        if(lexer.token == Symbol.FUNCTION){
            lexer.nextToken();

            tipo = any_type();
            id = id();
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

            if(lexer.token != Symbol.BEGIN){
                error.signal("A funcao deve comecar com BEGIN! Linha " + lexer.getLineNumber());
            }
            lexer.nextToken();

            corpo = func_body();

            if(lexer.token != Symbol.END){
                error.signal("A funcao deve terminar com END! Linha " + lexer.getLineNumber());
            }
            lexer.nextToken(); 
            
            novo = new Func_decl(tipo, id.getId(), paramlist, corpo);
            
            funcoes.add(novo);
        }
        
        return funcoes;
    }
    
    //func_decl_tail -> func_decl {func_decl_tail}
    public ArrayList<Func_decl> func_decl_tail(ArrayList<Func_decl> funcoes){
        Func_decl novo;
        Id id;
        Symbol tipo;
        Param_decl_list paramlist = null;
        Func_body corpo;
        
        while(lexer.token == Symbol.FUNCTION){
            lexer.nextToken();

            tipo = any_type();
            id = id();
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

            if(lexer.token != Symbol.BEGIN){
                error.signal("A funcao deve comecar com BEGIN! Linha " + lexer.getLineNumber());
            }
            lexer.nextToken();

            corpo = func_body();

            if(lexer.token != Symbol.END){
                error.signal("A funcao deve terminar com END! Linha " + lexer.getLineNumber());
            }
            lexer.nextToken(); 
            
            novo = new Func_decl(tipo, id.getId(), paramlist, corpo);
            
            funcoes.add(novo);
        }
        
        return funcoes;
    }
    
    //func_body -> decl stmt_list
    public Func_body func_body(){
        Decl decl;
        Stmt_list stmt_list;
        
        decl = decl();
        stmt_list = stmt_list();
        
        return new Func_body(decl, stmt_list);
    }
    

/******************** STATEMENT LIST ********************/      

    //stmt_list -> stmt stmt_tail | empty
    public Stmt_list stmt_list(){
        ArrayList<Stmt> stmtlist = new ArrayList();
        Stmt novo;
        
        if(lexer.token == Symbol.IDENT || lexer.token == Symbol.READ || lexer.token == Symbol.WRITE || lexer.token == Symbol.RETURN || lexer.token == Symbol.IF || lexer.token == Symbol.FOR){
            novo = stmt();
            
            stmtlist.add(novo);
            
            stmtlist = stmt_tail(stmtlist);
        }
        
        return new Stmt_list(stmtlist);
    }
    
    //stmt_tail -> stmt stmt_tail | empty
    public ArrayList<Stmt> stmt_tail(ArrayList<Stmt> stmtlist){
        Stmt novo;
        while(lexer.token == Symbol.IDENT || lexer.token == Symbol.READ || lexer.token == Symbol.WRITE || lexer.token == Symbol.RETURN || lexer.token == Symbol.IF || lexer.token == Symbol.FOR){
            novo = stmt();
            stmtlist.add(novo);
        }
        
        return stmtlist;
    }
    
    //stmt -> assign_stmt | read_stmt | write_stmt | return_stmt | if_stmt | for_stmt
    public Stmt stmt(){
        Symbol symaux;
        Stmt novo = null;
        
        if(lexer.token == Symbol.IDENT){
            symaux = lexer.checkNextToken();
            
            if(symaux == Symbol.LPAR){
                novo = call_expr();
                if(lexer.token != Symbol.SEMICOLON){
                    error.signal("Um ponto e virgula era esperado na linha " + lexer.getLineNumber() + " ou anterior a ela.");
                }
                lexer.nextToken();
            }
            else{
                novo = assign_stmt();
            }            
        }else if (lexer.token == Symbol.READ){
            novo = read_stmt();
            
        }else if(lexer.token == Symbol.WRITE){
            novo = write_stmt();
            
        }else if(lexer.token == Symbol.RETURN){
            novo = return_stmt();
            
        }else if(lexer.token == Symbol.IF){
            novo = if_stmt();
            
        }else if(lexer.token == Symbol.FOR){
            novo = for_stmt();
        }
        
        return novo;
    }
    
    
/******************** BASIC STATEMENTS ********************/ 
    
    //assign_stmt -> assign_expr ;
    public Assign_expr assign_stmt(){
        Assign_expr assexpr;
        
        assexpr = assign_expr();
        
        if(lexer.token != Symbol.SEMICOLON){
            error.signal("Esperado um ponto e virgula na linha " + lexer.getLineNumber() + " ou anterior a ela.");
        }
        lexer.nextToken();
        
        return assexpr;
    }

    //assign_expr -> id := expr
    public Assign_expr assign_expr(){       
        Expr expr = null;
        Id id = null;
        
        id = id();
        
        if(lexer.token != Symbol.ASSIGN){
            error.signal("Esperado uma simbolo de designacao na linha " + lexer.getLineNumber());
        }
        lexer.nextToken();
        
        expr = expr();
        
        return new Assign_expr(id, expr);
    }
    
    //read_stmt -> READ ( id_list );
    public Read_stmt read_stmt(){
        ArrayList<Id> idlist;
        if(lexer.token != Symbol.READ){
            error.signal("Esperado uma declaracao de READ na linha " + lexer.getLineNumber());
        }
        lexer.nextToken();
        
        if(lexer.token != Symbol.LPAR){
            error.signal("Os parametros devem estar entre parenteses! Linha " + lexer.getLineNumber());
        }
        lexer.nextToken();

        idlist = id_list();
        
        if(lexer.token != Symbol.RPAR){
            error.signal("Os parametros devem estar entre parenteses! Linha " + lexer.getLineNumber());
        }
        lexer.nextToken();

        if(lexer.token != Symbol.SEMICOLON){
            error.signal("Esperado ponto e virgula na linha " + lexer.getLineNumber() + " ou anterior a ela.");
        }
        lexer.nextToken();
        
        return new Read_stmt(idlist);
    }
    
    //write_stmt -> WRITE ( id_list );
    public Write_stmt write_stmt(){
        ArrayList<Id> idlist;
        
        if(lexer.token != Symbol.WRITE){
            error.signal("Esperado uma declaracao de READ na linha " + lexer.getLineNumber());
        }
        lexer.nextToken();
        
        if(lexer.token != Symbol.LPAR){
            error.signal("Os parametros devem estar entre parenteses! Linha " + lexer.getLineNumber());
        }
        lexer.nextToken();

        idlist = id_list();
        
        if(lexer.token != Symbol.RPAR){
            error.signal("Os parametros devem estar entre parenteses! Linha " + lexer.getLineNumber());
        }
        lexer.nextToken();

        if(lexer.token != Symbol.SEMICOLON){
            error.signal("Esperado ponto e virgula na linha " + lexer.getLineNumber() + " ou anterior a ela.");
        }
        lexer.nextToken();        
        
        return new Write_stmt(idlist);
    }
    
    //return_stmt -> RETURN expr;
    public Return_stmt return_stmt(){
        Expr expr;
        
        if(lexer.token != Symbol.RETURN){
            error.signal("Esperado declaracao RETURN na linha " + lexer.getLineNumber());
        }
        lexer.nextToken();
        
        expr = expr();
        
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
        
        
        fact = factor();
        
        tail = expr_tail();
        
        return new Expr(fact, tail); 
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
    
/*    //postfix_expr -> primary | call_expr
    public void postfix_expr(){
        Symbol auxsym;
        
        if(lexer.token == Symbol.IDENT){
        
            auxsym = lexer.checkNextToken();
        
            if(auxsym == Symbol.LPAR){
                call_expr();
            }
            else{
                primary();
            }
        }
        else{
            primary();
        }
    }
*/
    //call_expr -> id ( {expr_list} )
    public Call_expr call_expr(){
        Id id;
        ArrayList<Expr> exprlist = null;
        
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
        Expr expr;
        Id id;
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
        Expr expr1, expr2;
        Symbol compop;
        
        expr1 = expr();
        
        compop = compop();
        
        expr2 = expr();
        
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
            error.signal("Uma declaracao ENDIF era esperada na linha " + lexer.getLineNumber());
        }
        lexer.nextToken();
        
        return new For_stmt(assign1, cond, assign2, stmtlist);
    }
    
	private Lexer lexer;
    private CompilerError error;
}