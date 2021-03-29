
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<T> implements ListInterface<T> {
	// dummy head
	Node<T> head;
	int numItems;

	public MyLinkedList() {
		head = new Node<T>(null);
	}

	/**
	 * {@code Iterable<T>}를 구현하여 iterator() 메소드를 제공하는 클래스의 인스턴스는 다음과 같은 자바 for-each
	 * 문법의 혜택을 볼 수 있다.
	 * 
	 * <pre>
	 * for (T item : iterable) {
	 * 	item.someMethod();
	 * }
	 * </pre>
	 * 
	 * @see PrintCmd#apply(MovieDB)
	 * @see SearchCmd#apply(MovieDB)
	 * @see java.lang.Iterable#iterator()
	 */
	public final Iterator<T> iterator() {
		return new MyLinkedListIterator<T>(this);
	}

	@Override
	public boolean isEmpty() {
		return head.getNext() == null;
	}

	@Override
	public int size() {
		return numItems;
	}

	@Override
	public T first() {
		return head.getNext().getItem();
	}

	@Override
	// 정렬 순서에 맞게 삽입하는 함수
	public void add(T item) {
		Node<T> nowNode = head;
		while (nowNode.next != null) {
			T nextItem = nowNode.next.item;
			// 같은 내용이면 삽입 X
			if (item.equals(nextItem))
				return;
			else if (item.compareTo(nextItem) < 0) {
				break;
			} else {
				nowNode = nowNode.next;
			}
		}
		nowNode.insertNext(item);
		numItems += 1;
	}

	@Override
	public void removeAll() {
		head.setNext(null);
	}
}

class MyLinkedListIterator<T> implements Iterator<T> {

	private MyLinkedList<T> list;
	private Node<T> curr;
	private Node<T> prev;

	public MyLinkedListIterator(MyLinkedList<T> list) {
		this.list = list;
		this.curr = list.head;
		this.prev = null;
	}

	@Override
	public boolean hasNext() {
		return curr.getNext() != null;
	}

	@Override
	public T next() {
		if (!hasNext())
			throw new NoSuchElementException();

		prev = curr;
		curr = curr.getNext();

		return curr.getItem();
	}

	@Override
	public void remove() {
		if (prev == null)
			throw new IllegalStateException("next() should be called first");
		if (curr == null)
			throw new NoSuchElementException();
		prev.removeNext();
		list.numItems -= 1;
		curr = prev;
		prev = null;
	}
}