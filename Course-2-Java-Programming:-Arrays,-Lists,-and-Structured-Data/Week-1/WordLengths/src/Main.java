import edu.duke.FileResource;

public class Main {

    public static void main(String[] args) {
        // write your code here

    }

    public static void countWordLengths(FileResource resource, int[] counts) {
        for (String word : resource.words()) {
            if(word.length() > 1) {
                boolean lastLetter = Character.isLetter(word.charAt(word.length() - 1));
                boolean firstLetter = Character.isLetter(word.charAt(0));
                if (firstLetter && lastLetter) {
                    if (word.length() < counts.length) {
                        counts[counts.length - 1]++;
                    } else {
                        counts[word.length() - 1]++;
                    }
                } else if (firstLetter || lastLetter) {
                    if (word.length() < counts.length) {
                        counts[counts.length - 1]++;
                    } else {
                        counts[word.length() - 2]++;
                    }
                } else if (Character.isLetter(word.charAt(1)) || Character.isLetter(word.charAt(word.length() - 2))) {
                    if (word.length() < counts.length) {
                        counts[counts.length - 1]++;
                    } else {
                        counts[word.length() - 3]++;
                    }
                }
            }else {
                if(Character.isLetter(counts[0])){
                    counts[0]++;
                }
            }
        }
    }
}
