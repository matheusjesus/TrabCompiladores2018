package AST;

import Lexer.Symbol;

public abstract class Stmt {
    abstract public void genC(PW pw);
    //abstract Symbol setTipo();


}
