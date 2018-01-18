package chap4;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Author: baojianfeng
 * Date: 2018-01-07
 * Description: implement a bottom-up splay tree
 */
public class SplayTreeBottomUp<T extends Comparable<? super T>> {
    private BinaryNode<T> root;

    public SplayTreeBottomUp() {
        root = null;
    }

    /**
     * insert a value into splay tree
     * @param x val
     */
    public void insert(T x) {
        if (root == null)
            root = new BinaryNode<>(x);
        else {
            root = insert(x, root);
            root = splay(root, x);
        }

    }

    /**
     * This method is for inserting after splay operation
     * @param root root node
     * @param x element
     * @return new root node
     */
    private BinaryNode<T> insert(BinaryNode<T> root, T x) {
        int compare = x.compareTo(root.element);

        BinaryNode<T> newRoot = new BinaryNode<>(x);
        if (compare < 0) {
            newRoot.left = root.left;
            root.left = null;
            newRoot.right = root;
            root = newRoot;
        } else if (compare > 0) {
            newRoot.right = root.right;
            root.right = null;
            newRoot.left = root;
            root = newRoot;
        }

        return root;
    }

    /**
     * private method, traverse down from root node
     * @param x element
     * @param root 'root' node
     */
    private BinaryNode<T> insert(T x, BinaryNode<T> root) {
        if (root == null)
            return new BinaryNode<>(x);

        int compare = x.compareTo(root.element);
        if (compare < 0) {
            root.left = insert(x, root.left);
            if (root.left != null)
                root.left.parent = root;
        } else if (compare > 0) {
            root.right = insert(x, root.right);
            if (root.right != null)
                root.right.parent = root;
        }

        return root;
    }

