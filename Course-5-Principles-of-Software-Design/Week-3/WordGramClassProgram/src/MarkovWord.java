import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

public class MarkovWord implements IMarkovModel
{
    private String[] myText;
    private Random myRandom;
    private int myOrder;

    public MarkovWord(int order)
    {
        myRandom = new Random();
        myOrder = order;
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
        int index = myRandom.nextInt(myText.length - myOrder);  // random word to start with
        WordGram key = new WordGram(myText,index,myOrder);
        sb.append(key.toString());
        sb.append(" ");
        for (int k = 0; k < numWords - myOrder; k++)
        {
            ArrayList<String> follows = getFollows(key);

            System.out.println(key);
            System.out.println(follows);

            if (follows.size() == 0)
            {
                break;
            }
            index = myRandom.nextInt(follows.size());
            String next = follows.get(index);
            sb.append(next);
            sb.append(" ");
            key = key.shiftAdd(next);
        }

        return sb.toString().trim();
    }

    private ArrayList<String> getFollows(WordGram kGram)
    {
        ArrayList<String> follows = new ArrayList<String>();
        int pos = 0;
        while (true)
        {
            pos = indexOf(myText, kGram, pos);
            if (pos >= myText.length - myOrder || pos == -1) break;
            follows.add(myText[pos+= myOrder]);
        }
        return follows;
    }

    private int indexOf(String[] words, WordGram target, int start)
    {
        return IntStream.range(start, words.length - myOrder)
                .filter(
                        i -> IntStream.range(0, myOrder)
                                .allMatch(j -> target.wordAt(j)
                                        .equals(words[i + j]))
                ).findFirst().orElse(-1);
    }
}
