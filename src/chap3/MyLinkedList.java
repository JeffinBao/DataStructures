package chap3;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Author: baojianfeng
 * Date: 2017-09-19
 * It is a Double-linked list
 */
public class MyLinkedList<T> implements Iterable<T> {
    private int theSize;
    private Node<T> beginMarker;
    private Node<T> endMarker;

    public MyLinkedList() {
        clear();
    }

    public void clear() {
        beginMarker = new Node<T>(null, null, null);
        endMarker = new Node<T>(null, beginMarker, null);
        beginMarker.next = endMarker;

        theSize = 0;
    }

    public int size() {
        return theSize;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean add(T x) {
        add(size(), x);
        return true;
    }

    public void add(int idx, T x) {
        addBefore(getNode(idx, 0, size()), x);
    }

    public void addBefore(Node<T> p, T x) {
        Node<T> newNode = new Node<T>(x, p.prev, p);
        newNode.prev.next = newNode;
        p.prev = newNode;
        theSize++;
    }

    public T get(int idx) {
        return getNode(idx).data;
    }

    private Node<T> getNode(int idx) {
        return getNode(idx, 0, size() - 1);
    }

    private Node<T> getNode(int idx, int lower, int upper) {
        Node<T> p;

        if (idx < lower || idx > upper)
            throw new IndexOutOfBoundsException("getNode index: " + idx + "; size: " + size());

        if (idx < size() / 2) {
            p = beginMarker.next;
            for (int i = 0; i < idx; i++) {
                p = p.next;
            }
        } else {
            p = endMarker;
            for (int i = size(); i > idx; i--) {
                p = p.prev;
            }
        }

        return p;
    }

    public T set(int idx, T newVal) {
        Node<T> p = getNode(idx);
        T oldVal = p.data;

        p.data = newVal;

        return oldVal;
    }

    public T remove(int idx) {
        return remove(getNode(idx));
    }

    private T remove(Node<T> p) {
        p.next.prev = p.prev;
        p.prev.next = p.next;
        theSize--;

        return p.data;
    }

    public void swap(int idx1, int idx2) {
        if ((idx1 < 0 || idx1 > size() - 1) ||
                (idx2 < 0 || idx2 > size() - 1))
            throw new IndexOutOfBoundsException("not able to swap");

        Node<T> p1 = null;
        Node<T> p2 = null;
        // if idx1 > idx2, consider this situation as the same of swap(idx2, idx1)
        if (idx1 < idx2) {
            p1 = getNode(idx1);
            p2 = getNode(idx2);
        } else if (idx1 > idx2) {
            p1 = getNode(idx2);
            p2 = getNode(idx1);
        } else {
            return;
        }

        // two conditions: swap neighbourhood elements or other element pair
        if (Math.abs(idx1 - idx2) == 1) {
            // change 6 arrows' direction
            p1.prev.next = p2;
            p2.next.prev = p1;

            p1.next = p2.next;
            p2.prev = p1.prev;
            p1.prev = p2;
            p2.next = p1;

            // change below to above four lines code, see the difference
//            Node<T> p1Prev = p1.prev;
//            p1.next = p2.next;
//            p1.prev = p2;
//            p2.next = p1;
//            p2.prev = p1Prev;
        } else {
            // change 8 arrows' direction
            p1.prev.next = p2;
            p1.next.prev = p2;
            p2.prev.next = p1;
            p2.next.prev = p1;

            Node<T> p2Prev = p2.prev; // store p2.prev, since it will change
            p2.prev = p1.prev;
            p1.prev = p2Prev;
            Node<T> p1Next = p1.next; // store p1.next, since it will change
            p1.next = p2.next;
            p2.next = p1Next;
        }

    }

    public void shift(int offset) {
        int shiftCount = offset % size();

//        if (shiftCount < 0) {
//            for (int i = 0; i < -shiftCount; i++) {
//                Node<T> pTail = getNode(size() - 1);
//                Node<T> pTailPrev = pTail.prev;
//                Node<T> pHead = getNode(0);
//
//                // change the connection with 3 nodes
//                pTailPrev.next = endMarker;
//                endMarker.prev = pTailPrev;
//                pTail.next = pHead;
//                pHead.prev = pTail;
//                beginMarker.next = pTail;
//                pTail.prev = beginMarker;
//            }
//        } else if (shiftCount > 0) {
//            for (int i = 0; i < shiftCount; i++) {
//                Node<T> pHead = getNode(0);
//                Node<T> p1 = getNode(1);
//                Node<T> pCurrentTail = getNode(size() - 1);
//
//                // change the connection with 3 nodes
//                p1.prev = beginMarker;
//                beginMarker.next = p1;
//                pCurrentTail.next = pHead;
//                pHead.prev = pCurrentTail;
//                pHead.next = endMarker;
//                endMarker.prev = pHead;
//            }
//
//        } else
//            return;

        // shift all the element together, instead of shift them one by one like the above codes
        // no matter shift forward or backward, tail will connect to head
        Node<T> head = getNode(0);
        Node<T> tail = getNode(size() - 1);
        // no matter move backward or forward, old tail and head will connect to each other
        tail.next = head;
        head.prev = tail;
        if (shiftCount < 0) {
            // move backward, use a new head
            Node<T> newHead = getNode(size() + shiftCount);

            endMarker.prev = newHead.prev;
            newHead.prev.next = endMarker;
            beginMarker.next = newHead;
            newHead.prev = beginMarker;
        } else if (shiftCount > 0) {
            // move forward, use a new tail
            Node<T> newTail = getNode(shiftCount - 1);

            beginMarker.next = newTail.next;
            newTail.next.prev = beginMarker;
            endMarker.prev = newTail;
            newTail.next = endMarker;
        } else
            return;
    }

    public void erase(int idx, int eraseSize) {
        if (idx < 0 || idx > size() - 1)
            throw new IndexOutOfBoundsException("can not erase elements");

        Node<T> current = getNode(idx);

        for (int i = 0; i < eraseSize; i++) {
            remove(current);
            if (null != current.next)
                current = current.next;
            else
                break;
        }
    }

    public void insertList(int idx, MyLinkedList<T> list) {
        for (int i = 0; i < list.size(); i++) {
            Node<T> p = list.getNode(i);
            add(idx + i, p.data);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[ ");

        for (T x : this)
            sb.append(x + " ");
        sb.append("]");

        return sb.toString();
    }

    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<T> {
        private Node<T> current = MyLinkedList.this.beginMarker.next;
        private boolean okToRemove = false;

        public boolean hasNext() {
            return current != endMarker;
        }

        public T next() {
            if (!hasNext())
                throw new NoSuchElementException();

            T nextItem = current.data;
            current = current.next;
            okToRemove = true;
            return nextItem;
        }

        public void remove() {
            if (!okToRemove)
                throw new IllegalStateException();

            MyLinkedList.this.remove(current.prev);
            okToRemove = false;
        }
    }

    private static class Node<T> {
        public T data;
        public Node<T> prev;
        public Node<T> next;

        public Node(T data, Node prev, Node next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }
}

class TestLinkedList {
    public static void main(String[] args) {
        MyLinkedList<Integer> lst = new MyLinkedList<Integer>();

        for (int i = 0; i < 10; i++)
            lst.add(i);
        for (int i = 20; i < 30; i++)
            lst.add(0, i);

        System.out.println("original list is: " + lst);

//        lst.remove(0);
//        lst.remove(lst.size() - 1);
//
//        System.out.println(lst);
//
//        Iterator<Integer> iterator = lst.iterator();
//        while (iterator.hasNext()) {
//            iterator.next();
//            iterator.remove();
//            System.out.println(lst);
//        }

        // test swap method
        // swap neighbouring elements
        lst.swap(0, 1);
        System.out.println("swap(0, 1) is: " + lst);
        lst.swap(1, 0);
        System.out.println("swap(1, 0) is: " + lst);
        // swap regular element pair
        lst.swap(0, 3);
        System.out.println("swap(0, 3) is: " + lst);
        try {
            lst.swap(1, 23);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // test shift method
        // test negative value
        lst.shift(4);
        System.out.println("shift 4 positions is: " + lst);
        // test positive value
        lst.shift(-4);
        System.out.println("shift -4 positions is: " + lst);
        // test value exceed list's size
        lst.shift(lst.size() + 3);
        System.out.println("shift size+3 positions is: " + lst);

        // test erase method
        lst.erase(1, 2);
        System.out.println("erase 2 elements starting from position 1: " + lst);
        // test erase the last element
        lst.erase(lst.size() - 1, 1);
        System.out.println("erase the last element: " + lst);

        // test insertList method
        MyLinkedList<Integer> insertList = new MyLinkedList<Integer>();
        for (int i = 0; i < 5; i++) {
            insertList.add(i);
        }
        lst.insertList(lst.size() - 1, insertList);
        System.out.println("insert 5 element in the last element: " + lst);
    }
}

