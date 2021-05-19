import java.util.*;

public class AVLNode<T extends Comparable<T>> {
	public T item;
	public AVLNode left, right;
	public int height;
	public LinkedList<Place> list = new LinkedList<>();
	
	public AVLNode(T sub) {
		item = sub;
		left = AVLTree.NIL;
		right = AVLTree.NIL;
		height = 1;
	}
	public AVLNode(T sub, Place p) {
		item = sub;
		left = AVLTree.NIL;
		right = AVLTree.NIL;
		height = 1;
		list.add(p);
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
	
	public Place() {
		this.line = 0;
		this.start = 0;
	}
	public Place(int line, int start) {
		this.line = line;
		this.start = start;
	}
	public String toString(){
		return "(" + this.line + ", " + this.start + ")";
	}
}
