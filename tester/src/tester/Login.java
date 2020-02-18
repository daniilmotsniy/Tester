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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Login {

    ObservableList<String> langs_group = FXCollections.observableArrayList("КС-12", "КС-12", "КС-12", "КС-12");
    List<File> langs_test = new ArrayList<>();

    LocalDateTime myDateObj = LocalDateTime.now();
    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    final File folder_tests = new File("res/tests"); //Folder with tests

    public static String selectedTest;

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
        receiveTests(folder_tests);

        // set each ComboBox item to test file name's (without extension)
        cmb_test.setItems(langs_test.stream()
                .map(Login::getNameWithoutExtension)
                .collect(Collectors.collectingAndThen(Collectors.toSet(), FXCollections::observableArrayList)).sorted());

        cmb_test.getSelectionModel().select(0);

        txt_field_name.setText("Git Bush"); // DBG

        btn_start.setOnAction(event -> {
            if (txt_field_name.getText().isEmpty()) {
                lbl_error.setText("Заповніть поле!");
            } else if (!txt_field_name.getText().matches(
                    "^ *[A-Za-zА-Яа-яёЁЇїІіЄєҐґ'\\-]+ +[A-Za-zА-Яа-яёЁЇїІіЄєҐґ'\\- ]+ *$")) {
                lbl_error.setText("Некоректне ім'я.");
            } else {
                lbl_error.setText("");

                // find test file with selected name
                selectedTest = langs_test.stream().filter(
                        i -> getNameWithoutExtension(i).equals(cmb_test.getSelectionModel().getSelectedItem())
                ).collect(Collectors.toList()).get(0).getPath();

                try (FileWriter writer = new FileWriter("res/students.txt", true))      // get name
                {
                    String name_str = txt_field_name.getText();
                    String group_str = cmb_group.getSelectionModel().getSelectedItem();
                    String current_time = myDateObj.format(myFormatObj);
                    writer.append(name_str).append('\t').append(group_str).append('\t').append(current_time).write('\n');
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    AnchorPane pane = FXMLLoader.load(getClass().getResource("fxml/test.fxml"));
                    rootPaneLogin.getChildren().setAll(pane);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void receiveTests(final File folder) {
        //noinspection ConstantConditions
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                receiveTests(fileEntry);
            } else {
                langs_test.add(fileEntry);
            }
        }
    }

    private static String getNameWithoutExtension(File f) {
        String name = f.getName();
        return name.substring(0, name.lastIndexOf('.'));
    }
}

