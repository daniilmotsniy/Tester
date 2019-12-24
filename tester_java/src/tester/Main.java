package tester;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileReader;
import java.util.Scanner;
import java.util.Vector;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("Tester");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
    }

    public static void main(String[] args) throws  Exception{

        Information txt = new Information();

            FileReader reader = new FileReader("res/tests/test1.txt");

            Scanner scan = new Scanner(reader);

            /*
            while (scan.hasNextLine()) {
                text.addElement(scan.nextLine());
            }

            reader.close();

            for(int i = 0; i < text.size(); i++){
                System.out.println(text.get(i));
            } */

            launch(args);
    }
}
