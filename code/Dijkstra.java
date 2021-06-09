import java.util.*;

public class Dijkstra {
	
	public static ArrayList<Integer> pathTracking(int[] path, int end, int dist) {
		Stack<Integer> stack = new Stack<>();
		ArrayList<Integer> result = new ArrayList<>();
		
		stack.add(end);
		int next = path[end];
		
		while(stack.peek() != next ) {
			stack.add(next);
			next = path[next];
		}
		while(!stack.isEmpty()) {
			result.add(stack.pop());
		}
		result.add(dist);
		return result;
	}
	
	// start, end 는 index값 
	public static ArrayList<Integer> findPath(int[][] graph, int N, int start, int end) {
		PriorityQueue<Edge> heap = new PriorityQueue<>();
		boolean[] visited = new boolean[N];
		int[] distance = new int[N];
		int[] path = new int[N];
				
		Arrays.fill(distance, Integer.MAX_VALUE);
		heap.add(new Edge(start, 0));
		distance[start] = 0;
		path[start] = start;
		
		// minHeap을 이용한 다익스트라 알고리즘
		while(!heap.isEmpty()) {
			Edge nowEdge = heap.poll();
			int now = nowEdge.station;
			
			if(visited[now]) 
				continue;
			else 
				visited[now] = true;
			
			for(int next = 0; next < N; next ++) {
				// 연결이 되어있다면 --> relaxing 시도
				if (graph[now][next] != 0) {
					int dist = graph[now][next];
					if(distance[next] >= distance[now] + dist) {
						distance[next] = distance[now] + dist;
						heap.add(new Edge(next, distance[next]));
						path[next] = now;
					}
				}
			}
		}
		return pathTracking(path, end, distance[end]);
	}
}

class Edge implements Comparable<Edge>{
	int station;
	int dist;
	
	public Edge(int station, int dist) {
		this.station = station;
		this.dist = dist;
	}
	
	@Override
	public int compareTo(Edge o) {
		return Integer.compare(this.dist, o.dist);
	}
}