    /**
     * remove a value in the tree
     * @param x value
     * @return true if the value exists, otherwise return false
     */
    public boolean remove(T x) {
        if (!contains(x))
            return false;

        if (root.left == null)
            root = root.right;
        else {
            BinaryNode<T> temp = root.right; // need to store root.right as temp, otherwise will have error removing element, don't know why?
            BinaryNode<T> newRoot = root.left;
            newRoot.parent = null;
            newRoot = splay(newRoot, x); // find the maximum element in left subtree, splay it
            newRoot.right = temp;
            if (temp != null)
                temp.parent = newRoot;
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

        BinaryNode<T> max = root;
        while (max.right != null)
            max = max.right;

        root = splay(root, max.element);

        return max.element;
    }

    /**
     * find the minimum value in the tree
     * @return minimum value
     */
    public T findMin() {
        if (root == null)
            return null;

        BinaryNode<T> min = root;
        while (min.left != null)
            min = min.left;

        root = splay(root, min.element);

        return min.element;
    }

    /**
     * verify whether value x is in the tree
     * @param x value
     * @return true if value x is in the tree, otherwise return false;
     */
    public boolean contains(T x) {
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

        BinaryNode<T> newRoot = findSplayNode(root, x);
        while (newRoot.parent != null) {
            if (newRoot.parent.parent == null) {
                // zig
                if (newRoot.parent.left == newRoot) {
                    // rotate right
                    newRoot = rotateRightWithParent(newRoot.parent);
                } else if (newRoot.parent.right == newRoot) {
                    // rotate left
                    newRoot = rotateLeftWithParent(newRoot.parent);
                }

            } else {
                // zig-zig or zig-zag
                BinaryNode<T> tempGrandparent = newRoot.parent.parent;
                BinaryNode<T> tempParent = newRoot.parent;

                if (tempGrandparent.left == tempParent) {
                    if (tempParent.left == newRoot) {
                        // zig-zig rotate right
                        // first rotate grandparent then parent
                        rotateRightWithParent(tempGrandparent);
                        newRoot = rotateRightWithParent(tempParent);
                    } else if (tempParent.right == newRoot) {
                        // zig-zag left child's right child
                        // first rotate parent then grandparent
                        rotateLeftWithParent(tempParent);
                        newRoot = rotateRightWithParent(tempGrandparent);
                    }
                } else if (tempGrandparent.right == tempParent) {
                    if (tempParent.right == newRoot) {
                        // zig-zig rotate left
                        // first rotate grandparent then parent
                        rotateLeftWithParent(tempGrandparent);
                        newRoot = rotateLeftWithParent(tempParent);
                    } else if (tempParent.left == newRoot) {
                        // zig-zag right child's left child
                        // first rotate parent then grandparent
                        rotateRightWithParent(tempParent);
                        newRoot = rotateLeftWithParent(tempGrandparent);
                    }
                }
            }
        }

        return newRoot;

    }

    /**
     * rotate right when there is a parent pointer
     * @param node node
     * @return new root node
     */
    private BinaryNode<T> rotateRightWithParent(BinaryNode<T> node) {
        BinaryNode<T> temp = node.left;
        temp.parent = node.parent; // node.parent is the 'root' node
        node.left = temp.right;
        if (node.left != null)
            node.left.parent = node;

        temp.right = node;
        node.parent = temp;
        if (temp.parent != null) {
            if (node == temp.parent.left)
                temp.parent.left = temp;
            else
                temp.parent.right = temp;
        } else
            root = temp;

        return temp;
    }

    /**
     * rotate left when there is a parent pointer
     * @param node node
     * @return new root node
     */
    private BinaryNode<T> rotateLeftWithParent(BinaryNode<T> node) {
        BinaryNode<T> temp = node.right;
        temp.parent = node.parent;
        node.right = temp.left;
        if (node.right != null)
            node.right.parent = node;

        temp.left = node;
        node.parent = temp;
        if (temp.parent != null) {
            if (node == temp.parent.left)
                temp.parent.left = temp;
            else
                temp.parent.right = temp;
        } else
            root = temp;

        return temp;
    }

    // below splay method is another way of splaying, however the result tree does not meet the bottom-up rule
//    private BinaryNode<T> splay(BinaryNode<T> node, T x) {
//        if (node == null) return null;
//
//        int compare1 = x.compareTo(node.element);
//        if (compare1 < 0) {
//            if (node.left == null)
//                return node;
//
//            int compare2 = x.compareTo(node.left.element);
//            if (compare2 < 0) {
//                node.left.left = splay(node.left.left, x);
//                node = rotateRight(node);
//            } else if (compare2 > 0) {
//                node.left.right = splay(node.left.right, x);
//                if (node.left.right != null)
//                    node.left = rotateLeft(node.left);
//            }
//
//            if (node.left == null)
//                return node;
//            else
//                return rotateRight(node);
//
//        } else if (compare1 > 0) {
//            if (node.right == null)
//                return node;
//
//            int compare2 = x.compareTo(node.right.element);
//            if (compare2 > 0) {
//                node.right.right = splay(node.right.right, x);
//                node = rotateLeft(node);
//            } else if (compare2 < 0) {
//                node.right.left = splay(node.right.left, x);
//                if (node.right.left != null)
//                    node.right = rotateRight(node.right);
//            }
//
//            if (node.right == null)
//                return node;
//            else
//                return rotateLeft(node);
//        } else
//            return node;
//    }

    /**
     * rotate right when there is no parent pointer
     * @param node node needs to be rotated
     * @return new 'root' node
     */
    private BinaryNode<T> rotateRight(BinaryNode<T> node) {
        BinaryNode<T> temp = node.left;
        node.left = temp.right;
        temp.right = node;

        return temp;
    }

    /**
     * rotate left when there is no parent pointer
     * @param node node needs to be rotated
     * @return new 'root' node
     */
    private BinaryNode<T> rotateLeft(BinaryNode<T> node) {
        BinaryNode<T> temp = node.right;
        node.right = temp.left;
        temp.left = node;

        return temp;
    }

    /**
     * find the node which needs to be splayed, if the node doesn't exist, return the last accessed node
     * @param node node
     * @param x element
     * @return node founded
     */
    private BinaryNode<T> findSplayNode(BinaryNode<T> node, T x) {
        if (node == null || node.left == null && node.right == null)
            return node;

        int compare = x.compareTo(node.element);
        if (compare < 0)
            if (node.left != null)
                return findSplayNode(node.left, x);
            else
                return node;
        else if (compare > 0)
            if (node.right != null)
                return findSplayNode(node.right, x);
            else
                return node;
        else
            return node;
    }

    /**
     * public method, print the tree level by level
     */
    public void printLevels() {
        printLevels(root);
    }

    /**
     * private method, print the tree level by level from the root node
     * @param node root node
     */
    private void printLevels(BinaryNode<T> node) {
        if (node == null)
            return;

        Queue<BinaryNode<T>> queue = new LinkedList<>();
        queue.add(node);
        while (!queue.isEmpty()) {
            int size = queue.size();

            for (int i = 0; i < size; i++) {
                BinaryNode<T> bn = queue.poll();
                System.out.print(bn.element + " ");
                if (bn.left != null)
                    queue.add(bn.left);
                if (bn.right != null)
                    queue.add(bn.right);
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * private class, node data structure
     * @param <T> Generic type
     */
    private class BinaryNode<T> {
        T element;
        BinaryNode<T> left;
        BinaryNode<T> right;
        BinaryNode<T> parent;

        BinaryNode() {
            this(null, null, null, null);
        }

        BinaryNode(T element) {
            this(element, null, null, null);
        }

        BinaryNode(T element, BinaryNode<T> parent) {
            this(element, null, null, parent);
        }

        BinaryNode(T element, BinaryNode<T> left, BinaryNode<T> right, BinaryNode<T> parent) {
            this.element = element;
            this.left = left;
            this.right = right;
            this.parent = parent;
        }
    }

    public static void main(String[] args) {
        SplayTreeBottomUp<Integer> bottomUp = new SplayTreeBottomUp<>();
        bottomUp.insert(3);
        bottomUp.insert(10);
        bottomUp.printLevels();
        bottomUp.insert(1);
        bottomUp.printLevels();
        bottomUp.insert(7);
        bottomUp.printLevels();
        bottomUp.insert(20);
        bottomUp.printLevels();
        bottomUp.insert(5);
        bottomUp.printLevels();
        bottomUp.insert(15);
        bottomUp.printLevels();
        bottomUp.insert(36);
        bottomUp.printLevels();
        bottomUp.remove(3);
        bottomUp.printLevels();
        bottomUp.remove(36);
        bottomUp.printLevels();
        bottomUp.remove(15);
        bottomUp.printLevels();
        bottomUp.remove(7);
        bottomUp.printLevels();
        bottomUp.insert(8);
        bottomUp.printLevels();
        bottomUp.insert(7);
        bottomUp.printLevels();
        bottomUp.insert(20);
        bottomUp.printLevels();

    }
}
