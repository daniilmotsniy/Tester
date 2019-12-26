package tester;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("fxml/login.fxml"));
        primaryStage.setTitle("Tester");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
    }

    public void pageLoad(String fxml_url){

        //Here I need to close the window (login.fxml)

        //Loading new fxml
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxml_url));

        //Checking for exception
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Setting parameters
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setTitle("Tester");
        stage.show();
        //Icon
        //Image icon = new Image(getClass().getResourceAsStream("icon/cross.png"));
        //stage.getIcons().add(icon);
    }

    public static void main(String[] args) throws  Exception{
            launch(args);
    }


}


