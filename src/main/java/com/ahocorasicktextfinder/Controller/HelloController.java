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

    @FXML
    private Label welcomeText;

    @FXML
    private TextField debugging;

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
                TextPattern textPattern = gson.fromJson(reader, TextPattern.class);
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
    }

    static int MAXS = 500;

    static int MAXC = 26;

    static int []out = new int[MAXS];

    static int []f = new int[MAXS];

    static int [][]g = new int[MAXS][MAXC];

    static int buildMatchingMachine(String arr[], int k)
    {

        Arrays.fill(out, 0);

        for(int i = 0; i < MAXS; i++)
            Arrays.fill(g[i], -1);

        int states = 1;

        for(int i = 0; i < k; ++i)
        {
            String word = arr[i];
            int currentState = 0;

            for(int j = 0; j < word.length(); ++j)
            {
                int ch = word.charAt(j) - 'a';

                if (g[currentState][ch] == -1)
                    g[currentState][ch] = states++;

                currentState = g[currentState][ch];
            }

            out[currentState] |= (1 << i);
        }

        for(int ch = 0; ch < MAXC; ++ch)
            if (g[0][ch] == -1)
                g[0][ch] = 0;

        Arrays.fill(f, -1);

        Queue<Integer> q = new LinkedList<>();

        for(int ch = 0; ch < MAXC; ++ch)
        {

            if (g[0][ch] != 0)
            {
                f[g[0][ch]] = 0;
                q.add(g[0][ch]);
            }
        }

        while (!q.isEmpty())
        {

            int state = q.peek();
            q.remove();

            for(int ch = 0; ch < MAXC; ++ch)
            {

                if (g[state][ch] != -1)
                {

                    int failure = f[state];

                    while (g[failure][ch] == -1)
                        failure = f[failure];

                    failure = g[failure][ch];
                    f[g[state][ch]] = failure;

                    out[g[state][ch]] |= out[failure];

                    q.add(g[state][ch]);
                }
            }
        }
        return states;
    }

    static int findNextState(int currentState, char nextInput)
    {
        int answer = currentState;
        int ch = nextInput - 'a';

        while (g[answer][ch] == -1)
            answer = f[answer];

        return g[answer][ch];
    }

    static String searchWords(String arr[], int k,
                              String text)
    {
        String result = "";
        buildMatchingMachine(arr, k);

        int currentState = 0;

        for(int i = 0; i < text.length(); ++i)
        {
            currentState = findNextState(currentState,
                    text.charAt(i));

            if (out[currentState] == 0)
                continue;

            for(int j = 0; j < k; ++j)
            {
                if ((out[currentState] & (1 << j)) > 0)
                {
                    result += ("Word " + arr[j] +
                            " appears from " +
                            (i - arr[j].length() + 1) +
                            " to " + i + "\n");
                }
            }
        }
        return result;
    }
}
