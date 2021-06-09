import java.io.*;
import java.util.*;

public class Subway {

	public static void main(String[] args) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		// 경로 탐색에 필요한 자료들
		File data = new File(args[0]);
		int N = DataHandling.countStation(data);
		int[][] graph = DataHandling.makeGraph(data, N);
		HashMap numMap = DataHandling.numToIndex(data, N);
		HashMap indexMap = DataHandling.indexToNumber(data, N);
		HashMap numToNameMap = DataHandling.numToName(data, N);
		HashMap<String, String[]> nameMap = DataHandling.nameToNumber(data, N);
	
		while(true) {
			
			String input = br.readLine();
			
			if(input.equals("QUIT")) {
				break;
			}
			
			ArrayList<String> answer = new ArrayList<>();
			String[] info = input.split(" ");
			String startName = info[0];
			String endName = info[1];		
			
			String[] startPoint = nameMap.get(startName);
			String[] endPoint = nameMap.get(endName);
			
			ArrayList<Integer> answerPath = null;
			for(int start = 0; start < startPoint.length; start ++) {
				for(int end = 0; end < endPoint.length; end ++) {
					String startNumber = startPoint[start];
					String endNumber = endPoint[end];
					int startIndex = (int) numMap.get(startNumber);
					int endIndex = (int) numMap.get(endNumber);
					
					ArrayList<Integer> nowPath = Dijkstra.findPath(graph, N, startIndex, endIndex);
					if(answerPath == null) {
						answerPath = nowPath;
					}
					else if(answerPath.get(answerPath.size() - 1) > nowPath.get(nowPath.size() - 1)) {
						answerPath = nowPath;
					}
				}
			}
			
			// 역이름으로 출력
			for(int i = 0; i < answerPath.size() - 1; i ++) {
				int nowIndex = answerPath.get(i);
				String nowNumber = (String) indexMap.get(nowIndex);
				String station = (String) numToNameMap.get(nowNumber);
				answer.add(station);
			}
			
			// 환승 역 처리
			for(int i = 1; i < answer.size(); i ++) {
				String prev = answer.get(i - 1);
				String now = answer.get(i);
				if(prev.equals(now)) {
					answer.remove(i - 1);
					answer.remove(i - 1);
					answer.add(i - 1, "[" + now + "]");
				}
			}
			
			// 출력부
			for(int i = 0; i < answer.size(); i ++) {
				if(i == answer.size() - 1) {
					System.out.print(answer.get(i));
				}
				else System.out.print(answer.get(i) + " ");
			}
			System.out.println("");
			// 소요시간 출력
			System.out.println(answerPath.get(answerPath.size() - 1));			
		}
	}
}

