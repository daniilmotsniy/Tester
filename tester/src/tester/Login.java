package tester;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Login {

    ObservableList<String> langs_group = FXCollections.observableArrayList("КС-12", "КС-12", "КС-12", "КС-12"); //DSAW
    ObservableList<String> langs_test = FXCollections.observableArrayList();//

    LocalDateTime myDateObj = LocalDateTime.now();
    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    final File folder_tests = new File("res/tests"); //DSAW Folder with tests

    public static String selectedTest; //DSAW

    @FXML
    private AnchorPane rootPaneLogin;
    @FXML
    private TextField txt_field_name;
    @FXML
    private ComboBox<String> cmb_group;
    @FXML
    private ComboBox<String> cmb_test;
    @FXML
    private Button btn_start;
    @FXML
    private Label lbl_error;

    @FXML
    void initialize() {
        //Cmb with groups
        cmb_group.setItems(langs_group);
        cmb_group.getSelectionModel().select(0);
        //Cmb with tests
        receiveTests(folder_tests, langs_test);
        cmb_test.setItems(langs_test);
        cmb_test.getSelectionModel().select(0);
        txt_field_name.setText("Git Bush :3");

        btn_start.setOnAction(event -> {
                if(txt_field_name.getText().isEmpty()){
                    lbl_error.setText("Заповніть поле!");
                } else {
                    lbl_error.setText("");

                        //DSAW Get selected test
                        selectedTest = cmb_test.getSelectionModel().getSelectedItem();

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
                        }
                        catch(IOException ex){
                            System.out.println(ex.getMessage());
                        }

                        try {
                            AnchorPane pane = FXMLLoader.load(getClass().getResource("fxml/test.fxml"));
                            rootPaneLogin.getChildren().setAll(pane);
                        }
                        catch (Exception e)
                        {
                            System.out.println(e.getMessage());
                        }
                }
        });

    }
    
    //DSAW Adding test name to array
    void receiveTests(final File folder, ObservableList<String> langs_test){
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                receiveTests(fileEntry, langs_test);
            } else {
                langs_test.add(fileEntry.getName());
            }
        }
    }

}

