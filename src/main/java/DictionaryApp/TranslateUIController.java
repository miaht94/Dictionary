package DictionaryApp;

import Model.DictionarySearcher;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Import Model only.
 */


/**
 * The type User interface controller.
 */
public class TranslateUIController {

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
    private ImageView backgroundArt;
    @FXML
    private JFXTextArea inputTextArea;
    @FXML
    private JFXTextArea outputTextArea;

    private long previousPressedTime = System.currentTimeMillis();


    public TranslateUIController() {

    }

    @FXML
    private void initialize() {

        Image image = new Image(this.userInterface.getThemeBackgroundURL());
        backgroundArt.setImage(image);
        System.out.println("translate request is " + UserInterface.getTranslateRequest());
        if (UserInterface.getTranslateRequest() != null) {
            System.out.println("get translate request successfully");
            inputTextArea.setText(UserInterface.getTranslateRequest());
            DictionarySearcher.translateWithBachAPI(UserInterface.getTranslateRequest(), this.outputTextArea);
            UserInterface.deleteTranslateRequest();
        }
    }

    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @FXML
    private void dictButtonPressed() {
        System.out.println("Dictionary Mode toggled");
        userInterface.initDictLayout();
    }

    @FXML
    private void tranButtonPressed() {

    }

    @FXML
    private void settingButtonPressed() {
        System.out.println("Setting Mode toggled");
        userInterface.initSettLayout();
    }

    @FXML
    private void favlistButtonPressed(){
        System.out.println("Favorite Mode toggled");
        userInterface.initFavLayout();
    }

    @FXML
    void translateButtonClicked(ActionEvent event) {
        System.out.println("API Translate Button Clicked");
        DictionarySearcher.translateWithBachAPI(this.inputTextArea.getText(), this.outputTextArea);
    }

}
