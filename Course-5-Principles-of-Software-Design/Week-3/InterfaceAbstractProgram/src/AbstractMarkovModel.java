
/**
 * Abstract class AbstractMarkovModel - write a description of the class here
 * 
 * @author (your name here)
 * @version (version number or date here)
 */

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractMarkovModel implements IMarkovModel {
    protected String myText;
    protected Random myRandom;
    
    public AbstractMarkovModel() {
        myRandom = new Random();
    }

    public void setRandom(int seed)
    {
        myRandom = new Random(seed);
    }

    public void setTraining(String s) {
        myText = s.trim();
    }
 
    abstract public String getRandomText(int numChars);

    protected ArrayList<String> getFollows(String key)
    {
        ArrayList<String> res = new ArrayList<>();
        //reformat key with all quantifiers, character classes and any single character
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
