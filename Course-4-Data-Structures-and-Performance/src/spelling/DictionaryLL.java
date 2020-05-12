package spelling;

import java.util.LinkedList;

/** A class that implements the Dictionary interface using a LinkedList */
public class DictionaryLL implements Dictionary {

  private LinkedList<String> dict;

  // DONE: Add a constructor

  public DictionaryLL() {
    dict = new LinkedList<>();
  }

  /**
   * Add this word to the dictionary. Convert it to lowercase first for the assignment requirements.
   *
   * @param word The word to add
   * @return true if the word was added to the dictionary (it wasn't already there).
   */
  public boolean addWord(String word) {
    // DONE: Implement this method
    word = word.toLowerCase();
    if (dict.contains(word)) return false;
    dict.add(word);
    return true;
  }

  /** Return the number of words in the dictionary */
  public int size() {
    // DONE: Implement this method
    return dict.size();
  }

  /** Is this a word according to this dictionary? */
  public boolean isWord(String s) {
    // DONE: Implement this method
    return dict.contains(s.toLowerCase());
  }
}
