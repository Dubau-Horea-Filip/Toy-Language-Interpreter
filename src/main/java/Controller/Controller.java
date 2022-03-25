package Controller;

import Exception.*;
import Model.ADT.MyIHeap;
import Model.ADT.MyIStack;
import Model.ADT.MyList;
import Model.PrgState;
import Model.Statements.IStmt;
import Model.Value.IValue;
import Model.Value.RefIValue;
import Repository.IRepo;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Controller {
    IRepo repo;
    ExecutorService executor;

    public IRepo getRepo()
    {
        return this.repo;
    }

    public Controller(IRepo repo) {
        this.repo = repo;
    }

    Map<Integer, IValue> unsafeGarbageCollector(List<Integer> symTableAddr, Map<Integer, IValue> heap) {
        return heap.entrySet().stream()
                .filter(e -> symTableAddr.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    Set<Integer> getAddrFromSymTable(Collection<IValue> symTableValues, MyIHeap<Integer, IValue> heap) {
        Set<Integer> result = new HashSet<>();
        symTableValues.stream()
                .filter(v -> v instanceof RefIValue)
                .forEach(v -> {
                    RefIValue v1 = (RefIValue) v;
                    result.add(v1.getAddress());
                    var heapValue = heap.lookup(v1.getAddress());
                    while (heapValue instanceof RefIValue) {
                        result.add(((RefIValue) heapValue).getAddress());
                        heapValue = heap.lookup(((RefIValue) heapValue).getAddress());
                    }
                });
        return result;
    }


    List<PrgState> removeCompletedPrg(List<PrgState> inPrgList) {
        return inPrgList.stream()
                .filter(PrgState::isNotCompleted)
                .collect(Collectors.toList());
    }


    public void allStep() throws InterruptedException, MyException {
        executor = Executors.newFixedThreadPool(2);
        //remove the completed programs
        List<PrgState> prgList = removeCompletedPrg(repo.getPrgList());
        if (prgList.size() > 0) {


            List<Integer> addrset = new ArrayList<>();
            for (PrgState prg : prgList) {
                addrset.addAll(getAddrFromSymTable(prg.getSymTable().getContent().values(), prg.getHeap()));
            }
            for(PrgState prg : prgList) {
                prg.getHeap().setContent(unsafeGarbageCollector(addrset,prg.getHeap().getContent()));
            }


            oneStepForAllPrg(prgList);
            //remove the completed programs
            prgList = removeCompletedPrg(repo.getPrgList());
        }
        executor.shutdownNow();
        repo.setPrgList( prgList);
    }

/*
    void oneStepForAllPrg(List<PrgState> prgList) throws InterruptedException {
        //before the execution, print the PrgState List into the log file
        prgList.forEach(prg -> {
            int iid = prg.getIID();
            System.out.println(iid);
            try {
                repo.logPrgStateExec(prg);
            } catch (MyException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //RUN concurrently one step for each of the existing PrgStates
        //-----------------------------------------------------------------------
        //prepare the list of callables
        List<Callable<PrgState>> callList = prgList.stream()
                .map((PrgState p) -> (Callable<PrgState>) (p::oneStep))
                .collect(Collectors.toList());

        //start the execution of the callables
        //it returns the list of new created PrgStates (namely threads)

        List<PrgState> newPrgList = executor.invokeAll(callList).stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        //here you can treat the possible
                        // exceptions thrown by statements
                        // execution, namely the green part
                        // from previous allStep method
                    }
                    return null;
                }).filter(p -> p != null)
                .collect(Collectors.toList());

        //add the new created threads to the list of existing threads
        prgList.addAll(newPrgList);
        //------------------------------------------------------------------------------
        //after the execution, print the PrgState List into the log file

        //Save the current programs in the repository
        repo.setPrgList( prgList);
    }

 */
public void oneStepForAllPrg(List<PrgState> prgList) throws MyException {

    prgList.forEach(prg -> {
        int iid = prg.getIID();
        System.out.println(iid);
        try {
            repo.logPrgStateExec(prg);
        } catch (MyException e) {
            e.printStackTrace();
        }
    });

    List<Callable<PrgState>> callList = prgList.stream()
            .map((PrgState p) -> (Callable<PrgState>) (p::oneStep))
            .collect(Collectors.toList());

    List<PrgState> newPrgList = null;
    try {
        newPrgList = executor.invokeAll(callList).stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    } catch (InterruptedException e) {
        System.out.println("Could not invoke the callables!");
    }

    if (newPrgList == null) throw new MyException("PrgList null");
    prgList.addAll(newPrgList);

    prgList.forEach(prg -> {
        int iid = prg.getIID();
        System.out.println(iid);
        try {
            repo.logPrgStateExec(prg);
        } catch (MyException e) {
            e.printStackTrace();
        }
    });

    repo.setPrgList(prgList);
}

}



