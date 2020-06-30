import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarkovFour implements IMarkovModel
{
    private String myText;
    private Random myRandom;

    public MarkovFour()
    {
        myRandom = new Random();
    }

    public void setRandom(int seed)
    {
        myRandom = new Random(seed);
    }

    public void setTraining(String s)
    {
        myText = s.trim();
    }

    public String getRandomText(int numChars)
    {
        if (myText == null) return "";
        int idx = myRandom.nextInt(myText.length() - 4);
        String key = myText.substring(idx, idx + 4);
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

    public ArrayList<String> getFollows(String key)
    {
        ArrayList<String> res = new ArrayList<>();
        //reformat key with all quantifiers, character classes and any single character TIHI
        key = key.replaceAll("\\.", "\\\\.").replaceAll("\\[","\\\\[").replaceAll("\\?", "\\\\?")
                .replaceAll("\\*","\\\\*").replaceAll("\\(","\\\\(").replaceAll("\\)","\\\\)");
        Pattern pattern = Pattern.compile(key);
        Matcher m = pattern.matcher(myText);
        while (m.find())
        {
            int followIdx = m.end();
            if (followIdx > myText.length() - 1) return res;
            res.add(myText.substring(followIdx, followIdx + 1));
        }
        return res;
    }
}
