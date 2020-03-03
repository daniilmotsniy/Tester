package tester;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *  Tester
 *
 *  Author DMotsniy & Mirniy18
 */

public class Main extends Application {

    //Main page with some settings
    Image icon = new Image(getClass().getResourceAsStream("icon/icon.png"));

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("fxml/login.fxml"));
        primaryStage.setTitle("Tester");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.getIcons().add(icon);
        primaryStage.show();
    }

    public static void main(String[] args) throws  Exception{ launch(args); }
}


