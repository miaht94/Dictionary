package DictionaryApp;

import Model.Dictionary;
import Model.DictionarySearcher;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.speech.EngineException;
import java.io.*;
import java.net.URL;

public class UserInterface extends Application {
    private Stage primaryStage;
    Scene dictScene,favScene,tranScene,settScene;
    private static int theme;
    private static String themeBackgroundURL;
    private static String translateRequest;
    UserInterfaceController controller;
    //private Object UserInterfaceController = new UserInterfaceController();

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Koyomia Dicitonary 2.0");
        setTheme(1);
        initDictLayout();
        this.primaryStage.setResizable(false);

    }

    @Override
    public void stop() throws EngineException {
        System.out.println("Stage is closing");
        Dictionary.appendAddedWordToFile();
        DictionarySearcher.executor.shutdown();
        controller.theVoiceTerminate();
    }

    public URL loadFile(String input){
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(input);
        return resource;
    }

    public void initDictLayout(){
        try{
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(loadFile("certainUI.fxml"));
            //loader.setLocation(UserInterface.class.getClass().getResource("/certainUI.fxml"));

            Parent root = loader.load();
            dictScene = new Scene(root, 1000, 768);

            dictScene.getStylesheets().add(loadFile("certainUIStyle.css").toString());
            //dictScene.getStylesheets().add(getClass().getClassLoader().getResource("certainUIStyle.css").toString());
            primaryStage.setScene(dictScene);
            primaryStage.show();

            //loader.setController(UserInterfaceController);
            controller = loader.getController();
            controller.setUserInterface(this);

        } catch (IOException e){
            e.printStackTrace();
        }

    }

    public void initFavLayout(){
        try{
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(UserInterface.class.getResource("/favoriteUI.fxml"));
            Parent root = loader.load();
            favScene = new Scene(root, 1000, 768);

            favScene.getStylesheets().add(getClass().getClassLoader().getResource("certainUIStyle.css").toString());
            //dictScene.getStylesheets().add(getClass().getClassLoader().getResource("certainUIStyle.css").toString());
            primaryStage.setScene(favScene);
            primaryStage.show();

            FavoriteUIController controller = loader.getController();
            controller.setUserInterface(this);

        } catch (IOException e){
            e.printStackTrace();
        }

    }

    public void initTranLayout(){
        try{
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(UserInterface.class.getResource("/translateUI.fxml"));
            Parent root = loader.load();
            tranScene = new Scene(root, 1000, 768);
            tranScene.getStylesheets().add(getClass().getClassLoader().getResource("certainUIStyle.css").toString());
            primaryStage.setScene(tranScene);
            primaryStage.show();

            TranslateUIController controller = loader.getController();
            controller.setUserInterface(this);

        } catch (IOException e){
            e.printStackTrace();
        }

    }

    public void initTranLayout(String input){
        setTranslateRequest(input);
        initTranLayout();
    }

    public void setTranslateRequest(String input){
        translateRequest = input;
        System.out.println("set translate request to "+ translateRequest);
    }

    public static void deleteTranslateRequest(){
        translateRequest = null;
    }

    public static String getTranslateRequest(){
        return translateRequest;
    }
    public void initSettLayout(){
        try{
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(UserInterface.class.getResource("/settingUI.fxml"));
            Parent root = loader.load();
            settScene = new Scene(root, 1000, 768);
            settScene.getStylesheets().add(getClass().getClassLoader().getResource("certainUIStyle.css").toString());
            primaryStage.setScene(settScene);
            primaryStage.show();

            SettingUIController controller = loader.getController();
            controller.setUserInterface(this);

        } catch (IOException e){
            e.printStackTrace();
        }

    }

    public void setTheme(int a){
        this.theme = a;
        this.themeBackgroundURL = getClass().getClassLoader().getResource("background/background" + a + ".png").toString();
        System.out.println("theme is now " + theme);
        System.out.println("theme background set to " + themeBackgroundURL);
    }

    public static String getThemeBackgroundURL(){
        return themeBackgroundURL;
    };

    public static int getTheme(){
        return theme;
    };

    public static void main(String[] args) {
        System.setProperty("Dfile.encoding","UTF-8");
        launch(args);
    }
}
