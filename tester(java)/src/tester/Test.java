package tester;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Test {
    //Here we have all the txt file
    LinkedList<String> text = new LinkedList<>();
    //It keeps answers from user
    HashMap<Integer, Character> answers = new HashMap<>();
    //It keeps true answers
    HashMap<Integer, Character> true_answers = new HashMap<>();

    int i = 0; //Number of block with questions and answers
    // block is the part of text that has 1 question and N (3) answers

    @FXML
    private Label label_time;
    @FXML
    private Label label_question;
    @FXML
    private Label label_result;
    @FXML
    private Button btn_prev;
    @FXML
    private Button btn_next;
    @FXML
    private Button btn_finish;
    @FXML
    private RadioButton answ_rb_0;
    @FXML
    private RadioButton answ_rb_1;
    @FXML
    private RadioButton answ_rb_2;

    // Date format
    LocalDateTime myDateObj = LocalDateTime.now();
    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    ToggleGroup answers_tgl = new ToggleGroup();

    //Path to txt file
    String path = "res/tests/test1.txt";

    @FXML
    void initialize() throws Exception {
        // Timer
        Timer start_time = new Timer();
        start_time.setLabel_time(label_time);
        start_time.f = true;
        start_time.runTime();

        //Setting togle group
        answ_rb_0.setToggleGroup(answers_tgl);
        answ_rb_1.setToggleGroup(answers_tgl);
        answ_rb_2.setToggleGroup(answers_tgl);

        //Main methods
        getText();
        getTrueAnswers();
        removeTrueSymbol();
        setInformation(i);

        btn_finish.setOnAction(event -> {
            //Stop timer counting
            start_time.f = false;
            //Adding answ
            addAnswer(i);

            String result = "Резульат: " + result(answers, true_answers);
            label_result.setText(result);

            //Add finish time + result in txt file
            try(FileWriter writer = new FileWriter("res/students.txt", true))      // get name
            {
                String current_time = myDateObj.format(myFormatObj);
                writer.append('\t');
                writer.write(current_time);
                writer.append('\t');
                writer.write(result);
                writer.append('\n');
            }
            catch(IOException ex){
                System.out.println(ex.getMessage());
            }
        });

        //Next btn
        btn_next.setOnAction(event -> {
            if(i > text.size()/4 - 1){
                i = text.size()/4 - 1;
            } else {
                setInformation(i);
                addAnswer(i);
            }
            i++;
        });

        //Prev btn
        btn_prev.setOnAction(event -> {
            i--;
                if(i<0)
                    i = 0;
            setInformation(i);
            addAnswer(i);
        });

    }


    void getText() throws  Exception {
        FileReader reader = new FileReader(path);
        Scanner scan = new Scanner(reader);
        while (scan.hasNextLine()) {
            text.add(scan.nextLine());
        }
        reader.close();
    }

    void getTrueAnswers() throws Exception {
        FileReader reader = new FileReader(path);
        Scanner scan = new Scanner(reader);
        int j = 0;
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            if(line.contains("+")){
                   true_answers.put(j, line.charAt(0));
                   j++;
            }
        }
        reader.close();
    }

    void removeTrueSymbol(){
        for (int i = 0; i < text.size(); i++){
            text.set(i, text.get(i).replace('+', ' '));
        }
    }

//    void printText(HashMap<Integer, Character> text){
//        for(int i = 0; i < text.size(); i++){
//            System.out.println(text.get(i));
//        }
//    }

    void setInformation(int i){
        btn_next.setText("Наступне");

        label_question.setText(text.get( i*4));
        answ_rb_0.setText(text.get(1 + i*4));
        answ_rb_1.setText(text.get(2 + i*4));
        answ_rb_2.setText(text.get(3 + i*4));
    }

    void addAnswer(int i) {
        if (answ_rb_0.isSelected() == true && answ_rb_1.isSelected() == false && answ_rb_2.isSelected() == false) {
            answers.put(i, '1');
        } else if (answ_rb_0.isSelected() == false && answ_rb_1.isSelected() == true && answ_rb_2.isSelected() == false) {
            answers.put(i, '2');
        } else if (answ_rb_0.isSelected() == false && answ_rb_1.isSelected() == false && answ_rb_2.isSelected() == true) {
            answers.put(i, '3');
        } else {
            System.out.printf("Error");
        }
        System.out.println("Result: q - "+i+"/ a - " + answers.get(i));
    }

    int result(HashMap<Integer, Character> answers, HashMap<Integer, Character> true_answers){
        int res = 0;

        for(int i = 0; i < true_answers.size(); i++){
            if(true_answers.get(i) == answers.get(i)){
                res++;
            }
        }
        return res;
    }
}
