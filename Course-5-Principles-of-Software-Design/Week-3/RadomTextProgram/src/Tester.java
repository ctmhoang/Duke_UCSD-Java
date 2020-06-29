import edu.duke.FileResource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Tester
{
    public static void main(String[] args)
    {
//        testGetFollows();
        testGetFollowsWithFile();
    }

    public static void testGetFollows()
    {
        MarkovOne tmp = new MarkovOne();
        tmp.setTraining("this is a test yes this is a test.");

        List<String> checkT = Arrays.asList("h", "e", " ", "h", "e", ".");
        List<String> checkE = Arrays.asList("s", "s", "s");
        List<String> checkES = Arrays.asList("t", " ", "t");
        List<String> checkDot = new ArrayList<>();
        List<String> checkTDot = new ArrayList<>();

        if (!tmp.getFollows("t").equals(checkT))
            System.err.println("DO NOT WORKING PROPERLY IN 't' case");
        if (!tmp.getFollows("e").equals(checkE))
            System.err.println("DO NOT WORKING PROPERLY IN 'e' case");
        if (!tmp.getFollows("es").equals(checkES))
            System.err.println("DO NOT WORKING PROPERLY IN 'es' case");
        if (!tmp.getFollows(".").equals(checkDot))
            System.err.println("DO NOT WORKING PROPERLY IN '.' case");
        if (!tmp.getFollows("t.").equals(checkTDot))
            System.err.println("DO NOT WORKING PROPERLY IN 't.' case");

        System.out.println("Test done!");
    }

    public static void testGetFollowsWithFile()
    {
        FileResource fr = new FileResource();
        String st = fr.asString();
        st = st.replace('\n', ' ');
        //load confucius
        MarkovOne tmp = new MarkovOne();
        tmp.setTraining(st);
        if(tmp.getFollows("t").size() != 11548)
            System.err.println("DO NOT WORKING PROPERLY");

        System.out.println("Test done!");

    }
}
