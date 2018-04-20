package AST;
        
public class VariableStr {
    private String nome;
    private String str;
    /* nome (id) = A
    str = STRINGLITERAL*/
    public VariableStr(String nome, String str){
        this.nome = nome;
        this.str = str;
    }
    
    public String getStr(){
        return str;
    }
    
    public String getNome(){
        return nome;
    }
    /*genC VariableStr
    declara a string como ponteiro, aloca o tamanho com malloc e copia o valor com strcpy*/
    public void genC(PW pw) {
      pw.println("" + this.nome + " = malloc(sizeof("+this.str.length()+"));");
      pw.println("strcpy(" + this.nome + ", " + this.str + ");");
    }
}