package tester;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.FileWriter;
import java.io.IOException;

public class Login {

    ObservableList<String> langs = FXCollections.observableArrayList("КС-11", "КС-12", "КС-12", "КС-14");

    @FXML
    private TextField txt_field_name;

    @FXML
    private ComboBox<String> cmb_group;

    @FXML
    private Button btn_start;

    @FXML
    private Label lbl_error;

    @FXML
    void initialize() {

        cmb_group.setItems(langs);

        btn_start.setOnAction(event -> {
                if(txt_field_name.getText().isEmpty()){
                    lbl_error.setText("Заповніть поля!");
                } else {
                    lbl_error.setText("");

                    try(FileWriter writer = new FileWriter("res/test.txt", false))      // get name
                    {
                        String text = txt_field_name.getText();
                        writer.write(text);
                        writer.flush();
                    }
                    catch(IOException ex){
                        System.out.println(ex.getMessage());
                    }
                }

        });

    }

}
