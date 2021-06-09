import java.util.*;
import java.io.*;

public class DataHandling {
	
	// 총 station의 개수를 count
	public static int countStation(File data) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(data));
		int n = 0;
		
		while(true) {
			String line = reader.readLine();
			if (line.equals("")) {
				reader.close();
				return n;
			}
			n ++;
		}
	}
	
	// key = 지하철 이름 , value는 지하철 넘버로하는 hash 만들기
	// ex.) 사당 --> [226, 433]
	public static HashMap<String, String[]> nameToNumber(File data, int N) throws IOException {
		HashMap<String, String[]> nameMap = new HashMap<>();
		BufferedReader reader = new BufferedReader(new FileReader(data));
		
		for(int i = 0; i < N; i ++) {
			String line = reader.readLine();
			String[] info = line.split(" ");
			String number = info[0];
			String station = info[1];
			
			if(nameMap.containsKey(station)) {
				String[] oldNumbers = nameMap.get(station);
				String[] newNumbers = new String[oldNumbers.length + 1];
				for (int j = 0; j < oldNumbers.length; j ++) {
					newNumbers[j] = oldNumbers[j];
				}
				newNumbers[oldNumbers.length] = number;
				nameMap.put(station, newNumbers);
			}
	
			else {
				String[] numbers = { number };
				nameMap.put(station, numbers);
			}
		}
		reader.close();
		return nameMap;
	}
	
	// key : 지하철 number , value : 역 이름인 hash
	public static HashMap<String, String> numToName(File data, int N) throws IOException {
		HashMap<String, String> numToNameMap = new HashMap<>();
		BufferedReader reader = new BufferedReader(new FileReader(data));
		
		for(int i = 0; i < N; i ++) {
			String line = reader.readLine();
			String[] info = line.split(" ");
			String number = info[0];
			String station = info[1];
			numToNameMap.put(number, station);
		}
		reader.close();
		return numToNameMap;
	}
	
	// 지하철 number를 0 ~ N으로 인덱싱
	public static HashMap<String, Integer> numToIndex(File data, int N) throws IOException {
		HashMap<String, Integer> numMap = new HashMap<>();
		BufferedReader reader = new BufferedReader(new FileReader(data));
		
		for(int i = 0; i < N; i ++) {
			String line = reader.readLine();
			String[] info = line.split(" ");
			String number = info[0];
			numMap.put(number, i);
		}
		reader.close();
		return numMap;
	}
	
	// key : index , value : 역 번호 인 hash 리턴
	public static HashMap<Integer, String> indexToNumber(File data, int N) throws IOException {
		HashMap<Integer, String> indexMap = new HashMap<>();
		BufferedReader reader = new BufferedReader(new FileReader(data));
		
		for(int i = 0; i < N; i ++) {
			String line = reader.readLine();
			String[] info = line.split(" ");
			String number = info[0];
			indexMap.put(i, number);
		}
		reader.close();
		return indexMap;
	}
	
	// 연결정보를 이용하여 graph화
	public static int[][] makeGraph(File data, int N) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(data));
		int[][] graph = new int[N][N];
		String line = null;
		HashMap<String, String[]> stationMap = new HashMap<>();
		HashMap<String, Integer> numMap = numToIndex(data, N);
		
		// 1. 환승역 처리해주기
		while(true) {
			line = reader.readLine();
			if(line.equals("")) break;
			String[] info = line.split(" ");
			String number = info[0];
			String station = info[1];
			
			if (stationMap.containsKey(station)) {
				String[] oldNumbers = stationMap.get(station);
				String[] newNumbers = new String[oldNumbers.length + 1];
				
				for (int i = 0; i < oldNumbers.length; i ++) {
					newNumbers[i] = oldNumbers[i];
					
					 int st1 = (int) numMap.get(oldNumbers[i]);
					 int st2 = (int) numMap.get(number);
					 int time = 5;
					
					 graph[st1][st2] = time;
					 graph[st2][st1] = time;
				}
				newNumbers[oldNumbers.length] = number;
				
				stationMap.replace(station, newNumbers);
			}
			else {
				String[] numbers = { number };
				stationMap.put(station, numbers);
			}
			
		}
		// 2. 그 외 연결된 역 처리해주기
		while((line = reader.readLine()) != null) {
			 String[] info = line.split(" ");
			 
			 int st1 = (int) numMap.get(info[0]);
			 int st2 = (int) numMap.get(info[1]);
			 int time = Integer.parseInt(info[2]);
			 
			 graph[st1][st2] = time;
		}
		reader.close();
		return graph;
	}
	
	
}
