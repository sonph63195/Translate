/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

/**
 *
 * @author SONPH
 * @param <E>
 */
public class BinarySearchTree<E extends Comparable<? super E>> {

    TreeNode<E> root;
    
    public TreeNode<E> getRoot() {
        return this.root;
    }

    /**
     * Create an empty tree
     */
    public BinarySearchTree() {
        this.root = null;
    }

    /**
     * Clear all node in tree
     */
    public void clear() {
        this.root = null;
    }

    /**
     * Check empty tree
     *
     * @return Return true if this tree has root = null
     */
    public boolean isEmpty() {
        return root == null;
    }
    
    /**
     * Insert the key to tree
     * @param key 
     */
    public void insert(E key) {
        this.root = insert(this.root, key);
    }
    
    /**
     * Insert the key to the node
     * @param root the node contain the key
     * @param key
     * @return 
     */
    TreeNode<E> insert(TreeNode<E> root, E key) {
        // if the tree is empty, add this key to the new node of the tree
        if (root == null) {
            root = new TreeNode<>(key);
            return root;
        }
        if (key.compareTo(root.key) == 0) {
            root.key = key;
        }else if (key.compareTo(root.key) < 0) {
            root.left = insert(root.left, key);
        } else if (key.compareTo(root.key) > 0) {
            root.right = insert(root.right, key);
        }

        /* return the (unchanged) node pointer */
        return root;
    }
    
    /**
     * Print the node 
     * @param node 
     */
    public void visit(TreeNode<E> node) {
        if (node != null) {
            System.out.println(node.key);
        }
    }

    /**
     * Searching node with key
     *
     * @param node The node start searching
     * @param key The key need to find
     * @return Return node contain key, return null if the key dose not exist in
     * the tree
     */
    public TreeNode<E> search(TreeNode<E> node, E key) {
        if (node == null || node.key.compareTo(key) == 0) {
            return node;
        }
        if (node.key.compareTo(key) > 0) {
            return search(node.left, key);
        }
        return search(node.right, key);
    }

    /**
     * Searching node with key start from the root
     *
     * @param key The key need to find
     * @return Return node contain key, return null if the key dose not exist in
     * the tree
     */
    public TreeNode<E> search(E key) {
        return search(root, key);
    }

}
