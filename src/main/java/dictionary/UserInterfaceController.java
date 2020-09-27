package dictionary;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener;
import javafx.collections.FXCollections;

import dictionary.UserInterface;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


/**
 * The type User interface controller.
 */
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
    private ListView<String> wordListNativeUI;
    @FXML
    private TextField searchBox;


    private ObservableList<String> sampleData = FXCollections.observableArrayList();



    private UserInterface userInterface;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public UserInterfaceController(){

    }

    @FXML
    private void initialize() {
        // Sample Data
        sampleData.add("Viet Anh");
        sampleData.add("Bach 100");
        sampleData.add("Bach 20");
        sampleData.add("ngu vl");

        // Set wordListNativeUI
        wordListNativeUI.setStyle("-fx-font-size: 21px; -fx-font-family: 'SF Pro Rounded Regular';");
        wordListNativeUI.setItems(sampleData);

        // searchBox listener init...
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            // if searchBox.textProperty() change then do search
            System.out.println("textfield changed from " + oldValue + " to " + newValue);
            wordListNativeUI.setItems(sampleData);
        });





    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param userInterface the user interface
     */
    public void setUserInterface(UserInterface userInterface){
        this.userInterface = userInterface;

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
    @FXML
    private void searchButtonPressed(){
        System.out.println("Search button pressed");

    }

}
