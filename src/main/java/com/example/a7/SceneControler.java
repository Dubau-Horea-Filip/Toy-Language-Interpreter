package com.example.a7;

import Controller.Controller;
import Model.ADT.MyDictionary;
import Model.ADT.MyHeap;
import Model.ADT.MyList;
import Model.ADT.MyStack;
import Model.PrgState;
import Model.Statements.IStmt;
import Repository.Repo;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import Exception.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SceneControler implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;


    private final HelloApplication a = new HelloApplication();
    private final ArrayList<IStmt> programs = a.getStatements();

    @FXML
    private ListView<String> myList;
    @FXML
    private Label myLable;

    String food[] = {"ramen", "pizza", "sushi"};
    String currentprogram;


    public void runProgram(ActionEvent event) throws IOException {
        int index = myList.getSelectionModel().getSelectedIndex();

        if (index < 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a program first");
            alert.showAndWait();
        }
        try {

        IStmt selectedProg = programs.get(index);



        /*
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main.fxml"));
        HelloController mainController = new HelloController(c);
        fxmlLoader.setController(mainController);
        Scene scene = new Scene(fxmlLoader.load());
        mainController.displayprog(currentprogram);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

         */



           // FXMLLoader mainLoader = new FXMLLoader();
            //mainLoader.setLocation(getClass().getResource("main.fxml"));

            //Parent mainRoot = mainLoader.load();
           // MainController mainController = mainLoader.getController();
            //Stage mainStage = new Stage();
           // Scene mainScene = new Scene(mainRoot);
           // mainStage.setScene(mainScene);


        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main.fxml"));
        Parent mainRoot = fxmlLoader.load();
       // stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        MainController scene2controler = fxmlLoader.getController();

        Stage mainStage = new Stage();
        Scene mainScene = new Scene(mainRoot);
        scene2controler.displayprog(currentprogram);
        scene2controler.setcontroler(selectedProg);
        scene2controler.initializefirst();

        mainStage.setScene(mainScene);
        mainStage.show();

          //  Scene scene = new Scene(fxmlLoader.load());
        //stage.setTitle("Hello!");
        //stage.setScene(scene);
        //stage.show();







        } catch (IOException | MyException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        for (IStmt prg : programs) {
            myList.getItems().add(prg.toString());
        }


        myList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                currentprogram = myList.getSelectionModel().getSelectedItem();
                myLable.setText(currentprogram);
            }
        });
    }
}
