package chap4;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Author: baojianfeng
 * Date: 2018-01-05
 */
public class SplayTreeAuthor<AnyType extends Comparable<? super  AnyType>> {

    /**
     * Construct the tree.
     */
    public SplayTreeAuthor( )
    {
        nullNode = new BinaryNode<AnyType>( null );
        nullNode.left = nullNode.right = nullNode;
        root = nullNode;
    }

    private BinaryNode<AnyType> newNode = null;  // Used between different inserts

    /**
     * Insert into the tree.
     * @param x the item to insert.
     */
    public void insert( AnyType x )
    {
        if( newNode == null )
            newNode = new BinaryNode<AnyType>( null );
        newNode.element = x;

        if( root == nullNode )
        {
            newNode.left = newNode.right = nullNode;
            root = newNode;
        }
        else
        {
            root = splay( x, root );

            int compareResult = x.compareTo( root.element );

            if( compareResult < 0 )
            {
                newNode.left = root.left;
                newNode.right = root;
                root.left = nullNode;
                root = newNode;
            }
            else
            if( compareResult > 0 )
            {
                newNode.right = root.right;
                newNode.left = root;
                root.right = nullNode;
                root = newNode;
            }
            else
                return;   // No duplicates
        }
        newNode = null;   // So next insert will call new
    }

    /**
     * Remove from the tree.
     * @param x the item to remove.
     */
    public void remove( AnyType x )
    {
        if( !contains( x ) )
            return;

        BinaryNode<AnyType> newTree;

        // If x is found, it will be splayed to the root by contains
        if( root.left == nullNode )
            newTree = root.right;
        else
        {
            // Find the maximum in the left subtree
            // Splay it to the root; and then attach right child
            newTree = root.left;
            newTree = splay( x, newTree );
            newTree.right = root.right;
        }
        root = newTree;
    }

    /**
     * Find the smallest item in the tree.
     * Not the most efficient implementation (uses two passes), but has correct
     *     amortized behavior.
     * A good alternative is to first call find with parameter
     *     smaller than any item in the tree, then call findMin.
     * @return the smallest item or throw UnderflowException if empty.
     */
    public AnyType findMin( )
    {
        if( isEmpty( ) )
            throw new UnderflowException( );

        BinaryNode<AnyType> ptr = root;

        while( ptr.left != nullNode )
            ptr = ptr.left;

        root = splay( ptr.element, root );
        return ptr.element;
    }

    /**
     * Find the largest item in the tree.
     * Not the most efficient implementation (uses two passes), but has correct
     *     amortized behavior.
     * A good alternative is to first call find with parameter
     *     larger than any item in the tree, then call findMax.
     * @return the largest item or throw UnderflowException if empty.
     */
    public AnyType findMax( )
    {
        if( isEmpty( ) )
            throw new UnderflowException( );

        BinaryNode<AnyType> ptr = root;

        while( ptr.right != nullNode )
            ptr = ptr.right;

        root = splay( ptr.element, root );
        return ptr.element;
    }

    /**
     * Find an item in the tree.
     * @param x the item to search for.
     * @return true if x is found; otherwise false.
     */
    public boolean contains( AnyType x )
    {
        if( isEmpty( ) )
            return false;

        root = splay( x, root );

        return root.element.compareTo( x ) == 0;
    }

    /**
     * Make the tree logically empty.
     */
    public void makeEmpty( )
    {
        root = nullNode;
    }

    /**
     * Test if the tree is logically empty.
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty( )
    {
        return root == nullNode;
    }

    private BinaryNode<AnyType> header = new BinaryNode<AnyType>( null ); // For splay

    /**
     * Internal method to perform a top-down splay.
     * The last accessed node becomes the new root.
     * @param x the target item to splay around.
     * @param t the root of the subtree to splay.
     * @return the subtree after the splay.
     */
    private BinaryNode<AnyType> splay( AnyType x, BinaryNode<AnyType> t )
    {
        BinaryNode<AnyType> leftTreeMax, rightTreeMin;

        header.left = header.right = nullNode;
        leftTreeMax = rightTreeMin = header;

        nullNode.element = x;   // Guarantee a match

        for( ; ; )
        {
            int compareResult = x.compareTo( t.element );

            if( compareResult < 0 )
            {
                if( x.compareTo( t.left.element ) < 0 )  // ??? why don't check whether t.left is null
                    t = rotateWithLeftChild( t );
                if( t.left == nullNode )
                    break;
                // Link Right
                rightTreeMin.left = t;
                rightTreeMin = t;
                t = t.left;
            }
            else if( compareResult > 0 )
            {
                if( x.compareTo( t.right.element ) > 0 )
                    t = rotateWithRightChild( t );
                if( t.right == nullNode )
                    break;
                // Link Left
                leftTreeMax.right = t;
                leftTreeMax = t;
                t = t.right;
            }
            else
                break;
        }

        leftTreeMax.right = t.left;
        rightTreeMin.left = t.right;
        t.left = header.right;
        t.right = header.left;
        return t;
    }

