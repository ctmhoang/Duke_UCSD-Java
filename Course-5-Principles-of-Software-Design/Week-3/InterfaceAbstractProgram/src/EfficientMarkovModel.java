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
        for(int i = 0, k = myText.length() - model; i <= k ; i ++)
        {
            String key = myText.substring(i, i + model);
            if(follows.containsKey(key)) continue;
            follows.put(key,super.getFollows(key));
        }
//        printHashMapInfo();
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
