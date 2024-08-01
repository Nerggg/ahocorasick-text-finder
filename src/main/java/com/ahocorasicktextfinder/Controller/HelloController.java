package com.ahocorasicktextfinder.Controller;

import com.ahocorasicktextfinder.Classes.TextPattern;
import com.ahocorasicktextfinder.Classes.AhoCorasick;
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
import java.util.*;

public class HelloController {

    private TextPattern textPattern;

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        AhoCorasick tes = new AhoCorasick();
//        String arr[] = { "he", "she", "hers", "his" };
//        String text = "ahishers";
        String arr[] = { "saya", "ayam" };
        String text = "saya sangat suka matkul irk. saya jadi ingin makan ayam.";
//        text = text.replaceAll("[.\\s]", "");
//        int k = arr.length;
//
//        searchWords(arr, k, text);
//        welcomeText.setText("Welcome to JavaFX Application!");
//        welcomeText.setText(searchWords(arr, k, text));
        welcomeText.setText(AhoCorasick.result(text, arr));
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
//                welcomeText.setText(textPattern.getPattern());
                welcomeText.setText(temp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        welcomeText.setText(AhoCorasick.result(textPattern.getText(), textPattern.getPattern()));
    }
}
