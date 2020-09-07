import edu.duke.FileResource;

public class Main {

    public static void main(String[] args) {
        // write your code here
        testCountWordLengths();
        //I used base 0 to count but assigment used base 1
    }

    public static void countWordLengths(FileResource resource, int[] counts) {
        for (String word : resource.words()) {
            if(word.length() > 1) {
                boolean lastLetter = Character.isLetter(word.charAt(word.length() - 1));
                boolean firstLetter = Character.isLetter(word.charAt(0));
                if (firstLetter && lastLetter) {
                    if (word.length() - 1  > counts.length - 1) {
                        counts[counts.length - 1]++;
                    } else {
                        counts[word.length() - 1]++;
                    }
                } else if (firstLetter || lastLetter) {
                    if (word.length() -2  > counts.length -1 ) {
                        counts[counts.length - 1]++;
                    } else {
                        counts[word.length() - 2]++;
                    }
                } else if (Character.isLetter(word.charAt(1)) || Character.isLetter(word.charAt(word.length() - 2))) {
                    if (word.length() - 3 > counts.length - 1) {
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
    public static void testCountWordLengths(){
        FileResource resource = new FileResource();
        int[] counts = new int[31];
        countWordLengths(resource, counts);
        for(int i : counts){
            System.out.println(i);
        }
        System.out.println(indexOfMax(counts));
    }

    public static int indexOfMax(int[] values){
        int max = 0, idx = 0 ;
        for(int i = 0 ; i < values.length ; i++){
            if(max < values[i]){
                max = values[i];
                idx = i;
            }
        }
        return idx;
    }
}
