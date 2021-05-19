import java.io.*;
import java.util.regex.Pattern;
import java.util.*;

public class Matching
{
	public static AVLTree[] hashTable = new AVLTree[100];
	
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

	// 들어온 입력이 유효한지 체크하는 함수
	private static boolean isValid(String input) {
		return Pattern.matches("^[<@?]\\s\\S+", input);
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
		if(!isValid(input)) throw new IOException();
		String[] arr = input.split(" ");
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
	
	//출력
	static void printData(String str) {
		int slotNumber = Integer.parseInt(str);
		try {
			AVLTree.preOrderPrint(hashTable[slotNumber].getRoot(), hashTable[slotNumber].getRoot());
		} 
		catch (NullPointerException e){
			System.out.println("EMPTY");
		}
	}
	
	static void searchData(String str) {}
	
}
