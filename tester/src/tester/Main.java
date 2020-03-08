package tester;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *  Tester
 *
 *  Author DMotsniy & Mirniy18
 */

public class Main extends Application {

    static void printError(String error_text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(error_text);
        alert.showAndWait();
    }

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



