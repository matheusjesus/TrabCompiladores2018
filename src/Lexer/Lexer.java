package Lexer;

import java.util.*;
import Error.*;

public class Lexer {

	// apenas para verificacao lexica
	public static final boolean DEBUGLEXER = false; 
    
    public Lexer( char []input, CompilerError error ) {
        this.input = input;
        // add an end-of-file label to make it easy to do the lexer
        input[input.length - 1] = '\0';
        // number of the current line
        lineNumber = 1;
        tokenPos = 0;
        this.error = error;
    }
    
    // contains the keywords
    private static final Hashtable<String, Symbol> keywordsTable;
    
    // this code will be executed only once for each program execution
    static {
        keywordsTable = new Hashtable<>();
        keywordsTable.put( "BEGIN", Symbol.BEGIN );
        keywordsTable.put( "END", Symbol.END );
        keywordsTable.put( "PROGRAM", Symbol.PROGRAM);
        keywordsTable.put( "FUNCTION", Symbol.FUNCTION);
        keywordsTable.put( "EOF", Symbol.EOF );
        keywordsTable.put( "READ", Symbol.READ );
        keywordsTable.put( "WRITE", Symbol.WRITE );
        keywordsTable.put( "IF", Symbol.IF );
        keywordsTable.put( "THEN", Symbol.THEN );
        keywordsTable.put( "ELSE", Symbol.ELSE );
        keywordsTable.put( "ENDIF", Symbol.ENDIF );
        keywordsTable.put( "RETURN", Symbol.RETURN );
        keywordsTable.put( "FOR", Symbol.FOR );
        keywordsTable.put( "ENDFOR", Symbol.ENDFOR );
        keywordsTable.put( "FLOAT", Symbol.FLOAT );
        keywordsTable.put( "INT", Symbol.INT );
        keywordsTable.put( "VOID", Symbol.VOID );
        keywordsTable.put( "STRING", Symbol.STRING );
    }
    
