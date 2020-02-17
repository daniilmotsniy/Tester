package tester;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import javax.swing.text.html.ImageView;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class Test {
    int questionIndex = 0; //Number of block with questions and answers
    // block is the part of text that has 1 question and N (3) answers

    @FXML
    private Label label_time;
    @FXML
    private Label label_question;
    @FXML
    private Label label_question1;
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
    @FXML
    private Label error_lbl;
    @FXML
    private ImageView imageView;

    private RadioButton[] answ_rbs;


    // Date format
    LocalDateTime myDateObj = LocalDateTime.now();
    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    ToggleGroup answers_tgl = new ToggleGroup();

    //Path to txt file
    String path = Login.selectedTest;

    private QuestionsReader questionsReader;

    @FXML
    void initialize() {
        // Timer
        Timer start_time = new Timer();
        start_time.setLabel_time(label_time);
        start_time.f = true;
        start_time.runTime();

        //Setting toggle group
        answ_rbs = new RadioButton[]{answ_rb_0, answ_rb_1, answ_rb_2};

        for (RadioButton button : answ_rbs) {
            button.setToggleGroup(answers_tgl);
        }

        //Main methods
        try {
            questionsReader = new QuestionsReader(path, 3);
        } catch (IOException e) {
            showExceptionAndExit("Помилка при читанні з файлу", e);
        }
        //It keeps answers from user
        int[] answers = new int[questionsReader.getQuestionsCount()];
        Arrays.fill(answers, -1);
        //Set inf-n on the page
        setInformation();

        btn_finish.setOnAction(event -> {
            //Stop timer counting
            start_time.f = false;
            //Adding answer
            addAnswer(answers);

            String result = "Результат: " + result(answers);
            label_question.setText(result);
            label_question1.setText("Тест пройдено");

            //Add finish time + result in txt file
            try (FileWriter writer = new FileWriter("res/students.txt", true)) { // get name
                String current_time = myDateObj.format(myFormatObj);
                writer.append('\t').append(current_time).append('\t').append(result).write('\n');

            } catch (IOException e) {
                showExceptionAndExit("Помилка запису у файл", e);
            }

            btn_next.setDisable(true);
            btn_prev.setDisable(true);
            btn_finish.setDisable(true);
            for (RadioButton button : answ_rbs) {
                button.setVisible(false);
            }
        });

        //Next btn
        btn_next.setOnAction(event -> {
            if (questionIndex > questionsReader.getQuestionsCount() - 1) {
                questionIndex = questionsReader.getQuestionsCount() - 1;
            } else {
                addAnswer(answers);
                questionIndex++;
                setInformation();
            }

            questionChanged(answers);
        });

        //Prev btn
        btn_prev.setOnAction(event -> {
            addAnswer(answers);
            questionIndex--;
            if (questionIndex < 0)
                questionIndex = 0;
            setInformation();

            questionChanged(answers);
        });

        btn_prev.setDisable(true);
    }

    private void questionChanged(int[] answers) {
        btn_prev.setDisable(questionIndex == 0);
        btn_next.setDisable(questionIndex == 2);

        int option = answers[questionIndex];

        if (option != -1) {
            answ_rbs[option].setSelected(true);
        }
    }

    void setInformation() {
        label_question.setText(questionsReader.getQuestions()[questionIndex]);
        String[] variants = questionsReader.getVariants(questionIndex);
        for (int i = 0; i < answ_rbs.length; ++i) {
            answ_rbs[i].setText(variants[i]);
        }

        try {
            BufferedImage image = questionsReader.getPicture(questionIndex);

            if (image != null) {
                System.out.printf("Question %d has image (width = %d)", questionIndex, image.getWidth());
                
            }
        } catch (IOException e) {
            showExceptionAndExit("Помилка читання зображення", e);
        }
    }

    void addAnswer(int[] answers) {
        int selected = -1;

        for (int i = 0; i < answ_rbs.length; ++i) {
            if (answ_rbs[i].isSelected()) {
                if (selected == -1) {
                    selected = i;
                } else {
                    System.err.println("WTF several RadioButtons selected"); // TODO maybe remove
                }
            }
        }

        if (selected == -1) {
            return;
        }

        answers[questionIndex] = selected;

        System.out.println("Result: q - " + questionIndex + "/ a - " + answers[questionIndex]);

        for (RadioButton button : answ_rbs) {
            button.setSelected(false);
        }
    }

    int result(int[] answers) {
        int result = 0;

        for (int i = 0; i < answers.length; ++i) {
            if (questionsReader.getAnswerIndex(i) == answers[i]) {
                result++;
            }
        }
        return result;
    }

    private void showExceptionAndExit(String message, Exception e) { // TODO add message to GUI
        System.err.println(message + '\n' + e.getLocalizedMessage());
        error_lbl.setText(message + '\n' + e.getLocalizedMessage());
        System.exit(-1);
    }
}
