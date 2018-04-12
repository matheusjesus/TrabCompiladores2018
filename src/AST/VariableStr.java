package AST;
        
public class VariableStr {
    private String nome;
    private String str;
    
    public VariableStr(String nome, String str){
        this.nome = nome;
        this.str = str;
    }
    
    public String getStr(){
        return str;
    }
    
    //fazer o genC() declarando a string como ponteiro, alocando ela pro tamanho correto, e copiando o valor de str pra ela.
}