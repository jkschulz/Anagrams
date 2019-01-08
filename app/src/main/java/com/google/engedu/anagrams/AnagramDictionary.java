/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private List<String> wordList = new ArrayList<>();
    private Set<String> wordSet = new HashSet<>();
    private Map<String, List<String>> lettersToWord = new HashMap<>();
    private Map<Integer, List<String>> sizeToWords = new HashMap<>();
    private int wordLength = DEFAULT_WORD_LENGTH;
    private static final String TAG = "AnagramDictionary";

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            String sortedWord = sortLetters(word);
            int lengthOfWord = word.length();
            if (!sizeToWords.containsKey(lengthOfWord)) {
                sizeToWords.put(lengthOfWord, new ArrayList<String>());
            }
            sizeToWords.get(lengthOfWord).add(word);
            if (!lettersToWord.containsKey(sortedWord)) {
                // Don't do this!
                // List<String> vals = lettersToWord.get(sortedWord);
                // vals.add(word);
                // Unless you do:
                // vals.put(sortedWord, vals);
                lettersToWord.put(sortedWord, new ArrayList<String>());
            }
            lettersToWord.get(sortedWord).add(word);
        }
        Log.i(TAG, "wordlist size: " + wordList.size());
        Log.i(TAG, "map for (opst) = " + lettersToWord.get("opst"));
        Log.i(TAG, "iGW('nonstop')=" + isGoodWord("nonstop", "post"));
    }

    // Checks if word in wordSet and that 'base' is not a substring of 'word'
    public boolean isGoodWord(String word, String base) {
        return wordSet.contains(word) && !word.toLowerCase().contains(base.toLowerCase());
    }

    // Returns all anagrams of 'targetWord' in dictionary.
    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<>();
        String targetWordSorted = sortLetters(targetWord);
        for (String word : wordList) {
            String wordSorted = sortLetters(word);
            if (wordSorted.equals(targetWordSorted)) {
                result.add(word);
            }
        }
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        List<String> result = new ArrayList<String>();
        for (char c = 'a'; c <= 'z'; c++) {
            Log.i(TAG, ""+c);
            String newWord = word + c;
            result.addAll(getAnagrams(newWord));
        }
        return result;
    }

    public String pickGoodStarterWord() {
        // Part 1
//        Log.i(TAG, "getAnagrams(dog) = " + getAnagrams("dog"));
//        // Select random word from dictionary
//        Random rand = new Random();
//        int index = rand.nextInt(wordList.size());
//        // Iterate until word has at least MIN_NUM_ANAGRAMS anagrams
//        int numAnagrams = 0;
//        while (numAnagrams < MIN_NUM_ANAGRAMS) {
//            index++;
//            if (index == wordList.size()) {
//                index = 0;
//            }
//            numAnagrams = getAnagramsWithOneMoreLetter(wordList.get(index)).size();
//        }
//        return wordList.get(index);

        // Part 2
        Random rand = new Random();
        List<String> wordsOfLength = sizeToWords.get(wordLength);
        int index = rand.nextInt(wordsOfLength.size());
        // Iterate until word has at least MIN_NUM_ANAGRAMS anagrams
        int numAnagrams = 0;
        while (numAnagrams < MIN_NUM_ANAGRAMS) {
            index++;
            if (index == wordsOfLength.size()) {
                index = 0;
            }
            numAnagrams = getAnagramsWithOneMoreLetter(wordsOfLength.get(index)).size();
        }
        wordLength++;
        return wordsOfLength.get(index);

    }

    // Returns a String with the letters of 'word' in alphabetical order (e.g. given "post", returns "opst").
    private String sortLetters(String word) {
        char[] wordLetters = word.toCharArray();
        Arrays.sort(wordLetters);
        return new String(wordLetters);
    }

}
