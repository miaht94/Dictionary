package DictionaryApp;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Import Model only.
 */


/**
 * The type User interface controller.
 */
public class SettingUIController {

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
    private JFXComboBox<String> themePicker;

    private ObservableList<String> themeList = FXCollections.observableArrayList("Overflow","Lipton","ULIS","DARK 100","Lime", "Kawaii");

    private long previousPressedTime = System.currentTimeMillis();


    public SettingUIController(){

    }

    @FXML
    private void initialize() {
        Image image = new Image(this.userInterface.getThemeBackgroundURL());
        backgroundArt.setImage(image);

        themePicker.setStyle("-fx-font-size: 18px;");
        themePicker.setPromptText(themeList.get(userInterface.getTheme()-1));
        themePicker.setItems(themeList);
        themePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
           if (newValue == "Overflow")          themeChange(1);
           else if (newValue == "Lipton")       themeChange(2);
           else if (newValue == "ULIS")         themeChange(3);
           else if (newValue == "DARK 100")     themeChange(4);
           else if (newValue == "Kawaii")         themeChange(5);
           else if (newValue == "Lime")         themeChange(6);

           System.out.println("Theme changed from" + oldValue + " to " + newValue);
        });

    }

    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    private void themeChange(int a){
        this.userInterface.setTheme(a);
        Image newImage = new Image(this.userInterface.getThemeBackgroundURL());
        backgroundArt.setImage(newImage);
        System.out.println("changed to" + this.userInterface.getThemeBackgroundURL());
    }

    @FXML
    private void favlistButtonPressed(){
        System.out.println("Favorite Mode toggled");
        userInterface.initFavLayout();
    }

    @FXML
    private void dictButtonPressed(){
        System.out.println("Dictionary Mode toggled");
        userInterface.initDictLayout();
    }
    @FXML
    private void tranButtonPressed(){
        System.out.println("Translate Mode toggled");
        userInterface.initTranLayout();
    }
    @FXML
    private void settingButtonPressed(){
    }

}
