import java.io.*;
import java.util.regex.Pattern;
import java.util.*;

public class Matching
{
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
		String fileName = arr[1];
		
		if (type.equals("<")) {
			// file을 읽는 코드, 출처 : https://www.w3schools.com/java/java_files_read.asp
		    try {
		        File file = new File(fileName);
		        Scanner sc = new Scanner(file);
		        while (sc.hasNextLine()) {
		          String data = sc.nextLine();
		          System.out.println(data);
		        }
		        sc.close();
		      } catch (FileNotFoundException e) {
		        e.printStackTrace();
		      }
		}
		
		else if(type.equals("@")) {}
		
		else if(type.equals("?")) {}
	}
}
