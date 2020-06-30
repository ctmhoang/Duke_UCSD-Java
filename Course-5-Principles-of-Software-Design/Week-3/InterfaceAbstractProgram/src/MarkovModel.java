import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarkovModel extends AbstractMarkovModel
{
    private int model;

    public MarkovModel(int m)
    {
        myRandom = new Random();
        model = m;
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
        return "MarkovModel of order " + model;
    }
}
