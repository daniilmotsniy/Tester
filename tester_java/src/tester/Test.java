package tester;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.FileReader;
import java.util.Scanner;
import java.util.Vector;

public class Test {

    Vector<String> text = new Vector();

    @FXML
    private Label label_question;

    @FXML
    private Button btn_prev;

    @FXML
    private Button btn_next;

    @FXML
    void initialize() throws Exception {
        btn_next.setOnAction(event -> {
            if(label_question.getText().isEmpty()){
                label_question.setText("Hi!");
            } else {
                label_question.setText("");
            }
        });

        getText("res/tests/test1.txt");
        printText(text);

    }


    void getText(String file_adress) throws  Exception {

        FileReader reader = new FileReader(file_adress);

        Scanner scan = new Scanner(reader);

        while (scan.hasNextLine()) {
            text.add(scan.nextLine());
        }

        reader.close();
    }

    void printText(Vector<String> text){
        for(int i = 0; i < text.size(); i++){
            System.out.println(text.get(i));
        }
    }

}
