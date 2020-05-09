package textgen;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/** @author UC San Diego MOOC team */
public class MyLinkedListTester {

  private static final int LONG_LIST_LENGTH = 10;

  MyLinkedList<String> shortList;
  MyLinkedList<Integer> emptyList;
  MyLinkedList<Integer> longerList;
  MyLinkedList<Integer> list1;

  /** @throws java.lang.Exception */
  @Before
  public void setUp() throws Exception {
    // Feel free to use these lists, or add your own
    shortList = new MyLinkedList<String>();
    shortList.add("A");
    shortList.add("B");
    emptyList = new MyLinkedList<Integer>();
    longerList = new MyLinkedList<Integer>();
    for (int i = 0; i < LONG_LIST_LENGTH; i++) {
      longerList.add(i);
    }
    list1 = new MyLinkedList<Integer>();
    list1.add(65);
    list1.add(21);
    list1.add(42);
  }

  /** Test if the get method is working correctly. */
  /*You should not need to add much to this method.
   * We provide it as an example of a thorough test. */
  @Test
  public void testGet() {
    // test empty list, get should throw an exception
    try {
      emptyList.get(0);
      fail("Check out of bounds");
    } catch (IndexOutOfBoundsException e) {

    }

    // test short list, first contents, then out of bounds
    assertEquals("Check first", "A", shortList.get(0));
    assertEquals("Check second", "B", shortList.get(1));

    try {
      shortList.get(-1);
      fail("Check out of bounds");
    } catch (IndexOutOfBoundsException e) {

    }
    try {
      shortList.get(2);
      fail("Check out of bounds");
    } catch (IndexOutOfBoundsException e) {

    }
    // test longer list contents
    for (int i = 0; i < LONG_LIST_LENGTH; i++) {
      assertEquals("Check " + i + " element", (Integer) i, longerList.get(i));
    }

    // test off the end of the longer array
    try {
      longerList.get(-1);
      fail("Check out of bounds");
    } catch (IndexOutOfBoundsException e) {

    }
    try {
      longerList.get(LONG_LIST_LENGTH);
      fail("Check out of bounds");
    } catch (IndexOutOfBoundsException e) {
    }
  }

  /**
   * Test removing an element from the list. We've included the example from the concept challenge.
   * You will want to add more tests.
   */
  @Test
  public void testRemove() {
    int a = list1.remove(0);
    assertEquals("Remove: check a is correct ", 65, a);
    assertEquals("Remove: check element 0 is correct ", (Integer) 21, list1.get(0));
    assertEquals("Remove: check size is correct ", 2, list1.size());

    // DONE: Add more tests here
    LLNode<Integer> tmp = list1.tail;
    int i = list1.size;

    while (i >= 0) {
      tmp = tmp.prev;
      i--;
    }
    assertNull("Pointer is not correctly set", tmp.data);
    //Add more test :((
    try{
      list1.remove(123123);
      fail("Cannot remove with index larger than size");
    }catch (IndexOutOfBoundsException ignored){}

    try {
      list1.remove(-1);
      fail("Check out of bounds");
    } catch (IndexOutOfBoundsException ignored) {}

  }

  /** Test adding an element into the end of the list, specifically public boolean add(E element) */
  @Test
  public void testAddEnd() {
    // DONE: implement this test
    boolean isAdded = list1.add(123);
    assertEquals("Remove: Added to end is correct ", 123, list1.get(list1.size - 1).intValue());
  }

  /** Test the size of the list */
  @Test
  public void testSize() {
    // DONE: implement this test
    assertEquals("Size: before add", 3, list1.size);
    list1.remove(1);
    list1.remove(0);
    assertEquals("Size: before remove 2 nodes", 1, list1.size);
  }

  /**
   * Test adding an element into the list at a specified index, specifically: public void add(int
   * index, E element)
   */
  @Test
  public void testAddAtIndex() {
    // DONE: implement this test
    list1.add(2, 123);
    assertEquals("Add: 123 to index 2", 123, list1.get(2).intValue());
    list1.add(0, 777);
    assertEquals("Add: 777 to index 0", 777, list1.get(0).intValue());
    //Add more test :((
    try{
      list1.add(-123,12);
      fail("Cannot add value with index negative");
    }catch (IndexOutOfBoundsException ignored){}

    try{
      list1.add(123123,12);
      fail("Cannot add value with index larger than size");
    }catch (IndexOutOfBoundsException ignored){}

    try{
      list1.add(0,null);
      fail("Cannot set value to null");
    }catch (NullPointerException ignored){}


  }

  /** Test setting an element in the list */
  @Test
  public void testSet() {
    // DONE: implement this test
    list1.set(0, 13);
    assertEquals("Set: 13 to index 0", 13, list1.get(0).intValue());
    //Add more test :((
    try{
      list1.set(123123,12);
      fail("Cannot set with index larger than size");
    }catch (IndexOutOfBoundsException ignored){}

    try {
      list1.set(-1,14234);
      fail("Check out of bounds");
    } catch (IndexOutOfBoundsException ignored) {}

      try {
        list1.set(0,null);
        fail("Cannot set null value to nodes data");
      } catch (NullPointerException ignored) {}

    }

  // TODO: Optionally add more test methods.

}
