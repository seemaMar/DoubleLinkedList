
/**
 * LinearNode represents a node in a linked list.
 *
 * @author Java Foundations, mvail
 * @version 4.0
 */
public class LinearNode<T> {
	private LinearNode<T> next;
	private LinearNode<T> previous;
	private T element;

	/**
  	 * Creates an empty node.
  	 */
	public LinearNode() {
		next = null;
		element = null;
	}

	/**
  	 * Creates a node storing the specified element.
 	 *
  	 * @param elem
  	 *            the element to be stored within the new node
  	 */
	public LinearNode(T elem) {
		next = null;
		element = elem;
	}

	/**
 	 * Returns the node that follows this one.
  	 *
  	 * @return the node that follows the current one
  	 */
	public LinearNode<T> getNext() {
		return next;
	}

	/**
 	 * Sets the node that follows this one.
 	 *
 	 * @param node
 	 *            the node to be set to follow the current one
 	 */
	public void setNext(LinearNode<T> node) {
		next = node;
	}

	/**
 	 * Returns the element stored in this node.
 	 *
 	 * @return the element stored in this node
 	 */
	public T getElement() {
		return element;
	}

	/**
 	 * Returns the node prior to this one.
  	 *
  	 * @return the node prior the current one
  	 */
	public LinearNode<T> getPrevious() {
		return previous;
	}
	
	/**
	 * Sets the node prior to this one.
	 *
	 * @param node
	 *            the node to be set previous to the current one
	 */
	public void setPrevious(LinearNode<T> node) {
		previous = node;
	}
	
	/**
 	 * Sets the element stored in this node.
  	 *
  	 * @param elem
  	 *            the element to be stored in this node
  	 */
	public void setElement(T elem) {
		element = elem;
	}

	@Override
	public String toString() {
		return "Element: " + element.toString() + " Has next: " + (next != null);
	}
}

