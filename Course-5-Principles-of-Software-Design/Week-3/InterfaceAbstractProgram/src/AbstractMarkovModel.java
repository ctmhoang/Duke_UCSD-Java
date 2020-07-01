
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

    //This way to find follows is much more slower (about 10 times) than travel through text to find it occurences
    //I think it create new interable for each find() called. 
    protected ArrayList<String> getFollows(String key)
    {
        ArrayList<String> res = new ArrayList<>();
        //reformat key with all quantifiers, character classes and any single character
        key = key.replaceAll("\\.", "\\\\.").replaceAll("\\[", "\\\\[").replaceAll("\\?", "\\\\?")
                .replaceAll("\\*", "\\\\*").replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)");
        Pattern pattern = Pattern.compile(key);
        Matcher m = pattern.matcher(myText);
        int i = 0;
        while (m.find(i))
        {
            int followIdx = m.end();
            if (followIdx > myText.length() - 1) return res;
            res.add(myText.substring(followIdx, followIdx + 1));
            i = m.start() + 1;
        }
        return res;
    }
    
    //This way is much more efficent but I wrote the above method first and I think it's neat to use regex
    //So I comment this out
//     protected ArrayList<String> getFollows(String key)
//     {
//         ArrayList<String> follows = new ArrayList<>();
//         int pos = 0;
//         while (pos < myText.length())
//         {
//             int start = myText.indexOf(key, pos);
//             if (start == -1 || start + key.length() >= myText.length())
//                 break;
// 
//             String next = myText.substring(start + key.length(), start + key.length() + 1);
//             follows.add(next);
//             pos = start + 1;
//         }
//         return follows;
//     }
}
