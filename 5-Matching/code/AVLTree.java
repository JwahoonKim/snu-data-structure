
// 자료구조 수업의 AVLTree 부분 PPT를 참고하여 구현했습니다.
public class AVLTree<T extends Comparable<T>>{
	private AVLNode<T> root;
	public final static AVLNode NIL = new AVLNode(null, null, null, 0);
	
	public AVLTree() {
		root = NIL;
	}
	
	public AVLNode getRoot() {
		return root;
	}
	// 검색
	public AVLNode<T> search(T t) {
		return searchItem(root, t);
	}
	private AVLNode searchItem(AVLNode<T> tNode, T t) {
		if(tNode == NIL) return NIL;
		else if( t.compareTo(tNode.item) == 0 ) return tNode;
		else if( t.compareTo(tNode.item) < 0 ) {
			return searchItem(tNode.left, t);
		}
		else return searchItem(tNode.right, t);
	}
	
	// 삽입
	public void insert(T t, Place p) {
		root = insertItem(root, t, p);
	}
	// 같은경우도 체크해서 추가해주자
	private AVLNode insertItem(AVLNode<T> tNode, T t, Place p) {  
		int type;
		if(tNode == NIL) {
			tNode = new AVLNode(t, p);
			return tNode;
		}
		if(t.compareTo(tNode.item) == 0) {
			tNode.list.add(p);
		}
		else if(t.compareTo(tNode.item) < 0) {
			tNode.left = insertItem(tNode.left, t, p);
			tNode.height = 1 + Math.max(tNode.left.height, tNode.right.height);
			type = needBalance(tNode);
			if(type != NO_NEED) {
				tNode = balanceAVL(tNode, type);
			}
		}
		else if(t.compareTo(tNode.item) > 0) {
			tNode.right = insertItem(tNode.right, t, p);
			tNode.height = 1 + Math.max(tNode.right.height, tNode.left.height);
			type = needBalance(tNode);
			if(type != NO_NEED) {
				tNode = balanceAVL(tNode, type);
			}
		}
		return tNode;
	}
	
	// Balancing 작업
	private final int LL = 1, LR = 2, RR = 3, RL = 4, NO_NEED = 0, ILLEGAL = -1;
	private int needBalance(AVLNode t) {
		int type = ILLEGAL;
		if( t.left.height + 2 <= t.right.height){
			if(t.right.left.height <= t.right.right.height) { type = RR;}
			else type = RL;
		}
		else if(t.left.height >= t.right.height + 2) {
			if(t.left.left.height >= t.left.right.height) {type = LL;}
			else type = LR;
		}
		else type = NO_NEED;
		return type;
	}
	
	private AVLNode balanceAVL(AVLNode tNode, int type) {
		AVLNode returnNode = NIL;
		switch(type) {
		case LL:
			returnNode = rightRotate(tNode);
			break;
		case LR:
			tNode.left = leftRotate(tNode.left);
			returnNode = rightRotate(tNode);
			break;
		case RR:
			returnNode = leftRotate(tNode);
			break;
		case RL:
			tNode.right = rightRotate(tNode.right);
			returnNode = leftRotate(tNode);
			break;
		default:
			System.out.println("Impossible type!");
			break;
		}
		
		return returnNode;
	}
	
	private AVLNode leftRotate(AVLNode t) {
		AVLNode RChild = t.right;
		if(RChild == NIL) {System.out.println("left rotate - ERROR!");}
		AVLNode RLChild = RChild.left;
		RChild.left = t;
		t.right = RLChild;
		t.height = 1 + Math.max(t.left.height, t.right.height);
		RChild.height = 1 + Math.max(RChild.left.height, RChild.right.height);
		return RChild;
	}
	private AVLNode rightRotate(AVLNode t) {
		AVLNode LChild = t.left;
		if(LChild == NIL) {System.out.println("right rotate - ERROR!");}
		AVLNode LRChild = LChild.right;
		LChild.right = t;
		t.left = LRChild;
		t.height = 1 + Math.max(t.left.height, t.right.height);
		LChild.height = 1 + Math.max(LChild.left.height, LChild.right.height);
		return LChild;
	}
	
	// 전위순회 출력
	public static void preOrderPrint(AVLNode node, AVLNode root) {
		if(node == NIL) {return;}
		if(node == root) System.out.print(node.item);
		else System.out.print(" "+node.item);
		preOrderPrint(node.left, root);
		preOrderPrint(node.right, root);
	}
}
