package Model.Statements;

import Exception.MyException;
import Model.ADT.MyIDictionary;
import Model.ADT.MyIStack;
import Model.PrgState;
import Model.Type.Type;


public class CompStmt implements  IStmt{
    IStmt first;
    IStmt snd;

    //constructor, getters
    public CompStmt(IStmt f, IStmt s){ this.first=f; this.snd=s;}
    public IStmt getFirst(){return this.first;}
    public IStmt getSecond(){return this.snd;}

    @Override
    public String toString() {
        return first.toString() + ";\n" + snd.toString(); //(int x; x=4)
    }

    public PrgState execute(PrgState state)  throws MyException {
        MyIStack<IStmt> stk=state.getStk();
        stk.push(snd);
        stk.push(first);
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new CompStmt(first.deepCopy(),snd.deepCopy());
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        //MyIDictionary<String,Type> typEnv1 = first.typecheck(typeEnv);
        //MyIDictionary<String,Type> typEnv2 = snd.typecheck(typEnv1);
        //return typEnv2;
        return snd.typecheck(first.typecheck(typeEnv));
    }
}
