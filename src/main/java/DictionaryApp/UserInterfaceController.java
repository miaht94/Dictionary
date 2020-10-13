package DictionaryApp;

import Model.*;
import com.jfoenix.controls.JFXButton;
import javafx.event.EventHandler;
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
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import javafx.scene.image.ImageView;

import java.io.FileNotFoundException;


/**
 * The type User interface controller.
 */
public class UserInterfaceController {

    private UserInterface userInterface;

    @FXML
    private JFXButton dictButton;
    @FXML
    private JFXButton tranButton;
    @FXML
    private JFXButton settingButton;

    @FXML
    private TextField searchBox;

    @FXML
    private Label StartupNote;

    @FXML
    private ListView<Word> wordListNativeUI;
    private ObservableList<Word> certainResultOL = FXCollections.observableArrayList();

    @FXML
    private Label certainWordTitle;
    @FXML
    private WebView certainWordDefinition;
    private WebEngine certainWordDefinitionWE;

    @FXML
    private ImageView backgroundArt;

    public UserInterfaceController(){

    }

    /**
     * Is called by the main application to give a reference back to itself.
     * @param userInterface the user interface
     */
    public void setUserInterface(UserInterface userInterface){
        this.userInterface = userInterface;
    }

    @FXML
    private void initialize() throws FileNotFoundException {
        Dictionary nativeDict = Dictionary.getInstance(DictionaryType.EN_VI);
        nativeDict.searchWord("", certainResultOL);
        showResult();

        Image image = new Image(this.userInterface.getThemeBackgroundURL());
        backgroundArt.setImage(image);

        initializeWebView();
        setWordListStyle();

        searchBoxListener(nativeDict);
        wordListListener();

        searchBox.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER))
                {
                    nativeDict.searchWord(searchBox.getText(), certainResultOL);
                    //DictionaryUtils.listToObservableList(nativeDict.getWordsFromDB(0,10000), certainResultOL);
                    showResult();
                    if (wordListNativeUI.getSelectionModel().getSelectedItem() == null){
                        System.out.println("No words found. Change to Translate.");
                        userInterface.initTranLayout(searchBox.getText());
                    }
                }
            }
        });

    }

    @FXML
    private void setWordListStyle(){
        //wordListNativeUI.setStyle(getClass().getClassLoader().getResource("certainUIStyle.css").toString());
        wordListNativeUI.setStyle("-fx-font-size: 19px; -fx-font-family: 'SF Pro Rounded Regular';");
    }

    @FXML
    private void searchBoxListener(Dictionary nativeDict){
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            long currentTime = System.currentTimeMillis();
            if (newValue == ""){
                StartupNote.setOpacity(1);
            } else {
                StartupNote.setOpacity(0);
            }
            System.out.println("textfield changed from " + oldValue + " to " + newValue);
            long count_start = System.currentTimeMillis();
            nativeDict.searchWord(newValue, certainResultOL);
            //DictionaryUtils.listToObservableList(nativeDict.getWordsFromDB(0,10000), certainResultOL);
            showResult();
            System.out.println(System.currentTimeMillis() - count_start);
        });
    }

    @FXML
    private void wordListListener(){
        wordListNativeUI.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Selected word changed from " + oldValue + " to " + newValue);

            if (newValue != null) {
                System.out.println(newValue.getTitle());
                System.out.println(newValue.getXML());
                certainWordTitle.setText(newValue.getTitle());
                certainWordDefinitionWE.loadContent(newValue.getXML());
            }

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

        wordListNativeUI.getSelectionModel().select(0);
    }

    @FXML
    private void dictButtonPressed(){

    }

    @FXML
    private void tranButtonPressed(){
        System.out.println("Translate Mode toggled");
        userInterface.initTranLayout();
    }

    @FXML
    private void settingButtonPressed(){
        System.out.println("Setting Mode toggled");
        userInterface.initSettLayout();
    }

}
