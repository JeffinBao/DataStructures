package chap3;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Author: baojianfeng
 * Date: 2017-09-24
 */
public class MyArrayList<T> implements Iterable<T> {
    private int theSize;
    private T[] theItems;
    private static final int DEFAULT_CAPACITY = 10;

    public MyArrayList() {
        clear();
    }

    public int size() {
        return theSize;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public T get(int idx) {
        if (idx < 0 || idx >= size())
            throw new ArrayIndexOutOfBoundsException("Index " + idx + "; size " + size());

        return theItems[idx];
    }

    public T set(int idx, T newVal) {
        if (idx < 0 || idx >= size())
            throw new ArrayIndexOutOfBoundsException("Index " + idx + "; size " + size());

        T old = theItems[idx];
        theItems[idx] = newVal;
        return old;
    }

    @SuppressWarnings("unchecked")
    public void ensureCapacity(int newCapacity) {
        if (newCapacity < theSize)
            return;

        T[] old = theItems;
        theItems = (T[]) new Object[newCapacity];
        for (int i = 0; i < size(); i++)
            theItems[i] = old[i];
    }

    public boolean add(T x) {
        add(size(), x);
        return true;
    }

    public void add(int idx, T x) {
        if (theItems.length == size())
            ensureCapacity(size() * 2 + 1);

        for (int i = theSize; i > idx; i--)
            theItems[i] = theItems[i - 1];

        theItems[idx] = x;
        theSize++;
    }

    public T remove(int idx) {
        T removedItem = theItems[idx];

        for (int i = idx; i < size() - 1; i++)
            theItems[i] = theItems[i + 1];
        theSize--;

        return removedItem;
    }

    public void clear() {
        theSize = 0;
        ensureCapacity(DEFAULT_CAPACITY);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[ ");

        for (T x : this)
            sb.append(x + " ");
        sb.append("]");

        return new String(sb);

    }

    public Iterator<T> iterator() {
        return new ArrayListIterator();
    }

    private class ArrayListIterator implements Iterator<T> {
        private int current = 0;
        private boolean okToRemove = false;

        public boolean hasNext() {
            return current < size();
        }

        public T next() {
            if (!hasNext())
                throw new NoSuchElementException();

            okToRemove = true;
            return theItems[current++];
        }

        public void remove() {
            // call next() before remove(), otherwise will throw IllegalStateException
            if (!okToRemove)
                throw new IllegalStateException();

            // use --current, to remove the previous invocation element of next() function
            MyArrayList.this.remove(--current);
            okToRemove = false;
        }
    }
}

class TestArrayList {
    public static void main(String[] args) {
        MyArrayList<Integer> lst = new MyArrayList<Integer>();

        for (int i = 0; i < 10; i++)
            lst.add(i);
        for (int i = 20; i < 30; i++)
            lst.add(0, i);
        System.out.println(lst);

        lst.remove(0);
        lst.remove(lst.size() - 1);

        System.out.println(lst);
    }
}

