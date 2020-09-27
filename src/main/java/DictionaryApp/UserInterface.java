package DictionaryApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class UserInterface extends Application {

    private Stage primaryStage;
    Scene dictScene,tranScene;


    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Dictionary Project KoyoMia100 - GUI Prototype");
        initDictLayout();
    }

    public void initDictLayout(){
        try{
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(UserInterface.class.getResource("/certainUI.fxml"));
            Parent root = loader.load();
            dictScene = new Scene(root, 1000, 768);
            primaryStage.setScene(dictScene);
            primaryStage.show();

            UserInterfaceController controller = loader.getController();
            controller.setUserInterface(this);

        } catch (IOException e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
