import Lexer.*;
import Error.*;
import AST.*;
import java.util.*;

public class Compiler {

	// para geracao de codigo
	public static final boolean GC = false; 

    public void compile( char []p_input ) {
        error = new CompilerError(null);
        lexer = new Lexer(p_input, error);
        error.setLexer(lexer);
        lexer.nextToken();
        Program p = program();
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
        if(lexer.token == Symbol.STRING){
            string_decl_list();
        }
        
        if(lexer.token == Symbol.INT || lexer.token == Symbol.FLOAT){
            var_decl_list();
        }
        
        while(lexer.token == Symbol.STRING || lexer.token == Symbol.INT || lexer.token == Symbol.FLOAT){
            decl();
        }
        
        return new Decl();
    }


/******************** GLOBAL STRING DECLARATION ********************/
    
    //string_decl_list -> string_decl {string_decl_tail}
    public void string_decl_list(){
        string_decl();
        
        if(lexer.token == Symbol.STRING){
            string_decl_tail();
        }
    }
    
    //string_decl -> STRING id := str ; | empty
    public void string_decl(){
        if(lexer.token == Symbol.STRING){
            lexer.nextToken();

            id();

            if(lexer.token != Symbol.ASSIGN){
                error.signal("Um sinal de atribuicao era esperado na linha " + lexer.getLineNumber());
            }
            lexer.nextToken();
            
            str();

            if(lexer.token != Symbol.SEMICOLON){
                error.signal("Um ponto e virgula era esperado na linha " + lexer.getLineNumber() + " ou anterior a ela.");
            }
            lexer.nextToken();
        }
    }
    
    //str -> STRINGLITERAL
    public void str(){
        if(lexer.token != Symbol.STRINGLITERAL){
            error.signal("Esperado um STRINGLITERAL na linha " + lexer.getLineNumber());
        }
        
        lexer.nextToken();
    }
    
    //string_decl_tail -> string_decl {string_decl_tail}
    public void string_decl_tail(){
        string_decl();
        
        if(lexer.token == Symbol.STRING){
            string_decl_tail();
        }
    }
    

/******************** VARIABLE DECLARATION ********************/    
    
    //var_decl_list -> var_decl {var_decl_tail}
    public ArrayList<Variable> var_decl_list(){
        ArrayList<Variable> lv;
        
        lv = var_decl();
        lv = var_decl_tail(lv);
        
        return lv;
    }
    
