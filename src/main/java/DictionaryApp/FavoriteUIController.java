package DictionaryApp;

import Model.Dictionary;
import Model.Word;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Callback;

import javax.speech.AudioException;
import javax.speech.EngineException;
import java.beans.PropertyVetoException;
import java.io.IOException;


/**
 * The type User interface controller.
 */
public class FavoriteUIController {

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
    private Label StartupNote;

    @FXML
    private ListView<String> favoriteListUI;
    private ObservableList<String> favoriteObservableList = FXCollections.observableArrayList();

    @FXML
    private Label certainWordTitle;

    @FXML
    private WebView certainWordDefinition;
    private WebEngine certainWordDefinitionWE;
    private String certainWordDefinitionString;

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

    private TheVoice su;
    private Dictionary nativeDict;

    public FavoriteUIController(){

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
        nativeDict = Dictionary.getInstance();

        fav = new TheFavorite();
        fav.initialize();

        initializeWebView();
        initializeTheVoice();
        initializeBackground();
        setWordListStyle();
        wordListListener();

        initializeFavoriteList();
    }

    @FXML
    private void initializeFavoriteList(){
        favoriteObservableList = FXCollections.observableArrayList(fav.getFavList());

        if (favoriteObservableList.isEmpty()){
            hideButtons();
            favoriteListUI.setItems(favoriteObservableList);
            StartupNote.setText("There's nothing in your favorite list. Add and study now <3");
            certainWordDefinitionWE.loadContent("");
        }
        else {
            favoriteListUI.setItems(favoriteObservableList);
            favoriteListUI.getSelectionModel().select(0);
        }
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
        favoriteListUI.setStyle("-fx-font-size: 19px; -fx-font-family: 'SF Pro Rounded Regular';");
    }


    @FXML
    private void wordListListener(){
        favoriteListUI.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Selected word changed from " + oldValue + " to " + newValue);
            if (newValue != null) {
                showButtons();
                try {
                    certainWordTitle.setText(newValue);
                    certainWordDefinitionString = fav.getDefinition(newValue);
                    certainWordDefinitionWE.loadContent(fav.getDefinition(newValue));
                    try {
                        favButton.setSelected(fav.isAdded(newValue));
                        if (fav.isAdded(newValue) == true){
                            favButton.setText("★  Favorited");
                        }
                        else {
                            favButton.setText("★  Favorite");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
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
    private void favButtonToggle() throws IOException {
        if (fav.isAdded(certainWordTitle.getText()) == true){
            fav.unmarkFav(certainWordTitle.getText());
            favButton.setText("★  Favorite");
            initializeFavoriteList();
        }
        else {
            fav.markFav(certainWordTitle.getText(),certainWordDefinitionString);
            favButton.setText("★  Favorited");
            initializeFavoriteList();
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
    }

    @FXML
    private void dictButtonPressed(){
        System.out.println("Dictionary Mode toggled");
        userInterface.initDictLayout();
    }

    @FXML
    private void favlistButtonPressed(){

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
