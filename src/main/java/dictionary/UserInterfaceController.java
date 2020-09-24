package dictionary;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener;
import javafx.collections.FXCollections;

import dictionary.UserInterface;


public class UserInterfaceController {
    @FXML
    private JFXButton enviButton;
    @FXML
    private JFXButton vienButton;
    @FXML
    private JFXButton tranButton;
    @FXML
    private JFXButton searchButton;
    @FXML
    private JFXListView<String> wordListUI;
    @FXML
    private TextField searchBox;


    private UserInterface userInterface;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public UserInterfaceController(){

    }

    @FXML
    private void initialize(){

    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param userInterface
     */

    public void setUserInterface(UserInterface userInterface){
        this.userInterface = userInterface;

        // Add observableList dictionary data later to the JFXListView
        //wordListUI.setItems(ObservableList<String> name);
    }

    @FXML
    private void enviButtonPressed(){
        System.out.println("English - Vietnamese Mode toggled");
        enviButton.setOpacity(1.0);
        vienButton.setOpacity(0.5);
        tranButton.setOpacity(0.5);
    }
    @FXML
    private void vienButtonPressed(){
        System.out.println("Vietnamese - English Mode toggled");
        vienButton.setOpacity(1.0);
        enviButton.setOpacity(0.5);
        tranButton.setOpacity(0.5);
    }
    @FXML
    private void tranButtonPressed(){
        System.out.println("Translate Mode toggled");
        tranButton.setOpacity(1.0);
        enviButton.setOpacity(0.5);
        vienButton.setOpacity(0.5);
    }

}
