
/**
 * Write a description of class MarkovRunner here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import edu.duke.*;

public class MarkovRunner
{
    public static void main(String[] args)
    {
//        runMarkov();
//        testHashMap();
        compareMethods();
    }
    public static void runModel(IMarkovModel markov, String text, int size)
    {
        markov.setTraining(text);
        System.out.println("running with " + markov);
        for (int k = 0; k < 3; k++)
        {
            String st = markov.getRandomText(size);
            printOut(st);
        }
    }

    public static void runModel(IMarkovModel markov, String text, int size, int seed)
    {
        markov.setTraining(text);
        markov.setRandom(seed);
        System.out.println("running with " + markov);
        for (int k = 0; k < 3; k++)
        {
            String st = markov.getRandomText(size);
            printOut(st);
        }
    }

    public static void runMarkov()
    {
        FileResource fr = new FileResource();
        String st = fr.asString();
        st = st.replace('\n', ' ');
        //MarkovWordOne markovWord = new MarkovWordOne(); 
        //runModel(markovWord, st, 200);

        MarkovWord tmp = new MarkovWord(3);
        tmp.setTraining(st);
        tmp.setRandom(643);
        printOut(tmp.getRandomText(100));
    }

    private static void printOut(String s)
    {
        String[] words = s.split("\\s+");
        int psize = 0;
        System.out.println("----------------------------------");
        for (int k = 0; k < words.length; k++)
        {
            System.out.print(words[k] + " ");
            psize += words[k].length() + 1;
            if (psize > 60)
            {
                System.out.println();
                psize = 0;
            }
        }
        System.out.println("\n----------------------------------");
    }

    public static void testHashMap()
    {
        EfficientMarkovWord markovWord = new EfficientMarkovWord(2);
        markovWord.setRandom(42);
        markovWord.setTraining("this is a test yes this is really a test");
        System.out.println(markovWord.getRandomText(50));

        markovWord.setTraining("this is a test yes this is really a test yes a test this is wow");
    }

    public static void compareMethods()
    {
        FileResource fr = new FileResource();
        String st = fr.asString();
        st = st.replace('\n', ' ');
        int size = 100;
        int seed = 42;

        IMarkovModel tmp;
        long str = System.nanoTime();
        tmp = new EfficientMarkovWord(2);
        runModel(tmp, st, size, seed);
        long end = System.nanoTime();

        long eff =  end - str;

        str = System.nanoTime();
        tmp = new MarkovWord(2);
        runModel(tmp, st, size, seed);
        end = System.nanoTime();

        long stock = end-str;

        System.out.println("faster than: " + stock/eff);

    }
}
