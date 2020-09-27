package DictionaryApp;

import Model.Word;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

/**
 * Import Model only.
 */
import DictionaryApp.UserInterface;
import Model.Dictionary;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Callback;


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
    private ListView<Word> wordListNativeUI;
    @FXML
    private TextField searchBox;
    @FXML
    private Label certainWordTitle;


    private UserInterface userInterface;

    private ObservableList<Word> certainResultOL = FXCollections.observableArrayList();

    @FXML
    private WebView certainWordDefinition;
    private WebEngine certainWordDefinitionWE;

    private long previousPressedTime = System.currentTimeMillis();

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public UserInterfaceController(){

    }

    String HTML_STRING = "Hello";


    /**
     *
     * Define property ListView should show reference: https://stackoverflow.com/questions/24597911/how-to-define-which-property-listview-should-use-to-render
     *
     */
    @FXML
    private void initialize() {
        // Sample Data
        Dictionary nativeDict = Dictionary.getInstance();
        nativeDict.searchWord("a", certainResultOL);
        showResult();

        certainWordDefinition = new WebView();
        certainWordDefinitionWE = certainWordDefinition.getEngine();

        // Set wordListNativeUI
        wordListNativeUI.setStyle("-fx-font-size: 21px; -fx-font-family: 'SF Pro Rounded Regular';");

        // searchBox listener init...
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            long currentTime = System.currentTimeMillis();
            //System.out.println("textfield changed from " + oldValue + " to " + newValue);
            if (currentTime-previousPressedTime >= 1000) {
                nativeDict.searchWord(newValue, certainResultOL);
                showResult();
            } else {
                previousPressedTime = currentTime;
            }
        });

        wordListNativeUI.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("selected word changed from " + oldValue + " to " + newValue);
            System.out.println(newValue.getTitle());
            System.out.println(newValue.getXML());

            certainWordTitle.setText(newValue.getTitle());
            certainWordDefinitionWE.loadContent(newValue.getXML());
        });
    }


    @FXML
    private void showResult() {
        wordListNativeUI.setItems(certainResultOL);
        wordListNativeUI.setCellFactory(new Callback<ListView<Word>, ListCell<Word>>() {
            @Override
            public ListCell<Word> call(ListView<Word> param) {
                return new ListCell<Word>() {
                    @Override
                    public void updateItem(Word item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            setText(null);
                        } else {
                            // assume MyDataType.getSomeProperty() returns a string
                            setText(item.getTitle());
                        }
                    }
                };
            }
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
