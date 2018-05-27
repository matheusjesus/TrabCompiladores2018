package AST;

import java.io.PrintWriter;

public class PW {
    int currentIndent = 0;
    static public final int green = 0, java = 1;
    int mode = green; 
    public int step = 4;
    public PrintWriter out;
    static final private String space = "                                                                                                        ";

    
    public void add() {
      currentIndent += step;
    }
    public void sub() {
      currentIndent -= step;
    }
   
    public void set( PrintWriter out ) {
      this.out = out;
      currentIndent = 0;
    }
   
    public void set( int indent ) {
       currentIndent = indent;
    }

    public void print( String s, boolean ident ) {
        if(ident == true)
            out.print( space.substring(0, currentIndent) );
        
        out.print(s);
    }

    public void println( String s, boolean ident ) {
        if(ident == true)
            out.print( space.substring(0, currentIndent) );
       
        out.println(s);
    }
}
