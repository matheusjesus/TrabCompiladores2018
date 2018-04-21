package AST;

public class Stmt {
    private final Read_stmt read;
    private final Assign_expr assign;
    private final Write_stmt write;
    private final Return_stmt ret;
    private final If_stmt if_stmt;
    private final For_stmt for_stmt;
    private final Call_expr callexpr;
    
    public Stmt(Call_expr callexpr, Assign_expr assign, Read_stmt read, Write_stmt write, Return_stmt return_stmt, If_stmt if_stmt, For_stmt for_stmt){
        this.callexpr = callexpr;
        this.assign = assign;
        this.read = read;
        this.write = write;
        this.ret = return_stmt;
        this.if_stmt = if_stmt;
        this.for_stmt = for_stmt;
    }
   
    public void genC(PW pw){
       
        if(callexpr != null)
           callexpr.genC(pw);
        
        else if(assign != null)
            assign.genC(pw);
        
        else if(read != null)
            read.genC(pw);
        
        else if(write != null)
            write.genC(pw);
        
        else if(ret != null)
            ret.genC(pw);
        
        else if(if_stmt != null)
            if_stmt.genC(pw);
        
        else if(for_stmt != null)
            for_stmt.genC(pw);
        
   } 
}
