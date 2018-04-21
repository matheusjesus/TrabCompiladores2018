package AST;

public class Program {
    private Id ident;
    private Pgm_body body;
    
    public Program(Id ident, Pgm_body body){
        this.ident = ident;
        this.body = body;
    }
    
    public void genC(PW pw){
        pw.println("#include <stdio.h>", true);
        pw.println("#include <string.h>\n", true);
        if(body != null)
            body.genC(pw);
    }
}
