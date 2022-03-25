package Repository;

import Exception.MyException;
import Model.ADT.MyList;
import Model.PrgState;

import java.io.IOException;
import java.util.List;

public interface IRepo
{

    void addPrg(PrgState prg) ;
    void logPrgStateExec(PrgState p_state) throws MyException;
    void setPrgList(List<PrgState> list);
    List<PrgState> getPrgList();
}
