package AST;
        
import Lexer.Symbol;

public class VariableStr extends Var_type{
    private String str;
    /* nome (id) = A
    str = STRINGLITERAL*/
    public VariableStr(String nome, String str){
        super(nome, Symbol.STRING);
        this.str = str;
    }
    
    public String getStr(){
        return str;
    }
    /*genC VariableStr
    declara a string como ponteiro, aloca o tamanho com malloc e copia o valor com strcpy*/
    public void genC(PW pw) {
      
      pw.println("" + this.getNome() + " = (char *) malloc(sizeof(char) * "+this.str.length()+"));", true);
      pw.println("strcpy(" + this.getNome() + ",  \" " + this.str + "\");", true);
      pw.print("\n", false);
    }
}