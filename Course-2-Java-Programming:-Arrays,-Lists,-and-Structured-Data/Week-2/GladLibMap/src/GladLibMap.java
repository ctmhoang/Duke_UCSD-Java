import edu.duke.FileResource;
import edu.duke.URLResource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;


public class GladLibMap {
    private HashMap<String, ArrayList<String>> myMap;

    private ArrayList<String> usedWords;
    private HashSet<String> usedCategories;

    private Random myRandom;

    private static String dataSourceURL = "http://dukelearntoprogram.com/course3/data";
    private static String dataSourceDirectory = "data";

    public GladLibMap() {
        myMap = new HashMap<>();
        initializeFromSource(dataSourceDirectory);
        myRandom = new Random();
    }

    public GladLibMap(String source) {
        myMap = new HashMap<>();
        initializeFromSource(source);
        myRandom = new Random();
    }

    private void initializeFromSource(String source) {
        String[] labs = {"adjective", "noun", "color", "country",
                "name", "animal", "timeframe", "verb", "fruit"};
        for (String lab : labs) {
            myMap.put(lab, readIt(source + "/" + lab + ".txt"));
        }
        usedWords = new ArrayList<>();
        usedCategories = new HashSet<>();
    }

    private String randomFrom(ArrayList<String> source) {
        int index = myRandom.nextInt(source.size());
        return source.get(index);
    }

    private String getSubstitute(String label) {
        if (label.equals("number")) {
            return "" + myRandom.nextInt(50) + 5;
        }
        if (myMap.containsKey(label)) {
            usedCategories.add(label);
            return randomFrom(myMap.get(label));
        }
        return "**UNKNOWN**";
    }

    private String processWord(String w) {
        int first = w.indexOf("<");
        int last = w.indexOf(">", first);
        if (first == -1 || last == -1) {
            return w;
        }
        String prefix = w.substring(0, first);
        String suffix = w.substring(last + 1);
        String sub = getSubstitute(w.substring(first + 1, last));
        while (usedWords.contains(sub)) {
            sub = getSubstitute(w.substring(first + 1, last));
        }
        usedWords.add(sub);
        return prefix + sub + suffix;
    }

    private void printOut(String s, int lineWidth) {
        int charsWritten = 0;
        for (String w : s.split("\\s+")) {
            if (charsWritten + w.length() > lineWidth) {
                System.out.println();
                charsWritten = 0;
            }
            System.out.print(w + " ");
            charsWritten += w.length() + 1;
        }
    }

    private String fromTemplate(String source) {
        StringBuilder story = new StringBuilder();
        if (source.startsWith("http")) {
            URLResource resource = new URLResource(source);
            for (String word : resource.words()) {
                story.append(processWord(word)).append(" ");
            }
        } else {
            FileResource resource = new FileResource(source);
            for (String word : resource.words()) {
                story.append(processWord(word)).append(" ");
            }
        }
        return story.toString();
    }

    private ArrayList<String> readIt(String source) {
        ArrayList<String> list = new ArrayList<String>();
        if (source.startsWith("http")) {
            URLResource resource = new URLResource(source);
            for (String line : resource.lines()) {
                list.add(line);
            }
        } else {
            FileResource resource = new FileResource(source);
            for (String line : resource.lines()) {
                list.add(line);
            }
        }
        return list;
    }

    public int totalWordsInMap() {
        int tot = 0;
        for (String lab : myMap.keySet()) {
            tot += myMap.get(lab).size();
        }
        return tot;
    }

    public int totalWordsConsidered() {
        int tot = 0;
        for (String lab : usedCategories) {
            tot += myMap.get(lab).size();
        }
        return tot;
    }

    public void makeStory() {
        usedWords.clear();
        System.out.println("\n");
        String story = fromTemplate("data/madtemplate2.txt");
        printOut(story, 60);
        System.out.println("\n\n Number of words that were replaced: " + usedWords.size());
        System.out.println(" total number of words that were possible to pick from: " + totalWordsInMap());
        System.out.println(" total number of words were considered: " + totalWordsConsidered());
    }
}
