package DictionaryApp;

import Model.DictionarySearcher;
import Model.DictionaryUtils;
import Model.Word;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import com.jfoenix.controls.JFXToggleNode;
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
import Model.Dictionary;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import javafx.scene.image.ImageView;

import java.beans.PropertyVetoException;
import java.io.*;

import DictionaryApp.TheVoice;

import javax.speech.AudioException;
import javax.speech.EngineException;


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
    private JFXToggleNode favButton;
    TheFavorite fav;
    @FXML
    private JFXButton ttsButton;

    @FXML
    private ImageView backgroundArt;

    private TheVoice su;


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
    private void initialize() throws IOException, AudioException, EngineException, PropertyVetoException {
        Dictionary nativeDict = Dictionary.getInstance();
        fav = new TheFavorite();
        //nativeDict.searchWord("", certainResultOL);
        //showResult();

        fav.initialize();
        initializeTheVoice();
        initializeBackground();
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
    private void initializeTheVoice() throws AudioException, EngineException, PropertyVetoException {
        su = new TheVoice();
        su.init("kevin16");
        ttsButton.setOpacity(0);
    }

    @FXML
    private void initializeBackground(){
        Image image = new Image(this.userInterface.getThemeBackgroundURL());
        backgroundArt.setImage(image);
    }

    @FXML
    private void setWordListStyle(){
        wordListNativeUI.setStyle("-fx-font-size: 19px; -fx-font-family: 'SF Pro Rounded Regular';");
    }

    @FXML
    private void searchBoxListener(Dictionary nativeDict){
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            long currentTime = System.currentTimeMillis();
            if (newValue == ""){
                StartupNote.setOpacity(1);
                ttsButton.setOpacity(0);
            } else {
                StartupNote.setOpacity(0);
                ttsButton.setOpacity(1);
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
                try {
                    favButton.setSelected(fav.isAdded(newValue.getTitle()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
    private void ttsButtonPressed() throws AudioException, EngineException, PropertyVetoException, InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                su.doSpeak(wordListNativeUI.getSelectionModel().selectedItemProperty().get().getTitle());
            } catch (EngineException | AudioException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.setDaemon(false);
        thread.start();
    }

    public void theVoiceTerminate() throws EngineException {
        su.terminate();
    }

    @FXML
    private void favButtonToggle() throws IOException {
        if (fav.isAdded(certainWordTitle.getText()) == true){
            fav.unmarkFav(certainWordTitle.getText());
        }
        else {
            fav.markFav(certainWordTitle.getText());
        }
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
