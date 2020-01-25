package tester;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

public class Test {

    Vector<String> text = new Vector();
    HashMap<Integer, Integer> answers = new HashMap<>();

    int i = 0; //Number of block with questions and answers

    @FXML
    private Label label_question;
    @FXML
    private Button btn_prev;
    @FXML
    private Button btn_next;
    @FXML
    private RadioButton answ_rb_0;
    @FXML
    private RadioButton answ_rb_1;
    @FXML
    private RadioButton answ_rb_2;

    ToggleGroup answers_tgl = new ToggleGroup();

    @FXML
    void initialize() throws Exception {

        answ_rb_0.setToggleGroup(answers_tgl);
        answ_rb_1.setToggleGroup(answers_tgl);
        answ_rb_2.setToggleGroup(answers_tgl);

        getText("res/tests/test1.txt");
        //printText(text);
        setInformation(i);

        btn_next.setOnAction(event -> {
            i++;
                if(i > text.size()/4 - 1)
                    i = text.size()/4 - 1;
            setInformation(i);
            addAnswer(i);
        });

        btn_prev.setOnAction(event -> {
            i--;
                if(i<0)
                    i = 0;
            setInformation(i);
            addAnswer(i);
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
        if(i==text.size()/4 - 1){
            btn_next.setText("Завершити");
            Timer finish_time = new Timer(false);
        } else {
            btn_next.setText("Наступне");
        }
        label_question.setText(text.elementAt( i*4));
        answ_rb_0.setText(text.elementAt(1 + i*4));
        answ_rb_1.setText(text.elementAt(2 + i*4));
        answ_rb_2.setText(text.elementAt(3 + i*4));
    }

    void addAnswer(int i) {
        if (answ_rb_0.isSelected() == true && answ_rb_1.isSelected() == false && answ_rb_2.isSelected() == false) {
            answers.put(i, 0);
        } else if (answ_rb_0.isSelected() == false && answ_rb_1.isSelected() == true && answ_rb_2.isSelected() == false) {
            answers.put(i, 1);
        } else if (answ_rb_0.isSelected() == false && answ_rb_1.isSelected() == false && answ_rb_2.isSelected() == true) {
            answers.put(i, 2);
        } else {
            System.out.printf("Error");
        }
        System.out.println("Result: q - "+i+"/ a - " + answers.get(i));
    }
}
