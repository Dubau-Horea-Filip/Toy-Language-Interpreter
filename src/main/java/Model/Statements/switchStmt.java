package Model.Statements;

import Model.ADT.MyIDictionary;
import Model.ADT.MyIHeap;
import Model.ADT.MyIStack;
import Model.Expression.Exp;
import Model.Expression.RelationalExp;
import Model.PrgState;
import Model.Type.BoolType;
import Model.Type.Type;
import Model.Value.IValue;
import Model.Value.IntIValue;
import Exception.*;

public class switchStmt implements IStmt{

    private Exp exp;
    private Exp exp1;
    private IStmt stmt1;
    private Exp exp2;
    private IStmt stmt2;
    private IStmt stmt3;

    public switchStmt(Exp exp, Exp exp1, Exp exp2, IStmt stm1, IStmt stm2, IStmt stm3) {
        this.exp = exp;
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.stmt1 = stm1;
        this.stmt2 = stm2;
        this.stmt3 = stm3;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {

        MyIStack<IStmt> stk = state.getStk();
        MyIDictionary<String, IValue> symTbl = state.getSymTable();
        MyIHeap<Integer, IValue> heap = state.getHeap();


        IValue cond = exp.eval(symTbl, heap);
        IntIValue bCond = (IntIValue) cond;
        IValue cond1 = exp1.eval(symTbl, heap);
        IValue cond2 = exp2.eval(symTbl, heap);

        //if(exp==exp1) then stmt1 else (if (exp==exp2) then stmt2 else stmt3)


        IStmt iff = new IfStmt(new RelationalExp(exp, exp1, "=="),
                stmt1, new IfStmt(new RelationalExp(exp, exp2, "=="), stmt2, stmt3));
        stk.push(iff);



        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new switchStmt(exp.deepCopy(), exp1.deepCopy(), exp2.deepCopy(), stmt1.deepCopy(), stmt2.deepCopy(), stmt3.deepCopy());
    }

    //The typecheck method of switch statement verifies if exp, exp1 and exp2 have
    //the same type and also typecheck the statements stmt1, stmt2 and stmt3.
    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {

        Type expresioType = exp.typecheck(typeEnv);
        Type expresioType1 = exp1.typecheck(typeEnv);
        Type expresioType2 = exp2.typecheck(typeEnv);
        if (!expresioType.equals(expresioType1)   ) {
            throw new InvalidTypeException("expresions must be all the same type");
        }
        if (!expresioType.equals(expresioType2)   ) {
            throw new InvalidTypeException("expresions must be all the same type");
        }

        stmt1.typecheck(typeEnv.deepcopy());
        stmt2.typecheck(typeEnv.deepcopy());
        stmt3.typecheck(typeEnv.deepcopy());
        return typeEnv;
    }

    //switch(exp) (case exp1: stmt1) (case exp2: stmt2) (default: stmt3)
    @Override
    public String toString() {
        return "Switch(" + exp.toString() + ") (case (" + exp1.toString() + "):" + stmt1.toString() +
                ") \n case (" + exp2.toString() + "):" + stmt2.toString() + ")"
                + "\n( default: " + this.stmt3.toString();
    }
}
