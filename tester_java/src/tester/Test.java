package tester;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Test {

    @FXML
    private Label label_question;

    @FXML
    private Button btn_prev;

    @FXML
    private Button btn_next;

    @FXML
    void initialize() {
        btn_next.setOnAction(event -> {
            if(label_question.getText().isEmpty()){
                label_question.setText("Hi!");
            } else {
                label_question.setText("");
            }
        });

    }

}
