import java.util.*;

public class EfficientMarkovModel extends AbstractMarkovModel
{
    private final int model;
    private Map <String, List<String>> follows;

    public EfficientMarkovModel(int m)
    {
        myRandom = new Random();
        follows = new HashMap<>();
        model = m;
    }

    @Override
    public void setTraining(String s)
    {
        super.setTraining(s);
        buildMap();
    }

    private void buildMap()
    {
    //Travel through text is more Efficient to call super method 0(n) vs 0(n**2)
    // Faster than regex getFollows 65 times
    // Faster than travel geFollows 6 times 
    // Oop~
        for (int i = 0, k = myText.length() - model; i <= k; i++)
        {
            String key = myText.substring(i, i + model);

            if (i + model == myText.length())
            {
                follows.putIfAbsent(key, new ArrayList<>());
                continue;
            }
            if(follows.containsKey(key)) follows.get(key).add(myText.substring(i + model, i + model + 1));
            else follows.put(key,new ArrayList<>(Arrays.asList(myText.substring(i + model, i + model + 1))));

        }
        printHashMapInfo();
    }
    }

    @Override
    protected ArrayList<String> getFollows(String key)
    {
        return new ArrayList<>(follows.get(key));
    }

    public String getRandomText(int numChars)
    {
        if (myText == null) return "";
        int idx = myRandom.nextInt(myText.length() - model);
        String key = myText.substring(idx, idx + model);
        StringBuilder sb = new StringBuilder(key);
        for (int k = 0; k < numChars; k++)
        {
            ArrayList<String> follows = getFollows(key);
            if(follows.size() == 0) break;
            String followChar = follows.get(myRandom.nextInt(follows.size()));
            key = key.substring(1) + followChar;
            sb.append(followChar);
        }

        return sb.toString();
    }

    @Override
    public String toString()
    {
        return "Efficient MarkovModel of order " + model;
    }

    private void printHashMapInfo()
    {
        if(follows.size() > 11)
            follows.forEach((key, value) -> System.out.println(key + " - " + value));

        System.out.println("Number of keys: " + follows.size());
        int maxSize =  Collections.max(follows.entrySet(), Comparator.comparingInt(entry -> entry.getValue().size())).getValue().size();

        System.out.println("The largest value is " + maxSize);
        System.out.println("Keys has maximum value are " + Arrays.toString(follows.entrySet().stream().filter(entry -> entry.getValue().size() == maxSize).toArray()));

    }
}