    /**
     * Rotate binary tree node with left child.
     * For AVL trees, this is a single rotation for case 1.
     */
    private static <AnyType> BinaryNode<AnyType> rotateWithLeftChild( BinaryNode<AnyType> k2 )
    {
        BinaryNode<AnyType> k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;
        return k1;
    }

    /**
     * Rotate binary tree node with right child.
     * For AVL trees, this is a single rotation for case 4.
     */
    private static <AnyType> BinaryNode<AnyType> rotateWithRightChild( BinaryNode<AnyType> k1 )
    {
        BinaryNode<AnyType> k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;
        return k2;
    }

    /**
     * public method
     */
    public void printLevels() {
        printLevels(root);
    }

    /**
     * print the splay tree level by level
     * @param root root
     */
    private void printLevels(BinaryNode<AnyType> root) {
        if (root == null)
            return;

        Queue<BinaryNode<AnyType>> queue = new LinkedList<>();
        queue.add(root);

        while (queue.size() > 0) {
            int nodeCount = queue.size();

            while (nodeCount > 0) {
                BinaryNode<AnyType> node = queue.poll();

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

    // Basic node stored in unbalanced binary search trees
    private static class BinaryNode<AnyType>
    {
        // Constructors
        BinaryNode( AnyType theElement )
        {
            this( theElement, null, null );
        }

        BinaryNode( AnyType theElement, BinaryNode<AnyType> lt, BinaryNode<AnyType> rt )
        {
            element  = theElement;
            left     = lt;
            right    = rt;
        }

        AnyType element;            // The data in the node
        BinaryNode<AnyType> left;   // Left child
        BinaryNode<AnyType> right;  // Right child
    }

    private BinaryNode<AnyType> root;
    private BinaryNode<AnyType> nullNode;


    // Test program; should print min and max and nothing else
//    public static void main( String [ ] args )
//    {
//        SplayTree<Integer> t = new SplayTree<Integer>( );
//        final int NUMS = 40000;
//        final int GAP  =   307;
//
//        System.out.println( "Checking... (no bad output means success)" );
//
//        for( int i = GAP; i != 0; i = ( i + GAP ) % NUMS )
//            t.insert( i );
//        System.out.println( "Inserts complete" );
//
//        for( int i = 1; i < NUMS; i += 2 )
//            t.remove( i );
//        System.out.println( "Removes complete" );
//
//        if( t.findMin( ) != 2 || t.findMax( ) != NUMS - 2 )
//            System.out.println( "FindMin or FindMax error!" );
//
//        for( int i = 2; i < NUMS; i += 2 )
//            if( !t.contains( i ) )
//                System.out.println( "Error: find fails for " + i );
//
//        for( int i = 1; i < NUMS; i += 2 )
//            if( t.contains( i ) )
//                System.out.println( "Error: Found deleted item " + i );
//    }

    public static void main(String[] args) {
        SplayTreeAuthor<Integer> splayTree = new SplayTreeAuthor<>();

        splayTree.insert(100);
        splayTree.insert(50);
        splayTree.printLevels();
        splayTree.insert(73);
        splayTree.printLevels();
        splayTree.insert(45);
        splayTree.printLevels();
        splayTree.insert(89);
        splayTree.printLevels();
        splayTree.insert(34);
        splayTree.printLevels();
        splayTree.remove(45);
        splayTree.printLevels();
        splayTree.remove(69);
        splayTree.printLevels();
    }

}
