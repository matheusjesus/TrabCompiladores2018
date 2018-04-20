package AST;

public class Program {
    private Id ident;
    private Pgm_body body;
    
    public Program(Id ident, Pgm_body body){
        this.ident = ident;
        this.body = body;
    }
    
    public void genC(PW pw){
        pw.println("#include <stdio.h>\n");
        pw.println("#include <string.h>\n");
        pw.println("");
        pw.println("int main() {");
        pw.add();
        pw.println(ident.toString());
        if(body != null)
            body.genC(pw);
        pw.println("return 0;");
        pw.sub();
        pw.println("}");
    }
}
