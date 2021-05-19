import java.util.*;

public class AVLNode<T extends Comparable<T>> {
	public T item;
	public AVLNode left, right;
	public int height;
	public LinkedList<Place> list = new LinkedList<>();
	
	public AVLNode(T sub) {
		item = sub;
		left = null;
		right = null;
		height = 1;
	}
	public AVLNode(T sub, AVLNode left, AVLNode right) {
		item = sub;
		this.left = left;
		this.right = right;
		height = 1;
	}
	public AVLNode(T sub, AVLNode left, AVLNode right, int height) {
		item = sub;
		this.left = left;
		this.right = right;
		this.height = height;
	}
}

class Place {
	int line;
	int start;
	
	public Place(int line, int start) {
		this.line = line;
		this.start = start;
	}
}
