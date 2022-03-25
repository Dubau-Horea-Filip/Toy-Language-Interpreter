package View;

import Controller.Controller;
import Exception.MyException;

import java.io.IOException;

public class RunExample extends Command {
    private Controller ctr;
    public RunExample(String key, String desc,Controller ctr){
        super(key, desc);
        this.ctr=ctr;
    }
    @Override
    public void execute() throws  MyException, IOException {
        try{
            ctr.allStep(); }
        catch (MyException | InterruptedException e)
        {
            System.out.println(e.getMessage());
        } //here you must treat the exceptions that can not be solved in the controller
    }
}