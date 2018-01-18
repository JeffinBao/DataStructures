package chap4;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Author: baojianfeng
 * Date: 2018-01-07
 * Description: implement a top-down splay tree
 */
public class SplayTreeTopDown<T extends Comparable<? super T>> {
    private BinaryNode<T> root;

    /**
     * constructor
     */
    public SplayTreeTopDown() {
        root = null;
    }

    /**
     * insert a value into splay tree
     * @param x val
     */
    public void insert(T x) {
        if (root == null)
            root = new BinaryNode<>(x);

        // below is splay first, then put the insert element at the root node
//        // if x already exist, do nothing
//        if (contains(x))
//            return;
//
//        BinaryNode<T> newRoot = new BinaryNode<>(x);
//        BinaryNode<T> t1; // every element in t1 is less than x
//        BinaryNode<T> t2; // every element in t2 is greater than x
//        int compare = x.compareTo(root.element);
//
//        if (compare < 0) {
//            t1 = root.left;
//            root.left = null; // set root.left = null, now only root node and its right subtree left
//            t2 = root;
//        } else if (compare > 0){
//            t2 = root.right;
//            root.right = null; // set root.right = null, now only root node and its left subtree left
//            t1 = root;
//        } else
//            return;
//
//        newRoot.left = t1;
//        newRoot.right = t2;
//        root = newRoot;

        // below is insert like a BST first, then splay the tree.
        // Note below solution will result in different tree structures compared to above splay first solution
        root = insert(root, x);
        root = splay(root, x);
    }


    /**
     * private method to insert an element like a normal BST
     * @param root input root node
     * @param x x
     * @return output root node
     */
    private BinaryNode<T> insert(BinaryNode<T> root, T x) {
        if (root == null)
            return new BinaryNode<>(x);

        int compare = x.compareTo(root.element);

        if (compare < 0) {
            root.left = insert(root.left, x);
        } else if (compare > 0) {
            root.right = insert(root.right, x);
        }

        return root;
    }

    /**
     * remove a value in the tree
     * @param x value
     * @return true if the value exists, otherwise return false
     */
    public boolean remove(T x) {
        // if x doesn't exist, return false
        if (!contains(x))
            return false;

        if (root.left == null) // if root.left = null, means there is no t1, where all elements are less than root.element
            root = root.right;
        else {
            // conduct join operation, for details, please see the original paper Fig.7(a)
            BinaryNode<T> newRoot = root.left;
            newRoot = splay(newRoot, x); // find the maxi element in the left subtree and splay it to root
            newRoot.right = root.right;
            root = newRoot;
        }

        return true;

    }

    /**
     * find the maximum value in the tree
     * @return maximum value
     */
    public T findMax() {
        if (root == null)
            return null;

        BinaryNode<T> temp = root;
        while (temp.right != null) {
            temp = temp.right;
        }

        root = splay(root, temp.element);

        return temp.element;

    }

    /**
     * find the minimum value in the tree
     * @return minimum value
     */
    public T findMin() {
        if (root == null)
            return null;

        BinaryNode<T> temp = root;
        while (temp.left != null) {
            temp = temp.left;
        }

        root = splay(root, temp.element);

        return temp.element;
    }

    /**
     * verify whether value x is in the tree
     * @param x value
     * @return true if value x is in the tree, otherwise return false;
     */
    public boolean contains(T x) {
        if (root == null)
            return false;

        root = splay(root, x);

        return root.element == x;
    }

    /**
     * splay the tree
     * @param root root
     * @param x value
     * @return the new root node
     */
    private BinaryNode<T> splay(BinaryNode<T> root, T x) {
        if (root == null)
            return null;

        BinaryNode<T> nullNode = new BinaryNode<>();
        BinaryNode<T> leftLink = nullNode;
        BinaryNode<T> rightLink = nullNode;

        while (true) {
            int compare = x.compareTo(root.element);

            if (compare < 0) {
                if (root.left == null)
                    break;

                if (x.compareTo(root.left.element) < 0) {
                    // rotate right
                    BinaryNode<T> temp = root.left;
                    root.left = temp.right;
                    temp.right = root;
                    root = temp;

                    // must check root.left after rotate right, for the situation that element is not in the tree,
                    // zig-zig operation only have two nodes, and after rotation, root.left may be null
                    if (root.left == null)
                        break;
                }

                // link to rightLink's left subtree
                rightLink.left = root;
                rightLink = root; // move rightLink to next level
                root = root.left; // move root to next level
            } else if (compare > 0) {
                if (root.right == null)
                    break;

                if (x.compareTo(root.right.element) > 0) {
                    // rotate left
                    BinaryNode<T> temp = root.right;
                    root.right = temp.left;
                    temp.left = root;
                    root = temp;

                    // must check root.right after rotate left, for the situation that element is not in the tree,
                    // zig-zig operation only have two nodes, and after rotation, root.right may be null
                    if (root.right == null)
                        break;
                }

                // link to leftLink's right subtree
                leftLink.right = root;
                leftLink = root; // move leftLink to next level
                root = root.right; // move root to next level
            } else
                break;
        }

        // assemble
        leftLink.right = root.left;
        rightLink.left = root.right;
        // assemble nullNode's right subtree(
        // same as the leftLink's right subtree, but can not use leftLink,
        // since it has been moved to the bottom level) to new root's left
        root.left = nullNode.right;
        root.right = nullNode.left; // same as link to nullNode's right subtree

        return root;

    }

    /**
     * public method, print a tree level by level
     */
    public void printLevels() {
        printLevels(root);
    }

    /**
     * print the splay tree level by level
     * @param root root
     */
    private void printLevels(BinaryNode<T> root) {
        if (root == null)
            return;

        Queue<BinaryNode<T>> queue = new LinkedList<>();
        queue.add(root);

        while (queue.size() > 0) {
            int nodeCount = queue.size();

            while (nodeCount > 0) {
                BinaryNode<T> node = queue.poll();

                System.out.print(node.element + " ");
                if (node.left != null)
                    queue.add(node.left);
                if (node.right != null)
                    queue.add(node.right);

                nodeCount--;
            }
            System.out.println();
        }
    }

    /**
     * private class, node data structure
     * @param <T> generic type
     */
    private class BinaryNode<T> {
        T element;
        BinaryNode<T> left;
        BinaryNode<T> right;

        BinaryNode() {
            this(null, null, null);
        }

        BinaryNode(T element) {
            this(element, null, null);
        }

        BinaryNode(T element, BinaryNode<T> left, BinaryNode<T> right) {
            this.element = element;
            this.left = left;
            this.right = right;
        }
    }

    public static void main(String[] args) {
        SplayTreeTopDown<Integer> splayTreeTopDown = new SplayTreeTopDown<>();

        splayTreeTopDown.insert(100);
        splayTreeTopDown.insert(50);
        splayTreeTopDown.printLevels();
        splayTreeTopDown.insert(73);
        splayTreeTopDown.printLevels();
        splayTreeTopDown.insert(45);
        splayTreeTopDown.printLevels();
        splayTreeTopDown.insert(89);
        splayTreeTopDown.printLevels();
        splayTreeTopDown.insert(34);
        splayTreeTopDown.printLevels();
    }
}
