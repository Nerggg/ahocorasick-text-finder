package com.ahocorasicktextfinder.Controller;

import com.ahocorasicktextfinder.Classes.AhoCorasick;
import com.ahocorasicktextfinder.Classes.TextPattern;
import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MainController {

    private TextPattern textPattern;

    @FXML
    private Label resultLabel;

    @FXML
    private TextField textField;

    @FXML
    private TextField patternField;

    @FXML
    protected void onSearchButtonClick() {
        resultLabel.setText(AhoCorasick.result(textField.getText(), patternField.getText().split(",")));
    }

    @FXML
    protected void onLoadButtonClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            Gson gson = new Gson();
            try (FileReader reader = new FileReader(selectedFile)) {
                textPattern = gson.fromJson(reader, TextPattern.class);
                String temp = "";
                String[] patterns = textPattern.getPattern();
                for (int i = 0; i < patterns.length; i++) {
                    temp = temp + patterns[i] + "\n";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        textField.setText(textPattern.getText());
        patternField.setText(String.join(",", textPattern.getPattern()));
        resultLabel.setText(AhoCorasick.result(textPattern.getText(), textPattern.getPattern()));
    }
}
