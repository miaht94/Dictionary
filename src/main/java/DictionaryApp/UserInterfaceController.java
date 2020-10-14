package DictionaryApp;

import Model.*;
import com.jfoenix.controls.*;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import javafx.scene.image.ImageView;

import java.beans.PropertyVetoException;
import java.io.*;

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
    private JFXButton favlistButton;
    @FXML
    private JFXButton tranButton;
    @FXML
    private JFXButton settingButton;

    @FXML
    private TextField searchBox;

    @FXML
    private JFXToggleButton languageButton;
    @FXML
    private Label languageTitle;

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
    private Rectangle ttsButtonFrame;
    @FXML
    private Rectangle favButtonFrame;


    @FXML
    private ImageView speakerIcon;

    @FXML
    private ImageView backgroundArt;

    @FXML
    private JFXButton addModeButton;
    @FXML
    private JFXButton removeModeButton;
    @FXML
    private Rectangle backlit;

    //ADD
    @FXML
    private Rectangle addDialog;
    @FXML
    private JFXTextField addTitleString;
    @FXML
    private JFXTextField addDefinitionString;
    @FXML
    private JFXButton confirmButton;
    @FXML
    private JFXButton cancelButton;

    private TheVoice su;
    private Dictionary nativeDict;
    private String certainWordDefinitionString;
    private String languageMode;

    private final String welcome = "Type a word to look up in Koyomia Dicitonary.";
    private final String nowordsfound = "No words found. Press Enter to translate online.";


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
        searchBox.setText("");
        nativeDict = Dictionary.getInstance(DictionaryType.EN_VI);
        languageMode = "EN_VI";
        languageButton.setSelected(false);
        languageTitle.setText("English - Vietnamese");
        nativeDict.searchWord("a", certainResultOL);
        showResult();

        hideMode();

        fav = new TheFavorite();
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
        Image speakerImage = new Image(getClass().getClassLoader().getResource("speaker.png").toString());
        backgroundArt.setImage(image);
        speakerIcon.setImage(speakerImage);
    }

    @FXML
    private void setWordListStyle(){
        wordListNativeUI.setStyle("-fx-font-size: 19px; -fx-font-family: 'SF Pro Rounded Regular';");
    }

    @FXML
    private void searchBoxListener(Dictionary nativeDict){
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            long currentTime = System.currentTimeMillis();
            if (searchBox.getText().isEmpty()){
                nativeDict.searchWord("a", certainResultOL);
                showResult();
                certainWordDefinitionWE.loadContent("");
                hideButtons();
                StartupNote.setText(welcome);
            } else {
                System.out.println("searchBox value changed from " + oldValue + " to " + newValue);
                long count_start = System.currentTimeMillis();
                nativeDict.searchWord(newValue, certainResultOL);
                //DictionaryUtils.listToObservableList(nativeDict.getWordsFromDB(0,10000), certainResultOL);
                showResult();
                System.out.println(System.currentTimeMillis() - count_start);

                if (certainResultOL.isEmpty()){
                    certainWordDefinitionWE.loadContent("");
                    hideButtons();
                    StartupNote.setText(nowordsfound);
                    removeModeButton.setDisable(true);
                    removeModeButton.setOpacity(0.25);
                }
                else {
                    showButtons();
                }
            }
        });
    }

    @FXML
    private void wordListListener(){
        wordListNativeUI.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Selected word changed from " + oldValue + " to " + newValue);
            if (newValue != null) {
                showButtons();
                System.out.println(newValue.getTitle());
                System.out.println(newValue.getXML());
                certainWordTitle.setText(newValue.getTitle());
                certainWordDefinitionString = newValue.getXML();
                certainWordDefinitionWE.loadContent(newValue.getXML());
                try {
                    favButton.setSelected(fav.isAdded(newValue.getTitle()));
                    if (fav.isAdded(newValue.getTitle()) == true){
                        favButton.setText("★  Favorited");
                    }
                    else {
                        favButton.setText("★  Favorite");
                    }
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
                if (certainWordTitle.getText() != null){
                    su.doSpeak(certainWordTitle.getText());
                    //su.doSpeak(wordListNativeUI.getSelectionModel().selectedItemProperty().get().getTitle());
                }
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
    private void languageButtonToggle(){
        if (languageMode == "EN_VI"){
            languageButton.setSelected(true);
            nativeDict = Dictionary.getInstance(DictionaryType.VI_EN);
            languageMode = "VI_EN";
            languageTitle.setText("Vietnamese - English");
            foundBehavior();
        }
        else {
            languageButton.setSelected(false);
            nativeDict = Dictionary.getInstance(DictionaryType.EN_VI);
            languageMode = "EN_VI";
            languageTitle.setText("English - Vietnamese");
            foundBehavior();
        }
    }

    private void foundBehavior(){
        if (searchBox.getText().isEmpty()){
            nativeDict.searchWord("a", certainResultOL);
            showResult();
            certainWordDefinitionWE.loadContent("");
            hideButtons();
            StartupNote.setText(welcome);
        }
        else {
            nativeDict.searchWord(searchBox.getText(), certainResultOL);
            showResult();
            if (certainResultOL.isEmpty()){
                certainWordDefinitionWE.loadContent("");
                StartupNote.setText(nowordsfound);
                hideButtons();
            } else {
                showButtons();
            }

        }
    }

    @FXML
    private void favButtonToggle() throws IOException {
        if (fav.isAdded(certainWordTitle.getText()) == true){
            fav.unmarkFav(certainWordTitle.getText());
            favButton.setText("★  Favorite");
        }
        else {
            fav.markFav(certainWordTitle.getText(),certainWordDefinitionString);
            favButton.setText("★  Favorited");
        }
    }

    private void showButtons(){
        ttsButton.setOpacity(1);
        ttsButtonFrame.setOpacity(1);
        favButtonFrame.setOpacity(1);
        favButton.setOpacity(1);
        speakerIcon.setOpacity(1);
        favButton.setDisable(false);
        ttsButton.setDisable(false);
        StartupNote.setOpacity(0);
        removeModeButton.setDisable(false);
        removeModeButton.setOpacity(1);
    }

    private void hideButtons(){
        ttsButton.setOpacity(0);
        ttsButtonFrame.setOpacity(0);
        favButtonFrame.setOpacity(0);
        favButton.setOpacity(0);
        speakerIcon.setOpacity(0);
        favButton.setDisable(true);
        ttsButton.setDisable(true);
        StartupNote.setOpacity(1);
        removeModeButton.setDisable(true);
        removeModeButton.setOpacity(0.5);
    }

    @FXML
    private void addModeButtonPressed(){
        showAddMode();
    }

    @FXML
    private void removeModeButtonPressed(){
        showRemoveMode();
    }

    @FXML
    private void cancelButtonPressed(){
        hideMode();
    }

    private boolean mode;
    //mode = false -> add
    //mode = true -> remove

    @FXML
    private void confirmButtonPressed() throws PropertyVetoException, AudioException, EngineException, IOException {
        if (mode == false){

            //ADD MODE
            Word s = new Word(addTitleString.getText(),addDefinitionString.getText());
            nativeDict.addWord(s);
            hideMode();
            initialize();
        } else {
            //REMOVE MODE
            if (wordListNativeUI.getSelectionModel().getSelectedItem() != null)
                nativeDict.delWord(wordListNativeUI.getSelectionModel().getSelectedItem());
            hideMode();
            initialize();
        }
    }

    private void showAddMode(){
        mode = false;
        confirmButton.setTextFill(Color.GRAY);
        confirmButton.setDisable(true);

        addTitleString.textProperty().addListener((observable, oldValue, newValue) -> {
            if  (addTitleString.getText().isEmpty() || addDefinitionString.getText().isEmpty()){
                confirmButton.setTextFill(Color.GRAY);
                confirmButton.setDisable(true);
            } else {
                confirmButton.setTextFill(Color.GREEN);
                confirmButton.setDisable(false);
            }
        });

        addDefinitionString.textProperty().addListener((observable, oldValue, newValue) -> {
            if  (addTitleString.getText().isEmpty() || addDefinitionString.getText().isEmpty()){
                confirmButton.setTextFill(Color.GRAY);
                confirmButton.setDisable(true);
            } else {
                confirmButton.setTextFill(Color.GREEN);
                confirmButton.setDisable(false);
            }
        });
        backlit.setOpacity(0.25);
        addDialog.setOpacity(1);
        addTitleString.setOpacity(1);
        addDefinitionString.setOpacity(1);
        confirmButton.setOpacity(1);
        cancelButton.setOpacity(1);
        backlit.setDisable(false);
        addDialog.setDisable(false);
        addTitleString.setDisable(false);
        addDefinitionString.setDisable(false);



        cancelButton.setDisable(false);

        addTitleString.setText("");
        addTitleString.setPromptText("Enter title");

        addDefinitionString.setText("");
        addDefinitionString.setPromptText("Enter definition");

        confirmButton.setText("ADD");

    }

    private void hideMode(){
        backlit.setOpacity(0);
        addDialog.setOpacity(0);
        addTitleString.setOpacity(0);
        addDefinitionString.setOpacity(0);
        confirmButton.setOpacity(0);
        cancelButton.setOpacity(0);
        backlit.setDisable(true);
        addDialog.setDisable(true);
        addTitleString.setDisable(true);
        addDefinitionString.setDisable(true);
        confirmButton.setDisable(true);
        cancelButton.setDisable(true);

    }

    private void showRemoveMode(){
        mode = true;
        backlit.setOpacity(0.25);
        addDialog.setOpacity(1);
        addTitleString.setOpacity(1);
        addDefinitionString.setOpacity(1);
        confirmButton.setOpacity(1);
        cancelButton.setOpacity(1);
        backlit.setDisable(false);
        addDialog.setDisable(false);

        addTitleString.setDisable(true);
        addDefinitionString.setDisable(true);

        confirmButton.setDisable(false);
        cancelButton.setDisable(false);
        addTitleString.setText(wordListNativeUI.getSelectionModel().getSelectedItem().getTitle());
        addDefinitionString.setText("Do you want to remove this word?");
        confirmButton.setText("REMOVE");
        confirmButton.setTextFill(Color.RED);

    }

    @FXML
    private void dictButtonPressed(){

    }

    @FXML
    private void favlistButtonPressed(){
        System.out.println("Favorite Mode toggled");
        userInterface.initFavLayout();
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
