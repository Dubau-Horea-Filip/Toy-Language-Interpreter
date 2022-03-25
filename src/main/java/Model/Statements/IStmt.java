package Model.Statements;

import Exception.*;
import Model.ADT.MyIDictionary;
import Model.PrgState;
import Model.Type.Type;

public interface IStmt {
    PrgState execute(PrgState state) throws MyException;
    public IStmt deepCopy();
    MyIDictionary<String, Type> typecheck(MyIDictionary<String,Type> typeEnv) throws MyException;
    }







