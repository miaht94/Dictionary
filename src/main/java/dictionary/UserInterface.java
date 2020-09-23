package dictionary;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class UserInterface extends Application {

    Stage dictWindow;
    Scene dictScene,tranScene;


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/certainUI.fxml"));
        dictWindow = primaryStage;
        dictWindow.setTitle("Dictionary from UET with love");
        dictScene = new Scene(root, 1000, 720);
        dictWindow.setScene(dictScene);
        dictWindow.show();



        //setOnAction(new EventHandler<ActionEvent event>{
        //      @Override
        //      public void handle(ActionEvent event){
        //
        //      }
        // }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
