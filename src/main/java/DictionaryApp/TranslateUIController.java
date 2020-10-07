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


    public TranslateUIController(){

    }

    @FXML
    private void initialize() {
        Image image = new Image(this.userInterface.getThemeBackgroundURL());
        backgroundArt.setImage(image);
    }

    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @FXML
    private void dictButtonPressed(){
        System.out.println("Dictionary Mode toggled");
        userInterface.initDictLayout();
    }

    @FXML
    private void tranButtonPressed(){

    }

    @FXML
    private void settingButtonPressed(){
        System.out.println("Setting Mode toggled");
        userInterface.initSettLayout();
    }

    @FXML
    void translateButtonClicked(ActionEvent event) {
        System.out.println("API Translate Button Clicked");
        DictionarySearcher.translateWithBachAPI(this.inputTextArea.getText(), this.outputTextArea);
    }

}
