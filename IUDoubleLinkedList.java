import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;


/**
 * Double-linked node implementation of IndexedUnsortedList
 * Nodes have been implemented via LinearNode
 * 
 * @author seema
 *
 * @param <T> type to store
 */
public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {
	private LinearNode<T> head;
	private LinearNode<T> tail;
	private int size;
	private int modCount; //useful for iterator class
	
	public IUDoubleLinkedList() {
		head = tail = null;
		size = 0;
		modCount = 0;
	}
	
	@Override
	public void addToFront(T element) {
		ListIterator<T> lit = listIterator(); //using iterator method
		lit.add(element);
	}

	@Override
	public void addToRear(T element) {
		LinearNode<T> newNode = new LinearNode<T>(element);
		newNode.setPrevious(tail);
		if(isEmpty()) {
			head = newNode;
		} else {
			tail.setNext(newNode);
		}
		tail = newNode;
		size++;
		modCount++; //incremented each time the list is modified
	}

	@Override
	public void add(T element) {
		addToRear(element);
	}

	@Override
	public void addAfter(T element, T target) {
		ListIterator<T> lit = listIterator();
		boolean foundIt = false;
		while(lit.hasNext() && !foundIt) { //loop ends if element is found in list
			if(lit.next().equals(target)) {
				foundIt = true;
			}
		}
		if(!foundIt) { //element doesn't exist in the list
			throw new NoSuchElementException();
		}
		lit.add(element);
	}

	@Override
	public void add(int index, T element) {
		ListIterator<T> lit = listIterator(index);
		lit.add(element);
	}

	@Override
	public T removeFirst() {
		ListIterator<T> lit = listIterator();
		if(!lit.hasNext()) {
			throw new NoSuchElementException();
		}
		T retVal = lit.next();
		lit.remove();
		return retVal;
	}

	@Override
	public T removeLast() {
		if(isEmpty()) {
			throw new NoSuchElementException();
		}
		T retVal = tail.getElement();
		if(head == tail) {
			head = tail = null;
		} else {
			tail = tail.getPrevious();
			tail.setNext(null);
		}
		size--; //incremented/decremented based on how list is being modified
		modCount++;
		return retVal;
	}

	@Override
	public T remove(T element) {
		if(isEmpty()){
			throw new NoSuchElementException();
		}
		LinearNode<T> current = head;
		T retVal = null;
		
		while(current != null && !current.getElement().equals(element)){
			current = current.getNext();
		}
		
		//scenarios for where the element is
		if(current == null){
			throw new NoSuchElementException();
		} else if(current == head){
			retVal = head.getElement();
			head = head.getNext();
		} else if(current == tail){
			retVal = tail.getElement();
			tail = tail.getPrevious();
		} else {
			retVal = current.getElement();
			current.getPrevious().setNext(current.getNext());
			current.getPrevious().setPrevious(current.getPrevious());
		}
		modCount++;
		size--;
		return retVal;
	}

	@Override
	public T remove(int index) {
		if(index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		ListIterator<T> lit = listIterator(index);
		T retVal = lit.next();
		lit.remove();
		return retVal;
	}

	@Override
	public void set(int index, T element) {
		if((index < 0 || index >= size || isEmpty())){
			throw new IndexOutOfBoundsException();
		}
		
		if(index == 0){
			head.setElement(element);
		} else {
			LinearNode<T> current = head;
			for (int i = 0; i < index; i++) {
				current = current.getNext();
			}
			current.setElement(element);
		}
		modCount++;
	}

	@Override
	public T get(int index) {
		if((index < 0 || index >= size)){
			throw new IndexOutOfBoundsException();
		}
		
		T retVal = null;
		if(index == 0){
			retVal = head.getElement();
		} else {
			LinearNode<T> current = head;			
			for (int i = 0; i < index; i++) {
				current = current.getNext();
			}
			retVal = current.getElement();
		}
		return retVal;
	}

	@Override
	public int indexOf(T element) {
		int retVal = 0;
		if(isEmpty()){
			retVal = -1;
		}
		LinearNode<T> current = head;
		int index = 0;
		while(current != null && !current.getElement().equals(element)){
			current = current.getNext();
			index++;
		}
		
		if(current == null){
			retVal = -1;
		} else {
			retVal = index;
		}
		return retVal;
	}

	@Override
	public T first() {
		if(isEmpty()) {
			throw new NoSuchElementException();
		}
		return head.getElement();
	}

	@Override
	public T last() {
		if(isEmpty()) {
			throw new NoSuchElementException();
		}
		return tail.getElement();
	}

	@Override
	public boolean contains(T target) {
		LinearNode<T> current = head;
		
		while(current != null && !current.getElement().equals(target)){
			current = current.getNext();
		}
		return (current != null);
	}
	
	/**
	 * Appends proper syntax for given list
	 */
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("[");
		Iterator<T> it = iterator();
		while(it.hasNext()){
			str.append(it.next().toString());
			str.append(", ");
		}
		if(!isEmpty()) {
			str.delete(str.length()-2, str.length());
		}
		str.append("]");
		return str.toString();
	}

	@Override
	public boolean isEmpty() {
		return (head == null);
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Iterator<T> iterator() {
		return listIterator();
	}

	@Override
	public ListIterator<T> listIterator() {
		return new DLLIterator();
	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		return new DLLIterator(startingIndex);
	}
	
	/**
	 * ListIterator for IUDoubleLinkedList
	 * Alters list based on position of the iterator.
	 * @author seema
	 *
	 */
	private class DLLIterator implements ListIterator<T> {
		private LinearNode<T> nextNode;
		private int iterModCount; //incremented each time iterator is modified
		private int nextIndex;
		private LinearNode<T> lastReturnedNode; //most useful for set() and remove()
		
		public DLLIterator() {
			this(0);
		}
		
		public DLLIterator(int startingIndex) {
			if(startingIndex < 0 || startingIndex > size) {
				throw new IndexOutOfBoundsException();
			}
			nextNode = head;
			for(int i = 0; i < startingIndex; i++) {
				nextNode = nextNode.getNext();
			}
			nextIndex = startingIndex;
			iterModCount = modCount;
			lastReturnedNode = null;
		}
		
		@Override
		public void add(T element) {
			if(iterModCount != modCount) { //implicates that another iterator has changed the list
				throw new ConcurrentModificationException();
			}
			LinearNode<T> newNode = new LinearNode<T>(element);
			newNode.setNext(nextNode);
			if(nextNode == null) {
				if(tail != null) {
					tail.setNext(newNode);
					newNode.setPrevious(tail);
				}
				tail = newNode;
			} else {
				newNode.setPrevious(nextNode.getPrevious());
				nextNode.setPrevious(newNode);
			}
			if(nextNode == head) {
				head = newNode;
			} else if(newNode.getPrevious() != null) {
				newNode.getPrevious().setNext(newNode);
			}
			lastReturnedNode = null;
			nextIndex++;
			size++;
			modCount++;
			iterModCount++;
		}

		@Override
		public boolean hasNext() {
			if(iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			return (nextNode != null);
			
		}

		@Override
		public boolean hasPrevious() {
			if(iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			return (nextNode != head);
		}

		@Override
		public T next() {
			if(!hasNext()) {
				throw new NoSuchElementException();
			}
			T retVal = nextNode.getElement();
			//have to know that next() was the last move
			lastReturnedNode = nextNode;
			nextNode = nextNode.getNext();
			nextIndex++;
			return retVal;
		}

		@Override
		public int nextIndex() {
			return nextIndex;
		}

		@Override
		public T previous() {
			if(!hasPrevious()){
				throw new NoSuchElementException();
			}
			T retVal = null;
			if(nextNode == null){
				if(tail != null){
					retVal = (T) tail.getElement();
					nextNode = tail;
				}
			} else if(nextNode.getPrevious() != null){
				retVal = nextNode.getPrevious().getElement();
				nextNode = nextNode.getPrevious();
			}
			lastReturnedNode = nextNode;
			nextIndex++;
			return retVal;
		}

		@Override
		public int previousIndex() {
			return (nextIndex - 1);
		}

		@Override
		public void remove() {
			if(iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			if(lastReturnedNode == null) {
				throw new IllegalStateException();
			}
			if(lastReturnedNode == head) {
				head = lastReturnedNode.getNext();
			} else {
				lastReturnedNode.getPrevious().setNext(lastReturnedNode.getNext());
			}
			if(lastReturnedNode == tail) {
				tail = tail.getPrevious();
			} else {
				lastReturnedNode.getNext().setPrevious(lastReturnedNode.getPrevious());
			}
			if(lastReturnedNode == nextNode){
				nextNode = nextNode.getNext();
			} else { //last move was next()
				nextIndex--;
			}
			lastReturnedNode = null;
			modCount++;
			iterModCount++;
			size--;
		}

		@Override
		public void set(T element) {
			if(iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			if (isEmpty()) {
				throw new IllegalStateException();
			}
			if(lastReturnedNode == null){ //program doesn't have any node to set
				throw new IllegalStateException();
			} else if (lastReturnedNode != null) {
				lastReturnedNode.setElement(element);	
			}
			modCount++;
			iterModCount++;
		}
		
	}
}
