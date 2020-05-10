package textgen;

import java.util.*;

/**
 * An implementation of the MTG interface that uses a list of lists.
 *
 * @author UC San Diego Intermediate Programming MOOC team
 */
public class MarkovTextGeneratorLoL implements MarkovTextGenerator {

  // The list of words with their next words
  private List<ListNode> wordList;

  // The starting "word"
  private String starter;

  // The random number generator
  private Random rnGenerator;

  public MarkovTextGeneratorLoL(Random generator) {
    wordList = new LinkedList<ListNode>();
    starter = "";
    rnGenerator = generator;
  }

  /** Train the generator by adding the sourceText */
  @Override
  public void train(String sourceText) {
    // DONE: Implement this method
    List<String> words = Arrays.asList(sourceText.split("\\s+"));
    if(words.get(0).equals("") && words.size() == 1)return;
    starter = words.get(0);
    String prevWord = starter;

    if (words.size() > 1) {
      for (String word : words.subList(1, words.size())) { //LOL upper is exclusive :)))))
        ListNode wordNode = getListNode(prevWord);
        wordNode.addNextWord(word);

        prevWord = word;
      }

    }
    // Add starter word to be next word for the last word
    getListNode(prevWord).addNextWord(starter);
  }

  /** Generate the number of words requested. */
  @Override
  public String generateText(int numWords) {
    // DONE: Implement this method
    if (wordList.size() == 0 || numWords == 0) return "";
    else if (numWords < 0) throw new IllegalArgumentException("numWords cannot be smaller than 0");
    String currWord = starter;
    StringBuilder res = new StringBuilder(currWord);
    while (numWords > 1) {
      currWord = getListNode(currWord).getRandomNextWord(rnGenerator);
      res.append(" ").append(currWord);
      numWords--;
    }
    return res.toString();
  }

  // Can be helpful for debugging
  @Override
  public String toString() {
    String toReturn = "";
    for (ListNode n : wordList) {
      toReturn += n.toString();
    }
    return toReturn;
  }

  /** Retrain the generator from scratch on the source text */
  @Override
  public void retrain(String sourceText) {
    // DONE: Implement this method.
    wordList = new LinkedList<>();
    starter = "";
    train(sourceText);
  }

  // DONE: Add any private helper methods you need here.
  private ListNode getListNode(String word) {
    Optional<ListNode> tmpSearch =
        wordList.stream().parallel().filter(k -> k.getWord().equals(word)).findAny();
    ListNode wordNode;
    if (!tmpSearch.isPresent()) {
      wordList.add(wordNode = new ListNode(word));
    } else wordNode = tmpSearch.get();

    return wordNode;
  }

  /**
   * This is a minimal set of tests. Note that it can be difficult to test methods/classes with
   * randomized behavior.
   *
   * @param args
   */
  public static void main(String[] args) {
    // feed the generator a fixed random value for repeatable behavior
    MarkovTextGeneratorLoL gen = new MarkovTextGeneratorLoL(new Random(42));
    String textString =
        "Hello.  Hello there.  This is a test.  Hello there.  Hello Bob.  Test again.";
    System.out.println(textString);
    gen.train(textString);
    System.out.println(gen);
    System.out.println(gen.generateText(20));
    String textString2 =
        "You say yes, I say no, "
            + "You say stop, and I say go, go, go, "
            + "Oh no. You say goodbye and I say hello, hello, hello, "
            + "I don't know why you say goodbye, I say hello, hello, hello, "
            + "I don't know why you say goodbye, I say hello. "
            + "I say high, you say low, "
            + "You say why, and I say I don't know. "
            + "Oh no. "
            + "You say goodbye and I say hello, hello, hello. "
            + "I don't know why you say goodbye, I say hello, hello, hello, "
            + "I don't know why you say goodbye, I say hello. "
            + "Why, why, why, why, why, why, "
            + "Do you say goodbye. "
            + "Oh no. "
            + "You say goodbye and I say hello, hello, hello. "
            + "I don't know why you say goodbye, I say hello, hello, hello, "
            + "I don't know why you say goodbye, I say hello. "
            + "You say yes, I say no, "
            + "You say stop and I say go, go, go. "
            + "Oh, oh no. "
            + "You say goodbye and I say hello, hello, hello. "
            + "I don't know why you say goodbye, I say hello, hello, hello, "
            + "I don't know why you say goodbye, I say hello, hello, hello, "
            + "I don't know why you say goodbye, I say hello, hello, hello,";
    System.out.println(textString2);
    gen.retrain(textString2);
    System.out.println(gen);
    System.out.println(gen.generateText(20));
  }
}

/** Links a word to the next words in the list You should use this class in your implementation. */
class ListNode {
  // The word that is linking to the next words
  private String word;

  // The next words that could follow it
  private List<String> nextWords;

  ListNode(String word) {
    this.word = word;
    nextWords = new LinkedList<String>();
  }

  public String getWord() {
    return word;
  }

  public void addNextWord(String nextWord) {
    nextWords.add(nextWord);
  }

  public String getRandomNextWord(Random generator) {
    // DONE: Implement this method
    // The random number generator should be passed from
    // the MarkovTextGeneratorLoL class
    return nextWords.get(generator.nextInt(nextWords.size()));
  }

  public String toString() {
    String toReturn = word + ": ";
    for (String s : nextWords) {
      toReturn += s + "->";
    }
    toReturn += "\n";
    return toReturn;
  }
}
