package spelling;

import java.util.*;

/**
 * An trie data structure that implements the Dictionary and the AutoComplete ADT
 *
 * @author You
 */
public class AutoCompleteDictionaryTrie implements Dictionary, AutoComplete {

  private TrieNode root;
  private int size;

  public AutoCompleteDictionaryTrie() {
    root = new TrieNode();
  }

  /**
   * Insert a word into the trie. For the basic part of the assignment (part 2), you should convert
   * the string to all lower case before you insert it.
   *
   * <p>This method adds a word by creating and linking the necessary trie nodes into the trie, as
   * described outlined in the videos for this week. It should appropriately use existing nodes in
   * the trie, only creating new nodes when necessary. E.g. If the word "no" is already in the trie,
   * then adding the word "now" would add only one additional node (for the 'w').
   *
   * @return true if the word was successfully added or false if it already exists in the
   *     dictionary.
   */
  public boolean addWord(String word) {
    // DONE: Implement this method.
    char[] letters = word.toLowerCase().toCharArray();
    TrieNode currentNode = root;
    // Travel and add Node to trie
    for (char c : letters) {
      TrieNode tmp = currentNode.insert(c);
      currentNode = tmp == null ? currentNode.getChild(c) : tmp;
    }
    if (currentNode.endsWord()) return false;
    currentNode.setEndsWord(true);
    size++;
    return true;
  }

  /**
   * Return the number of words in the dictionary. This is NOT necessarily the same as the number of
   * TrieNodes in the trie.
   */
  public int size() {
    // DONE: Implement this method
    //    return size(root); LOL I forgot having size field :))))
    return size;
  }

  //  private int size(TrieNode node) {
  //    Queue<Character> child = new LinkedList<>(node.getValidNextCharacters());
  //    if (child.size() == 0) return 0;
  //    int count = 0;
  //    while (child.size() != 0) {
  //      TrieNode tmp = node.getChild(child.remove());
  //      if (tmp.endsWord()) count++;
  //      count += size(tmp);
  //    }
  //    return count;
  //  }

  /**
   * Returns whether the string is a word in the trie, using the algorithm described in the videos
   * for this week.
   */
  @Override
  public boolean isWord(String s) {
    // Done: Implement this method
    TrieNode tmp = getNode(s);
    return tmp != null && tmp.endsWord();
  }

  private TrieNode getNode(String s) {
    if (s.length() == 0) return root;
    TrieNode currNode = root;
    char[] letters = s.toLowerCase().toCharArray();
    for (char letter : letters) if ((currNode = currNode.getChild(letter)) == null) return null;
    return currNode;
  }

  /**
   * Return a list, in order of increasing (non-decreasing) word length, containing the
   * numCompletions shortest legal completions of the prefix string. All legal completions must be
   * valid words in the dictionary. If the prefix itself is a valid word, it is included in the list
   * of returned words.
   *
   * <p>The list of completions must contain all of the shortest completions, but when there are
   * ties, it may break them in any order. For example, if there the prefix string is "ste" and only
   * the words "step", "stem", "stew", "steer" and "steep" are in the dictionary, when the user asks
   * for 4 completions, the list must include "step", "stem" and "stew", but may include either the
   * word "steer" or "steep".
   *
   * <p>If this string prefix is not in the trie, it returns an empty list.
   *
   * @param prefix The text to use at the word stem
   * @param numCompletions The maximum number of predictions desired.
   * @return A list containing the up to numCompletions best predictions
   */
  @Override
  public List<String> predictCompletions(String prefix, int numCompletions) {
    // DONE: Implement this method
    // This method should implement the following algorithm:
    // 1. Find the stem in the trie.  If the stem does not appear in the trie, return an
    //    empty list
    TrieNode stem = getNode(prefix);
    if (stem == null || numCompletions == 0) return new ArrayList<>();
    // 2. Once the stem is found, perform a breadth first search to generate completions
    //    using the following algorithm:
    //    Create a queue (LinkedList) and add the node that completes the stem to the back
    //       of the list.
    Queue<TrieNode> queue = new LinkedList<>();
    queue.add(stem);
    //    Create a list of completions to return (initially empty)
    List<String> res = new ArrayList<>();
    //    While the queue is not empty and you don't have enough completions:
    while (!queue.isEmpty() && numCompletions != 0) {
      //       remove the first Node from the queue
      TrieNode tmp = queue.remove();
      //       If it is a word, add it to the completions list
      if (tmp.endsWord()) {
        res.add(tmp.getText());
        numCompletions--;
      }
      //       Add all of its child nodes to the back of the queue
      queue.addAll(tmp.getChildren());
    }
    // Return the list of completions
    return res;
  }

  // For debugging
  public void printTree() {
    printNode(root);
  }

  /** Do a pre-order traversal from this node down */
  public void printNode(TrieNode curr) {
    if (curr == null) return;

    System.out.println(curr.getText());

    TrieNode next = null;
    for (Character c : curr.getValidNextCharacters()) {
      next = curr.getChild(c);
      printNode(next);
    }
  }
}
