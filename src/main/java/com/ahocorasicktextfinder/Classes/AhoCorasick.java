package com.ahocorasicktextfinder.Classes;

import java.util.*;

public class AhoCorasick {
    private static final int ALPHABET_SIZE = 128; // Size of ASCII table

    static class TrieNode {
        TrieNode[] children = new TrieNode[ALPHABET_SIZE];
        TrieNode failure;
        List<Integer> output = new ArrayList<>();
    }

    private final TrieNode root;

    public AhoCorasick() {
        root = new TrieNode();
    }

    // Convert character to index (using ASCII values directly)
    private int charToIndex(char c) {
        return (int) c;
    }

    // Add pattern to the trie
    public void addPattern(String pattern, int index) {
        TrieNode current = root;
        for (char c : pattern.toCharArray()) {
            int idx = charToIndex(c);
            if (current.children[idx] == null) {
                current.children[idx] = new TrieNode();
            }
            current = current.children[idx];
        }
        current.output.add(index);
    }

    // Build failure links
    public void buildFailureLinks() {
        Queue<TrieNode> queue = new LinkedList<>();
        for (int i = 0; i < ALPHABET_SIZE; i++) {
            if (root.children[i] != null) {
                root.children[i].failure = root;
                queue.add(root.children[i]);
            } else {
                root.children[i] = root;
            }
        }

        while (!queue.isEmpty()) {
            TrieNode current = queue.remove();
            for (int i = 0; i < ALPHABET_SIZE; i++) {
                if (current.children[i] != null) {
                    TrieNode failure = current.failure;
                    while (failure.children[i] == null) {
                        failure = failure.failure;
                    }
                    current.children[i].failure = failure.children[i];
                    current.children[i].output.addAll(failure.children[i].output);
                    queue.add(current.children[i]);
                }
            }
        }
    }

    // Search patterns in text and return a map with pattern count and their indices
    public Map<String, List<Integer>> search(String text, String[] patterns) {
        Map<String, List<Integer>> patternOccurrences = new HashMap<>();
        for (String pattern : patterns) {
            patternOccurrences.put(pattern, new ArrayList<>());
        }

        TrieNode current = root;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            int idx = charToIndex(c);
            while (current.children[idx] == null) {
                current = current.failure;
            }
            current = current.children[idx];
            for (int patternIndex : current.output) {
                String pattern = patterns[patternIndex];
                patternOccurrences.get(pattern).add(i - pattern.length() + 1);
            }
        }

        return patternOccurrences;
    }

    public static void main(String[] args) {
        String text = "ab.c ab.ab.c";
        String[] patterns = {"ab", "ab.", "bc", ".c"};

        AhoCorasick ac = new AhoCorasick();
        for (int i = 0; i < patterns.length; i++) {
            ac.addPattern(patterns[i], i);
        }

        ac.buildFailureLinks();
        Map<String, List<Integer>> result = ac.search(text, patterns);

        for (String pattern : patterns) {
            List<Integer> occurrences = result.get(pattern);
            System.out.println("Pattern '" + pattern + "' occurs " + occurrences.size() + " times at indices " + occurrences);
        }
    }

    public static String result(String text, String[] patterns) {
        String temp = "";
        AhoCorasick ac = new AhoCorasick();
        for (int i = 0; i < patterns.length; i++) {
            ac.addPattern(patterns[i], i);
        }

        ac.buildFailureLinks();
        Map<String, List<Integer>> result = ac.search(text, patterns);

        for (String pattern : patterns) {
            List<Integer> occurrences = result.get(pattern);
            temp += ("Pattern '" + pattern + "' occurs " + occurrences.size() + " times at indices " + occurrences + "\n");
        }
        return temp;
    }
}

//    public static String result(String text, String[] patterns) {
//        String temp = "";
//        AhoCorasick ac = new AhoCorasick();
//        for (int i = 0; i < patterns.length; i++) {
//            ac.addPattern(patterns[i], i);
//        }
//
//        ac.buildFailureLinks();
//        Map<String, List<Integer>> result = ac.search(text, patterns);
//
//        for (String pattern : patterns) {
//            List<Integer> occurrences = result.get(pattern);
//            temp += ("Pattern '" + pattern + "' occurs " + occurrences.size() + " times at indices " + occurrences + "\n");
//        }
//        return temp;
//    }