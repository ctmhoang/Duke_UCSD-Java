package textgen;

import java.util.AbstractList;

/**
 * A class that implements a doubly linked list
 *
 * @author UC San Diego Intermediate Programming MOOC team
 * @param <E> The type of the elements stored in the list
 */
public class MyLinkedList<E> extends AbstractList<E> {
  LLNode<E> head;
  LLNode<E> tail;
  int size;

  /** Create a new empty LinkedList */
  public MyLinkedList() {
    // DONE: Implement this method
    head = new LLNode<>(null);
    tail = new LLNode<>(null);
    head.next = tail;
    tail.prev = head;
    size = 0;
  }

  /**
   * Appends an element to the end of the list
   *
   * @param element The element to add
   */
  public boolean add(E element) {
    // DONE: Implement this method;
    int oldSize = size;
    add(size - 1 ,element);
    return oldSize != size;
  }

  /**
   * Get the element at position index
   *
   * @throws IndexOutOfBoundsException if the index is out of bounds.
   */
  public E get(int index) {
    // DONE: Implement this method.
    return getNodeAt(index).data;
  }

  /**
   * Add an element to the list at the specified index
   *
   * @param index where the element should be added
   * @param element The element to add
   */
  public void add(int index, E element) {
    // DONE: Implement this method
    LLNode<E> prevNode = size == 0 ? head : getNodeAt(index);
    LLNode<E> tmpNode = prevNode.next;

    LLNode<E> insertNode = new LLNode<>(element);
    // Resetting surrounding nodes refs
    prevNode.next = insertNode;
    tmpNode.prev = insertNode;
    //Config new inserted node
    insertNode.prev = prevNode;
    insertNode.next = tmpNode;

    size ++;
  }

  /** Return the size of the list */
  public int size() {
    // DONE: Implement this method
    return size;
  }

  /**
   * Remove a node at the specified index and return its data element.
   *
   * @param index The index of the element to remove
   * @return The data element removed
   * @throws IndexOutOfBoundsException If index is outside the bounds of the list
   */
  public E remove(int index) {
    // TODO: Implement this method
    return null;
  }

  /**
   * Set an index position in the list to a new element
   *
   * @param index The index of the element to change
   * @param element The new element
   * @return The element that was replaced
   * @throws IndexOutOfBoundsException if the index is out of bounds.
   */
  public E set(int index, E element) {
    // TODO: Implement this method
    return null;
  }

  private LLNode<E> getNodeAt(int index)
  { if(index < 0 || index > size - 1) throw new IndexOutOfBoundsException("INACCESSIBLE TO INVALID INDEX");
    LLNode<E> tmp;
    //Half first part of list
    if(index <= (size - 1) /2){
      tmp = head.next;
      for(int i = 0 ; i < index ; i++)
        tmp = tmp.next;
    }else {
      tmp = tail.prev;
      for(int i = size - 1 ; i > index ; i--)
        tmp = tmp.prev;
    }
    return tmp;
  }
}

class LLNode<E> {
  LLNode<E> prev;
  LLNode<E> next;
  E data;

  // TODO: Add any other methods you think are useful here
  // E.g. you might want to add another constructor

  public LLNode(E e) {
    this.data = e;
    this.prev = null;
    this.next = null;
  }
}
