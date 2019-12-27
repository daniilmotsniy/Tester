package tester;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;

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
    private RadioButton answ_rb_1;
    @FXML
    private RadioButton answ_rb_2;
    @FXML
    private RadioButton answ_rb_3;

    @FXML
    void initialize() throws Exception {

        getText("res/tests/test1.txt");
        printText(text);

        //answ_rb_1.setText(text.elementAt(0));
        //answ_rb_2.setText(text.elementAt(1));

            for (int i = 0; i < 4; ++i) {
                if (i % 4 == 1) {
                    answ_rb_1.setText(text.elementAt(0));
                } else {
                    label_question.setText(text.elementAt(3));
                }
            }

        btn_next.setOnAction(event -> {
            if(label_question.getText().isEmpty()){
                label_question.setText("Hi!");
            } else {
                label_question.setText("");
            }
        });

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
