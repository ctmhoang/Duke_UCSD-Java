import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EfficientMarkovWord implements IMarkovModel
{
    private String[] myText;
    private Random myRandom;
    private int myOrder;
    private Map<WordGram, List<String>> follows;

    public EfficientMarkovWord(int order)
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
        follows = new HashMap<>();
        buildMap();
    }

    public String getRandomText(int numWords)
    {
        StringBuilder sb = new StringBuilder();
        int index = myRandom.nextInt(myText.length - myOrder);  // random word to start with
        WordGram key = new WordGram(myText, index, myOrder);
        sb.append(key.toString());
        sb.append(" ");
        for (int k = 0; k < numWords - myOrder; k++)
        {
            ArrayList<String> follows = getFollows(key);

//            System.out.println(key);
//            System.out.println(follows);

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
        return (ArrayList<String>) follows.get(kGram);
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

    private void buildMap()
    {
        for (int i = 0; i <= myText.length - myOrder; i++)
        {
            WordGram tmp = new WordGram(myText, i, myOrder);
            if(i + myOrder == myText.length)
            {
                follows.putIfAbsent(tmp, new ArrayList<>());
                continue;
            }
            if(follows.containsKey(tmp)) follows.get(tmp).add(myText[i+myOrder]);
            else follows.put(tmp,new ArrayList<String>(Arrays.asList(myText[i+myOrder])));
        }
        printHashMapInfo();
    }

    private void printHashMapInfo()
    {
        if (follows.size() < 11)
            follows.forEach((key, value) -> System.out.println(key + " - " + value));

        System.out.println("Number of keys: " + follows.size());
        int maxSize = Collections.max(follows.entrySet(), Comparator.comparingInt(entry -> entry.getValue().size())).getValue().size();

        System.out.println("The largest value is " + maxSize);
        System.out.println("Keys has maximum value are " + Arrays.toString(follows.entrySet().stream().filter(entry -> entry.getValue().size() == maxSize).toArray()));
    }
}
