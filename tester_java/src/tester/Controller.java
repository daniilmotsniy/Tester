package tester;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Controller {

    @FXML
    private Label label_question;

    @FXML
    private Button btn_check;

    @FXML
    void initialize() {
        btn_check.setOnAction(event -> {
            if(label_question.getText().isEmpty()){
                label_question.setText("Hi!");
            } else {
                label_question.setText("");
            }
        });

    }

}
