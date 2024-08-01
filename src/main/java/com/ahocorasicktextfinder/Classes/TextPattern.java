package com.ahocorasicktextfinder.Classes;

public class TextPattern {
    private String text;
    private String[] patterns;

    public TextPattern(String text, String[] patterns) {
        this.text = text;
        this.patterns = patterns;
    }

    public String getText() {
        return text;
    }

    public String[] getPattern() {
        return patterns;
    }


}