package voina_i_mir;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextAnalyzer {
    private HashMap<String, Integer> wordsMap;
    private TreeMap<Integer, String> countMap;
    private int lineCounter;
    private int wordCounter;
    private int commaCounter;
    private File file;

    public TextAnalyzer(String filePath) {
        if (filePath.endsWith(".txt")) {
            File file = new File(filePath);
            if (file.exists()) {
                this.file = file;
            }
        } else {
            throw new ExceptionInInitializerError("Ops, check your file!");
        }

        wordsMap = new HashMap<>();
        countMap = new TreeMap<>((e1, e2) -> ((Integer) e2).compareTo(e1));
        lineCounter = 0;
        commaCounter = 0;
        wordCounter = 0;
        createWordMap();
        findCommon10Words();
    }

    private void createWordMap() {
        Scanner sc;
        String pattern = "[а-яА-Я]+";
        String line;
        try {
            sc = new Scanner(file);
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                this.lineCounter++;
                Pattern MY_PATTERN = Pattern.compile("[а-яА-Яa-zA-Z]+|,");
                Matcher m = MY_PATTERN.matcher(line);
                while (m.find()) {
                    String word = m.group().toLowerCase();
                    if (word.equals(",")) {
                        this.commaCounter++;
                        continue;
                    }
                    if (!wordsMap.containsKey(word)) {
                        wordsMap.put(word, 0);
                    }
                    wordsMap.put(word, wordsMap.get(word) + 1);
                    this.wordCounter++;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void printLinesCount() {
        System.out.println("Count of lines: " + this.lineCounter);
    }

    public void printWordCount() {
        System.out.println("Count of words: " + this.wordCounter);
    }

    public void printCommaCount() {
        System.out.println("Count of commas: " + this.commaCounter);
    }

    private void findCommon10Words() {
        Iterator it = this.wordsMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            this.countMap.put((int) pair.getValue(), (String) pair.getKey());
        }
    }

    public void printCommon10Words() {
        int counter = 0;
        Iterator it = this.countMap.entrySet().iterator();
        System.out.println("Most common 10 words:");
        while (it.hasNext() && counter <= 10) {
            counter++;
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println("\"" + pair.getValue() + "\"" + " found " + pair.getKey() + " times");
        }
    }

    public void printVoinaIliMir() {
        int war = 0;
        int peace = 0;
        String[] warWords = {"война", "войната", "воините", "воини"};
        String[] peaceWords = {"мир", "мирът"};
        for (int i = 0; i < warWords.length; i++) {
            try {
                war += wordsMap.get(warWords[i]);
            } catch (Exception e) {

            }
        }
        for (int i = 0; i < peaceWords.length; i++) {
            try {
                peace += wordsMap.get(peaceWords[i]);
            } catch (Exception e) {

            }
        }
        System.out.println("Война " + war + " : " + peace + " Мир");


    }

    public void saveWordsInFiles() {
        TreeMap<Integer, TreeSet<String>> wordsByLetters = new TreeMap<>();
        Iterator it = this.wordsMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> pair = (Map.Entry) it.next();
            if (!wordsByLetters.containsKey(pair.getKey().length())) {
                wordsByLetters.put(pair.getKey().length(), new TreeSet<>());
            }
            wordsByLetters.get(pair.getKey().length()).add(pair.getKey());
        }

        File dir = new File("words");
        dir.mkdir();
        for (int i = 1; i < wordsByLetters.size()+1; i++) {
            File wordsFile = new File("words/" + i + "_letter_words.txt");
            try {
                PrintStream ps = new PrintStream(wordsFile);
                for (String word : wordsByLetters.get(i)) {
                    ps.println(word);
                }

            } catch (FileNotFoundException e) {
                System.out.println("Cannot write file!" + e.fillInStackTrace());
            }
        }
        System.out.println(this.wordsMap.size() + " unique words were saved in " + wordsByLetters.size() + " files");
    }
}
