import java.io.*;
import java.util.regex.Pattern;
import java.util.*;

public class Matching
{
	public static AVLTree[] hashTable;
	
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("QUIT") == 0)
					break;

				command(input);
			}
			catch (IOException e)
			{
				System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
				break;
			}
		}
	}
	
	// 해시 함수
	private static int hash(String str) {
		int length = str.length();
		int sum = 0;
		for(int i = 0; i < length; i ++) {
			sum += (int)str.charAt(i);
		}
		return sum % 100;
	}
	
	private static void command(String input) throws IOException
	{	
		String[] arr = input.split(" ", 2);
		String type = arr[0];
		String content = arr[1];
		
		if (type.equals("<")) {inputData(content);}
		else if(type.equals("@")) {printData(content);}
		else if(type.equals("?")) {searchData(content);}
		else {throw new IOException();}
	}
	// 입력
	static void inputData(String content) throws IOException {
		try {
			hashTable = new AVLTree[100];
	        File file = new File(content);
	        BufferedReader br = new BufferedReader(new FileReader(file));
	        String line = null;
	        int k = 0;
	        while ((line = br.readLine()) != null) {
	        	k++;
	          for(int i = 0; i <= line.length() - 6; i ++) {
	        	  String sub = "";
	        	  Place p = new Place();
	        	  for(int j = 0; j < 6; j ++) {
	        		  sub += line.charAt(i + j);
	        		  p.line = k;
	        		  p.start = i + 1;
	        	  }
	        	  int slotNumber = hash(sub);
	        	  if(hashTable[slotNumber] == null) {
	        		  hashTable[slotNumber] = new AVLTree<String>();
	        	  }
	        	  hashTable[slotNumber].insert(sub, p);
	          }
	        }
	        br.close();
	      } catch (FileNotFoundException e) {
	        e.printStackTrace();
	      }
	}
	
	// 출력
	static void printData(String str) {
		int slotNumber = Integer.parseInt(str);
		try {
			AVLTree.preOrderPrint(hashTable[slotNumber].getRoot(), hashTable[slotNumber].getRoot());
			System.out.println("");
		} 
		catch (NullPointerException e){
			System.out.println("EMPTY");
		}
	}
	
	// 검색
	static void searchData(String str) {
		int k = 6;
		int length = str.length();
		int subNumber = length / k + (length % 6 == 0 ? 0 : 1);
		int lastLength = length % 6;
		boolean isLast = length % 6 == 0 ? false : true;
		List<List<Place>> place = new ArrayList<>(); 
		ArrayList<String> subList = new ArrayList<>();
		ArrayList<Place> answer = new ArrayList<>();
		
		for(int i = 0; i <= length - k; i += k) {
			subList.add(str.substring(i, i + k));
		}
		// 끝에서부터 6자리
		if (isLast == true) {
			subList.add(str.substring(length - 6, length));
		}
		
		// Input String을 6자리씩 끊어서 각각 어느 위치에 있는지 2-D array(place) 에 삽입
		for(int i = 0; i < subNumber; i ++){
			for(String sub : subList) {
				List<Place> pList = new ArrayList<>();
				int slot = hash(sub);
				try {
					AVLNode<String> node = hashTable[slot].search(sub);
					if (node == AVLTree.NIL) {
						System.out.println("(0, 0)");
						return;
					}
					for(Place p : node.list) {
						pList.add(p);
					}
					place.add(pList);
				}catch (Exception e){
					System.out.println("(0, 0)");
					return;
				}
			}
		}
		
		// String 길이가 6이었으면 처음 찾은 Place들이 전부 정답
		if (place.size() == 1) {
			for(int i = 0; i < place.get(0).size(); i++) {
				if(i == place.get(0).size() - 1) {
					System.out.print(place.get(0).get(i));
				}
				else {
					System.out.print(place.get(0).get(i) + " ");
				}
			}
			System.out.println("");
			return;
		}
		
		// String 길이가 6이 넘는 경우
		for(int i = 0; i < place.get(0).size(); i ++) {
			Place cur = place.get(0).get(i);
			int line = cur.line; 
			int start = cur.start;
			int step = k; 
			int lastStep = length % 6;
			
			for(int j = 1; j < subNumber; j ++) {
				boolean isMatch = false;
				if(j == subNumber - 1 && isLast == true) {
					for(int x = 0; x < place.get(j).size(); x++) {
						Place now = place.get(j).get(x);
						if( now.line == line && now.start == start + k * (j-1) + lastStep ) {
							isMatch = true;
							break;
						}
						else if( now.line > line) {
							isMatch = false;
							break;
						}
						if( x == place.get(j).size() - 1) {
							isMatch = false;
							break;
						}
					}
				}
				else {
					for(int x = 0; x < place.get(j).size(); x++) {
						Place now = place.get(j).get(x);
						if( now.line == line && now.start == start + k * j) {
							isMatch = true;
							break;
						}
						else if( now.line > line) {
							isMatch = false;
							break;
						}
						if (x == place.get(j).size() - 1) {
							isMatch = false;
							break;
						}
					}
				}
				if(isMatch == false) {
					break;
				}
				if(j == subNumber - 1) {
					answer.add(cur);
				}	
			}
		}
		for(int i = 0; i < answer.size(); i++) {
			if(i == answer.size() - 1) {
				System.out.print(answer.get(i));
			}
			else {
				System.out.print(answer.get(i) + " ");
			}
		}
		System.out.println("");
		return;
	}
}
	
