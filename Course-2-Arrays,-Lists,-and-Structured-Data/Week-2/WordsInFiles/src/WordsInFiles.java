import edu.duke.DirectoryResource;
import edu.duke.FileResource;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class WordsInFiles {
    private HashMap<String, ArrayList<String>> wordToFiles;

    public WordsInFiles() {
        wordToFiles = new HashMap<>();
    }

    private void addWordsFromFile(File f) {
        FileResource resource = new FileResource(f);
        String fileName = f.getName();
        for (String word : resource.words()) {
            if (!wordToFiles.containsKey(word)) {
                wordToFiles.put(word, new ArrayList<>(Collections.singletonList(fileName)));
            } else {
                if (!wordToFiles.get(word).contains(fileName)) {
                    wordToFiles.get(word).add(f.getName());
                }
            }
        }
    }

    public void buildWordFileMap() {
        wordToFiles.clear();
        DirectoryResource directoryResource = new DirectoryResource();
        for (File resource : directoryResource.selectedFiles()) {
            addWordsFromFile(resource);
        }
    }

    public int maxNumber() {
        return Collections.max(wordToFiles.entrySet(), Comparator.comparingInt(entry -> entry.getValue().size())).getValue().size();
    }

    public ArrayList<String> wordsInNumFiles(int number) {
        return wordToFiles.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue().size(), number))
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void printFileIn(String word) {
        for (String fileName : wordToFiles.get(word)) {
            System.out.println(fileName);
        }
    }

    public boolean isContainWord(String word){
        return wordToFiles.keySet().contains(word);
    }

    public void tester() {
        buildWordFileMap();
        for (String word : wordToFiles.keySet()) {
            System.out.println("the maximum number of " + word + " in is " + wordToFiles.get(word).size());
        }
        System.out.println();
        System.out.println("The maximum number of files any word is in is " + maxNumber());
        System.out.println("Shown below: ");
        ArrayList<String> maxWords = wordsInNumFiles(maxNumber());
        for (String word : maxWords) {
            System.out.println("Name: \t" + word);
            printFileIn(word);
        }
        System.out.println("Number of words: " + maxWords.size());
        String word = "red";
        System.out.println("Contain: " + word + " " + isContainWord(word));
    }
}
