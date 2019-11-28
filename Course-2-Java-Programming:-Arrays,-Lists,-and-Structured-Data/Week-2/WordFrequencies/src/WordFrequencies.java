import edu.duke.FileResource;
import java.util.ArrayList;
import java.util.Arrays;

public class WordFrequencies
{
    private ArrayList<String> myWords;
    private ArrayList<Integer> myFreqs;
    public WordFrequencies(){
        myWords = new ArrayList<String>();
        myFreqs = new ArrayList<Integer>();
    }
    public void findUnique(){
        myWords.clear();
        myFreqs.clear();
        FileResource resource = new FileResource();
        for (String word : resource.words()){
            String lowercase = word.toLowerCase();
            if(myWords.contains(lowercase)){
                int idx = myWords.indexOf(lowercase);
                myFreqs.set(idx, myFreqs.get(idx) + 1);
            }else {
                myWords.add(lowercase);
                myFreqs.add(1);
            }
        }
    }

    public int findIndexOfMax(){
        Integer[] freqs = myFreqs.toArray(new Integer[0]);
        Arrays.sort(freqs);
        return freqs[freqs.length - 1];
    }

    public void tester(){
        findUnique();
        System.out.println("Unique words: "+ myWords.size());
        System.out.println("The word that occurs most often and its count are: " + findIndexOfMax());
        for(int i = 0 ; i < myWords.size(); i ++){
            System.out.println(myFreqs.get(i) + "\t" + myWords.get(i));
        }
    }
}
