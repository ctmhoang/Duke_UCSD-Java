import java.util.Arrays;
import java.util.StringJoiner;
import java.util.stream.IntStream;

public class WordGram
{
    private String[] myWords;
    private int myHash;

    public WordGram(String[] source, int start, int size)
    {
        myWords = new String[size];
        System.arraycopy(source, start, myWords, 0, size);
        myHash = toString().hashCode();
    }

    public String wordAt(int index)
    {
        if (index < 0 || index >= myWords.length)
        {
            throw new IndexOutOfBoundsException("bad index in wordAt " + index);
        }
        return myWords[index];
    }

    public int length()
    {
        // DONE: Complete this method
        return myWords.length;
    }

    @Override
    public String toString()
    {
        StringJoiner ret = new StringJoiner(" ");
        // DONE: Complete this method
        Arrays.stream(myWords).forEachOrdered(ret::add);
        return ret.toString();
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof WordGram)) return false;
        WordGram other = (WordGram) o;
        // DONE: Complete this method
        if (myWords.length != other.length()) return false;
        return IntStream.range(0, myWords.length).allMatch(i -> myWords[i].equals(other.wordAt(i)));
    }

    public WordGram shiftAdd(String word)
    {
//        WordGram out = new WordGram(myWords, 0, myWords.length);
        // shift all words one towards 0 and add word at the end. 
        // you lose the first word
        // DONE: Complete this method
        String[] newWords = new String[myWords.length];
        System.arraycopy(myWords, 1, newWords, 0, myWords.length - 1);
        newWords[newWords.length - 1] = word;
        return new WordGram(newWords, 0, newWords.length);
    }

    public int hashCode()
    {
        return myHash;
    }

}