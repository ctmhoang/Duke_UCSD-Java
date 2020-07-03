import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

public class MarkovWordTwo implements IMarkovModel
{
    private String[] myText;
    private Random myRandom;

    public MarkovWordTwo()
    {
        myRandom = new Random();
    }

    public void setRandom(int seed)
    {
        myRandom = new Random(seed);
    }

    public void setTraining(String text)
    {
        myText = text.split("\\s+");
    }

    public String getRandomText(int numWords)
    {
        StringBuilder sb = new StringBuilder();
        int index = myRandom.nextInt(myText.length - 2);  // random word to start with
        String key = myText[index];
        String key1 = myText[index + 1];
        sb.append(key);
        sb.append(" ");
        sb.append(key1);
        sb.append(" ");
        for (int k = 0; k < numWords - 1; k++)
        {
            ArrayList<String> follows = getFollows(key, key1);

//            System.out.println(key + " " + key1);
//            System.out.println(follows);

            if (follows.size() == 0)
                break;
            index = myRandom.nextInt(follows.size());
            String next = follows.get(index);
            sb.append(next);
            sb.append(" ");
            key = key1;
            key1 = next;
        }

        return sb.toString().trim();
    }

    private ArrayList<String> getFollows(String... key)
    {
        ArrayList<String> follows = new ArrayList<String>();
        int pos = 0;
        while (true)
        {
            pos = indexOf(myText, key[0], key[1], pos);
            if (pos >= myText.length - key.length || pos == -1) break; //look up 3 words ahead
            follows.add(myText[pos+=key.length]);
        }
        return follows;
    }

    private int indexOf(String[] words, String target1, String target2, int start)
    {
        return IntStream.range(start, words.length-1).filter(i -> words[i].equals(target1) && words[i+1].equals(target2)).findFirst().orElse(-1);
    }

}