    public Symbol checkNextToken(){
        int backupTokenPos = tokenPos;
        int backupLineNumber = lineNumber;
        Symbol backupToken = token;
        int backupIntNumber = IntNumber;
        float backupFloatNumber = FloatNumber;
        String backupStringValue = stringValue;

        if(DEBUGLEXER)
            System.out.print("Prox: ");
        
        Symbol retorno = nextToken();

        tokenPos = backupTokenPos;
        lineNumber = backupLineNumber;
        token = backupToken;
        IntNumber = backupIntNumber;
        FloatNumber = backupFloatNumber;
        stringValue = backupStringValue;

        return retorno;
    }
    
    
    public Symbol nextToken() {
        lastTokenPos = tokenPos - 1;

        
        //pula os caracteres de espacamento e pula linha
        while(Character.isWhitespace(input[tokenPos]) == true){
            if (input[tokenPos] == '\n'){
                lineNumber++;
            }
            tokenPos++;
        }
        
        //verifica se chegou ao final do arquivo
        if (input[tokenPos] == '\0'){
            token = Symbol.EOF;
            return token;
        }
        
        //verifica se eh comentario
        if (input[tokenPos] == '-' && input[tokenPos+1] == '-'){
            tokenPos = tokenPos +2;
            while(input[tokenPos] != '\n' && input[tokenPos] != '\0'){
                tokenPos++;
            }
            this.nextToken();
            return token;
        }
        
        //pula verificacao de stringliteral
        if (input[tokenPos] == '"'){
            tokenPos++;
            
            stringValue = "";
            while(input[tokenPos] != '"' && input[tokenPos] != '\0'){
                stringValue += input[tokenPos];
                tokenPos++;
            }
            tokenPos++;
            
            token = Symbol.STRINGLITERAL;
            
            if(DEBUGLEXER)
                System.out.println(token.toString() + "\n\n");
            
            return token;
        }
        

        //Reconhecer o Token:
        StringBuffer aux = new StringBuffer();
        int isFloat = 0;
        while (Character.isDigit(input[tokenPos]) || input[tokenPos] == '.'){
            
            //verifica se tem . se sim, concluimos que o num eh um float
            if(input[tokenPos] == '.'){
                isFloat = 1;
            }
            
            //concatenar esses digitos na string aux
            aux = aux.append(input[tokenPos]);
            tokenPos++;
        }
    
        if (aux.length() > 0){
            if(isFloat == 1){
                //converte string para inteiro
                FloatNumber = Float.parseFloat(aux.toString());
                if (FloatNumber > Float.MAX_VALUE){
                    error.show("O valor e maior que o valor maximo de um float.");
                }
                token = Symbol.FLOATLITERAL;

                if(DEBUGLEXER)
                    System.out.println(token.toString() + "\n\n");
                
            }
            else{
                //converte string para inteiro
                IntNumber = Integer.parseInt(aux.toString());
                if (IntNumber > MaxValueInteger){
                    error.show("O valor e maior que o valor maximo de um int.");
                }
                this.token = Symbol.INTLITERAL;
                if(DEBUGLEXER)
                    System.out.println(token.toString() + "\n\n");
                
                //nextToken();
            }            
        }
        else{
            if(Character.isLetter(input[tokenPos])){
                int tamstr = 0;
                while (Character.isLetter(input[tokenPos]) || Character.isDigit(input[tokenPos])){
                    aux = aux.append(input[tokenPos]);
                    tokenPos++;
                    tamstr++;
                }
                
                if(tamstr > 31){
                    error.show("O tamanho maximo de um identificador eh de 31 caracteres! Valor ultrapassado" );
                }
            }
            
            if (aux.length() > 0){
                Symbol temp;
                temp = keywordsTable.get(aux.toString());
                if (temp == null){
                    token = Symbol.IDENT;
                    stringValue = aux.toString();
                }
                else {
                    token = temp;
                }
            }
            else if(input[tokenPos] == ':' && input[tokenPos+1] == '='){
                token = Symbol.ASSIGN;
                tokenPos++;
                tokenPos++;
            }
            else {
                switch (input[tokenPos]){
                    case '+':
                        token = Symbol.PLUS;
                        break;
                    case '-':
                        token = Symbol.MINUS;
                        break;
                    case '/':
                        token = Symbol.DIV;
                        break;
                    case '*':
                        token = Symbol.MULT;
                        break;
                    case '=':
                        token = Symbol.EQUAL;
                        break;
                    case ',':
                        token = Symbol.COMMA;
                        break;
                    case ';':
                        token = Symbol.SEMICOLON;
                        break;
                    case '<':
                        token = Symbol.LT;
                        break;
                    case '>':
                        token = Symbol.GT;
                        break;
                    case '(':
                        token = Symbol.LPAR;
                        break;
                    case ')':
                        token = Symbol.RPAR;
                        break;
                    default:
                        error.show("erro lexico");
                }
                tokenPos++;
            }
        }        
        if (DEBUGLEXER)
            System.out.println(token.toString() + "\n\n");

        return token;
    }
    
    // return the line number of the last token got with getToken()
    public int getLineNumber() {
        return lineNumber;
    }
    
    public String getCurrentLine() {
        int i = lastTokenPos;
        if ( i == 0 )
            i = 1;
        else
            if ( i >= input.length )
                i = input.length;
        
        StringBuffer line = new StringBuffer();
        // go to the beginning of the line
        while ( i >= 1 && input[i] != '\n' )
            i--;
        if ( input[i] == '\n' )
            i++;
        // go to the end of the line putting it in variable line
        while ( input[i] != '\0' && input[i] != '\n' && input[i] != '\r' ) {
            line.append( input[i] );
            i++;
        }
        return line.toString();
    }
    
    public String getStringValue() {
        return stringValue;
    }
    
    public int getIntNumber() {
        return IntNumber;
    }
    
    public float getFloatNumber() {
        return FloatNumber;
    }
    
    public char getCharValue() {
        return charValue;
    }
    // current token
    public Symbol token;
    private String stringValue;
    private int IntNumber;
    private float FloatNumber;
    private char charValue;
    
    private int  tokenPos;
    //  input[lastTokenPos] is the last character of the last token
    private int lastTokenPos;
    // program given as input - source code
    private char []input;
    
    // number of current line. Starts with 1
    private int lineNumber;
    
    private CompilerError error;
    private static final int MaxValueInteger = 32768;
}
