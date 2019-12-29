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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Login extends Main {

    ObservableList<String> langs = FXCollections.observableArrayList("КС-11", "КС-12", "КС-12", "КС-14");
    LocalDateTime myDateObj = LocalDateTime.now();
    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

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
        cmb_group.getSelectionModel().select(0);

        btn_start.setOnAction(event -> {
                if(txt_field_name.getText().isEmpty()){
                    lbl_error.setText("Заповніть поле!");
                } else {
                    lbl_error.setText("");

                    try(FileWriter writer = new FileWriter("res/students.txt", true))      // get name
                    {
                        String name_str = txt_field_name.getText();
                        String group_str = cmb_group.getSelectionModel().getSelectedItem();
                        String current_time = myDateObj.format(myFormatObj);
                        writer.write(name_str);
                        writer.append('\t');
                        writer.write(group_str);
                        writer.append('\t');
                        writer.write(current_time);
                        writer.append('\n');
                    }
                    catch(IOException ex){
                        System.out.println(ex.getMessage());
                    }
                    pageLoad("fxml/test.fxml");
                }


        });



    }

}
