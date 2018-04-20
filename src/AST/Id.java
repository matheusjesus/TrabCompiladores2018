package AST;

public class Id {
    private String name;
    
    public Id (String name){
        this.name = name;
    }
    
    public String getId(){
        return this.name;
    }
    
    /*public void genC(PW pw){
        System.out.println("Identificador: " + this.name);
    }*/
}