    //var_decl -> var_type id_list ; | empty
    public ArrayList<Variable> var_decl(){
        Symbol tipo;
        ArrayList<Id> idlist;
        ArrayList<Variable> lv = new ArrayList();
        Variable v;
        int i;
        
        if(lexer.token == Symbol.FLOAT || lexer.token == Symbol.INT){
            tipo = var_type();

            idlist = id_list();

            if(lexer.token != Symbol.SEMICOLON){
                error.signal("Um ponto e virgula era esperado na linha " + lexer.getLineNumber() + " ou anterior a ela.");
            }
            lexer.nextToken();
            
            for(i=0;i < idlist.size(); i++){
                v = new Variable(idlist.get(i).getId(), tipo);
                System.out.println(v.getNome() + "tipo: " + v.getTipo());
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
    public void any_type(){
        if(lexer.token != Symbol.VOID){
            var_type();
        }
        else if(lexer.token == Symbol.VOID){
            lexer.nextToken();
        }
        else{
           error.signal("Uma declaracao de VOID era esperada na linha " + lexer.getLineNumber());
        }
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
    public ArrayList<Variable> var_decl_tail(ArrayList<Variable> lv){
        ArrayList<Variable> lvaux;
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
    public void param_decl_list(){
        param_decl();
        param_decl_tail();
    }
    
    //param_decl -> var_type id
    public void param_decl(){
        var_type();
        id();
    }
    
    //param_decl_tail -> , param_decl param_decl_tail | empty
    public void param_decl_tail(){
        int comma = 0;
               
        if(lexer.token == Symbol.COMMA){
            comma = 1;
            lexer.nextToken();
        }
        
        if(lexer.token == Symbol.FLOAT || lexer.token == Symbol.INT){
            if(comma == 0){
                error.signal("Uma virgula era esperada na linha " + lexer.getLineNumber());
            }
            
            param_decl();
            param_decl_tail();
        }
    }
    
    
/******************** FUNCTION DECLARATIONS ********************/      

    //func_declarations -> func_decl {func_decl_tail}
    public Func_declarations func_declarations(){
        func_decl();
        
        if(lexer.token == Symbol.FUNCTION){
            func_decl_tail();
        }
        
        return new Func_declarations();
    }
    
    //func_decl -> FUNCTION any_type id ({param_decl_list}) BEGIN func_body END | empty
    public void func_decl(){
        if(lexer.token == Symbol.FUNCTION){
            lexer.nextToken();

            any_type();
            id();

            if(lexer.token != Symbol.LPAR){
                error.signal("Os parametros da funcao devem estar entre parenteses! Linha " + lexer.getLineNumber());
            }
            lexer.nextToken();

            if(lexer.token == Symbol.INT || lexer.token == Symbol.FLOAT){
                param_decl_list();
            }

            if(lexer.token != Symbol.RPAR){
                error.signal("Os parametros da funcao devem estar entre parenteses! Linha " + lexer.getLineNumber());
            }
            lexer.nextToken();

            if(lexer.token != Symbol.BEGIN){
                error.signal("A funcao deve comecar com BEGIN! Linha " + lexer.getLineNumber());
            }
            lexer.nextToken();

            func_body();

            if(lexer.token != Symbol.END){
                error.signal("A funcao deve terminar com END! Linha " + lexer.getLineNumber());
            }
            lexer.nextToken(); 
        }
    }
    
    //func_decl_tail -> func_decl {func_decl_tail}
    public void func_decl_tail(){
        func_decl();
        
        if(lexer.token == Symbol.FUNCTION){
            func_decl_tail();
        }
    }
    
    //func_body -> decl stmt_list
    public void func_body(){
        decl();
        stmt_list();
    }
    

/******************** STATEMENT LIST ********************/      

    //stmt_list -> stmt stmt_tail | empty
    public void stmt_list(){
        if(lexer.token == Symbol.IDENT || lexer.token == Symbol.READ || lexer.token == Symbol.WRITE || lexer.token == Symbol.RETURN || lexer.token == Symbol.IF || lexer.token == Symbol.FOR){
            stmt();
        
            stmt_tail();
        }
    }
    
    //stmt_tail -> stmt stmt_tail | empty
    public void stmt_tail(){
        if(lexer.token == Symbol.IDENT || lexer.token == Symbol.READ || lexer.token == Symbol.WRITE || lexer.token == Symbol.RETURN || lexer.token == Symbol.IF || lexer.token == Symbol.FOR){
            stmt();
        
            stmt_tail();
        }
    }
    
    //stmt -> assign_stmt | read_stmt | write_stmt | return_stmt | if_stmt | for_stmt
    public void stmt(){
        Symbol symaux;
        
        if(lexer.token == Symbol.IDENT){
            symaux = lexer.checkNextToken();
            
            if(symaux == Symbol.LPAR){
                call_expr();
                if(lexer.token != Symbol.SEMICOLON){
                    error.signal("Um ponto e virgula era esperado na linha " + lexer.getLineNumber() + " ou anterior a ela.");
                }
                lexer.nextToken();
            }
            else{
                assign_stmt();
            }            
        }else if (lexer.token == Symbol.READ){
            read_stmt();
            
        }else if(lexer.token == Symbol.WRITE){
            write_stmt();
            
        }else if(lexer.token == Symbol.RETURN){
            return_stmt();
            
        }else if(lexer.token == Symbol.IF){
            if_stmt();
            
        }else if(lexer.token == Symbol.FOR){
            for_stmt();
        }
    }
    
    
/******************** BASIC STATEMENTS ********************/ 
    
    //assign_stmt -> assign_expr ;
    public void assign_stmt(){
        assign_expr();
        
        if(lexer.token != Symbol.SEMICOLON){
            error.signal("Esperado um ponto e virgula na linha " + lexer.getLineNumber() + " ou anterior a ela.");
        }
        lexer.nextToken();
    }
    
    //assign_expr -> id := expr
    public void assign_expr(){
        id();
        
        if(lexer.token != Symbol.ASSIGN){
            error.signal("Esperado uma simbolo de designacao na linha " + lexer.getLineNumber());
        }
        lexer.nextToken();
        
        expr();
    }
    
    //read_stmt -> READ ( id_list );
    public void read_stmt(){
        if(lexer.token != Symbol.READ){
            error.signal("Esperado uma declaracao de READ na linha " + lexer.getLineNumber());
        }
        lexer.nextToken();
        
        if(lexer.token != Symbol.LPAR){
            error.signal("Os parametros devem estar entre parenteses! Linha " + lexer.getLineNumber());
        }
        lexer.nextToken();

        id_list();
        
        if(lexer.token != Symbol.RPAR){
            error.signal("Os parametros devem estar entre parenteses! Linha " + lexer.getLineNumber());
        }
        lexer.nextToken();

        if(lexer.token != Symbol.SEMICOLON){
            error.signal("Esperado ponto e virgula na linha " + lexer.getLineNumber() + " ou anterior a ela.");
        }
        lexer.nextToken();        
    }
    
    //write_stmt -> WRITE ( id_list );
    public void write_stmt(){
        if(lexer.token != Symbol.WRITE){
            error.signal("Esperado uma declaracao de READ na linha " + lexer.getLineNumber());
        }
        lexer.nextToken();
        
        if(lexer.token != Symbol.LPAR){
            error.signal("Os parametros devem estar entre parenteses! Linha " + lexer.getLineNumber());
        }
        lexer.nextToken();

        id_list();
        
        if(lexer.token != Symbol.RPAR){
            error.signal("Os parametros devem estar entre parenteses! Linha " + lexer.getLineNumber());
        }
        lexer.nextToken();

        if(lexer.token != Symbol.SEMICOLON){
            error.signal("Esperado ponto e virgula na linha " + lexer.getLineNumber() + " ou anterior a ela.");
        }
        lexer.nextToken();        
    }
    
    //return_stmt -> RETURN expr;
    public void return_stmt(){
        if(lexer.token != Symbol.RETURN){
            error.signal("Esperado declaracao RETURN na linha " + lexer.getLineNumber());
        }
        lexer.nextToken();
        
        expr();
        
        if(lexer.token != Symbol.SEMICOLON){
            error.signal("Esperado ponto e virgula na linha " + lexer.getLineNumber() + " ou anterior a ela.");
        }
        lexer.nextToken();
    }
    
    
/******************** EXPRESSIONS ********************/ 

    //expr -> factor expr_tail
    public void expr(){
        factor();
        
        expr_tail();
    }
    
    //expr_tail -> addop factor expr_tail | empty
    public void expr_tail(){
        if(lexer.token == Symbol.PLUS || lexer.token == Symbol.MINUS){
            addop();

            factor();

            expr_tail();
        }
    }
    
    //factor -> postfix_expr factor_tail
    public void factor(){
        postfix_expr();
        
        factor_tail();
    }
    
    //factor_tail -> mulop postfix_expr factor_tail | empty
    public void factor_tail(){
        if(lexer.token == Symbol.MULT || lexer.token == Symbol.DIV){
            mulop();
            
            postfix_expr();
            
            factor_tail();
        }
    }
    
    //postfix_expr -> primary | call_expr
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
    
    //call_expr -> id ( {expr_list} )
    public void call_expr(){
        id();
        
        if(lexer.token != Symbol.LPAR){
            error.signal("As expressoes devem estar entre parenteses! Linha " + lexer.getLineNumber());
        }
        lexer.nextToken();

        expr_list();
        
        if(lexer.token != Symbol.RPAR){
            error.signal("As expressoes devem estar entre parenteses! Linha " + lexer.getLineNumber());
        }
        lexer.nextToken();
    }

    //expr_list -> expr expr_list_tail
    public void expr_list(){
        expr();
        
        expr_list_tail();
    }
    
    //expr_list_tail -> , expr expr_list_tail | empty
    public void expr_list_tail(){
        int comma = 0;
        
        if(lexer.token == Symbol.COMMA){
            comma = 1;
            lexer.nextToken();
        }
        
        if(lexer.token == Symbol.LPAR || lexer.token == Symbol.IDENT){
            if(comma == 0){
                error.signal("Uma virgula era esperada na linha " + lexer.getLineNumber());
            }
            
            expr();
            
            expr_list_tail();
        }
    }
    
    //primary -> (expr) | id | INTLITERAL | FLOATLITERAL
    public void primary(){
        if(lexer.token == Symbol.LPAR){
            lexer.nextToken();
            
            expr();
            
            if(lexer.token != Symbol.RPAR){
                error.signal("Esperado um fecha parenteses na linha " + lexer.getLineNumber());
            }
            lexer.nextToken();
        }
        else if(lexer.token == Symbol.IDENT){
            id();
        }
        else if(lexer.token == Symbol.INTLITERAL || lexer.token == Symbol.FLOATLITERAL){
            lexer.nextToken();
        }
    }
    
    //addop -> +|-
    public void addop(){
        if(lexer.token != Symbol.PLUS && lexer.token != Symbol.MINUS){
            error.signal("Um sinal de + ou - era esperado na linha " + lexer.getLineNumber());
        }
        lexer.nextToken();
    }
    
    //mulop -> * | /
    public void mulop(){
        if(lexer.token != Symbol.MULT && lexer.token != Symbol.DIV){
            error.signal("Um sinal de + ou - era esperado na linha " + lexer.getLineNumber());
        }
        lexer.nextToken();
    }
    
    
/******************** COMPLEX STATEMENTS AND CONDITIONS ********************/ 

    //if_stmt -> IF ( cond ) THEN stmt_list else_part ENDIF
    public void if_stmt(){
        if(lexer.token != Symbol.IF){
            error.signal("Uma declaracao IF era esperada na linha " + lexer.getLineNumber());
        }
        lexer.nextToken();
    
        if(lexer.token != Symbol.LPAR){
            error.signal("As condicoes devem estar entre parenteses! Linha " + lexer.getLineNumber());
        }
        lexer.nextToken();

        cond();
        
        if(lexer.token != Symbol.RPAR){
            error.signal("As expressoes devem estar entre parenteses! Linha " + lexer.getLineNumber());
        }
        lexer.nextToken();
        
        if(lexer.token != Symbol.THEN){
            error.signal("Uma declaracao ELSE era esperada na linha " + lexer.getLineNumber());
        }
        lexer.nextToken();
        
        stmt_list();
        
        if(lexer.token == Symbol.ELSE){
            else_part();
        }       
        
        
        if(lexer.token != Symbol.ENDIF){
            error.signal("Uma declaracao ENDIF era esperada na linha " + lexer.getLineNumber());
        }
        lexer.nextToken();
    }
    
    //else_part -> ELSE stmt_list | empty
    public void else_part(){
        if(lexer.token == Symbol.ELSE){
            lexer.nextToken();
            
            stmt_list();
        }
    }
    
    //cond -> expr compop expr
    public void cond(){
        expr();
        
        compop();
        
        expr();
    }
    
    //compop -> < | > | =
    public void compop(){
        if(lexer.token == Symbol.LT || lexer.token == Symbol.GT || lexer.token == Symbol.EQUAL){
            lexer.nextToken();
        }
        else{
            error.signal("Uma operacao de comparacao era esperada na linha " + lexer.getLineNumber());
        }
    }
    
    //for_stmt -> FOR ({assig_expr}; {cond}; {assign_expr}) stmt_list ENDFOR
    public void for_stmt(){
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
            assign_expr();
        }
        
        if(lexer.token != Symbol.SEMICOLON){
            error.signal("Esperado ponto e virgula na linha " + lexer.getLineNumber() + " ou anterior a ela.");
        }
        lexer.nextToken();
        
        
        //condicao de parada
        if(lexer.token != Symbol.SEMICOLON){
            cond();
        }
        
        if(lexer.token != Symbol.SEMICOLON){
            error.signal("Esperado ponto e virgula na linha " + lexer.getLineNumber() + " ou anterior a ela.");
        }
        lexer.nextToken();
        
        
        //operacao na itereacao
        if(lexer.token != Symbol.RPAR){
            assign_expr();
        }

        
        //fecha ṕarenteses
        if(lexer.token != Symbol.RPAR){
            error.signal("Os parametros devem estar entre parenteses! Linha " + lexer.getLineNumber());
        }
        lexer.nextToken();

        
        stmt_list();
        
        
        if(lexer.token != Symbol.ENDFOR){
            error.signal("Uma declaracao ENDIF era esperada na linha " + lexer.getLineNumber());
        }
        lexer.nextToken();
    }
    
	private Lexer lexer;
    private CompilerError error;
}
