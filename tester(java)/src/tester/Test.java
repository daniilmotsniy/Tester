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
    int i = 0;

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
        setInformation(i);

        btn_next.setOnAction(event -> {
            i++;
            if(i > text.size()/4 - 1){
                i=text.size()/4 - 1;
            }
            setInformation(i);
        });

        btn_prev.setOnAction(event -> {
            i--;
            if(i<0){
                i=0;
            }
            setInformation(i);
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

    void setInformation(int i){
        label_question.setText(text.elementAt( i*4));
        answ_rb_1.setText(text.elementAt(1 + i*4));
        answ_rb_2.setText(text.elementAt(2 + i*4));
        answ_rb_3.setText(text.elementAt(3 + i*4));
    }

}
