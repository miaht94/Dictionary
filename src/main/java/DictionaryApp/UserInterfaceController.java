package DictionaryApp;

import Model.Word;
import com.jfoenix.controls.JFXButton;
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
import Model.Dictionary;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Callback;


/**
 * The type User interface controller.
 */
public class UserInterfaceController {

    private UserInterface userInterface;

    @FXML
    private JFXButton enviButton;
    @FXML
    private JFXButton vienButton;
    @FXML
    private JFXButton tranButton;

    @FXML
    private TextField searchBox;
    @FXML
    private JFXButton searchButton;

    @FXML
    private ListView<Word> wordListNativeUI;
    private ObservableList<Word> certainResultOL = FXCollections.observableArrayList();

    @FXML
    private Label certainWordTitle;
    @FXML
    private WebView certainWordDefinition;
    private WebEngine certainWordDefinitionWE;

    private long previousPressedTime = System.currentTimeMillis();


    public UserInterfaceController(){

    }

    @FXML
    private void initialize() {
        Dictionary nativeDict = Dictionary.getInstance();
        nativeDict.searchWord("a", certainResultOL);
        showResult();

        initializeWebView();
        setWordListStyle();

        searchBoxListener(nativeDict);
        wordListListener();
    }

    @FXML
    private void setWordListStyle(){
        //wordListNativeUI.setStyle(getClass().getClassLoader().getResource("certainUIStyle.css").toString());
        wordListNativeUI.setStyle("-fx-font-size: 21px; -fx-font-family: 'SF Pro Rounded Regular';");
    }

    @FXML
    private void searchBoxListener(Dictionary nativeDict){
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime-previousPressedTime >= 1000) {
                System.out.println("textfield changed from " + oldValue + " to " + newValue);
                nativeDict.searchWord(newValue, certainResultOL);
                showResult();
            } else {
                previousPressedTime = currentTime;
            }
        });
    }

    @FXML
    private void wordListListener(){
        wordListNativeUI.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Selected word changed from " + oldValue + " to " + newValue);
            System.out.println(newValue.getTitle());
            System.out.println(newValue.getXML());
            certainWordTitle.setText(newValue.getTitle());
            certainWordDefinitionWE.loadContent(newValue.getXML());
        });
    }


    @FXML
    private void initializeWebView() {
        this.certainWordDefinitionWE = this.certainWordDefinition.getEngine();
        this.certainWordDefinitionWE.setUserStyleSheetLocation(getClass().getClassLoader().getResource("dictionaryStyle.css").toString());

    }

    /**
     * Only show Word Title property through getTitle() on wordListNativeUI list.
     * Define property ListView should show reference:
     * https://stackoverflow.com/questions/24597911/how-to-define-which-property-listview-should-use-to-render
     *
     */
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
