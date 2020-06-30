
/**
 * Write a description of class MarkovRunner here.
 *
 * @author Duke Software
 * @version 1.0
 */

import edu.duke.*;

public class MarkovRunnerWithInterface
{
    public static void main(String[] args)
    {
//		runMarkov();
//		runMarkov(3);
//        testHashMap();
//        compareMethods();
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
        int size = 200;

        MarkovZero mz = new MarkovZero();
        runModel(mz, st, size);

        MarkovOne mOne = new MarkovOne();
        runModel(mOne, st, size);

        MarkovModel mThree = new MarkovModel(3);
        runModel(mThree, st, size);

        MarkovFour mFour = new MarkovFour();
        runModel(mFour, st, size);

    }

    public static void runMarkov(int seed)
    {
        FileResource fr = new FileResource();
        String st = fr.asString();
        st = st.replace('\n', ' ');
        int size = 200;

        MarkovZero mz = new MarkovZero();
        runModel(mz, st, size, seed);

        MarkovOne mOne = new MarkovOne();
        runModel(mOne, st, size, seed);

        MarkovModel mThree = new MarkovModel(3);
        runModel(mThree, st, size, seed);

        MarkovFour mFour = new MarkovFour();
        runModel(mFour, st, size, seed);

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
        EfficientMarkovModel tmp = new EfficientMarkovModel(2);
        tmp.setRandom(42);
        tmp.setTraining("yes-this-is-a-thin-pretty-pink-thistle");
        System.out.println(tmp.getRandomText(50));
		/*
		 “le” occurs only once at the end of the training text
		It has 25 keys in the HashMap
		The maximum number of keys following a key is 3
		Keys that have the largest ArrayList (of size 3 in this case) are: “hi”, “s-”, “-t”, “is”, and “th”
		*/
    }

    public static void compareMethods()
    {
        FileResource fr = new FileResource();
        String st = fr.asString();
        st = st.replace('\n', ' ');
        int size = 1000;
        int seed = 42;

        IMarkovModel tmp;
        long str = System.nanoTime();
        tmp = new EfficientMarkovModel(2);
        runModel(tmp, st, size, seed);
        long end = System.nanoTime();

        long eff =  end - str;

        str = System.nanoTime();
        tmp = new MarkovModel(2);
        runModel(tmp, st, size, seed);
        end = System.nanoTime();

       long stock = end-str;

        System.out.println("faster than: " + stock/eff);

    }

}
