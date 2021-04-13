import java.io.*;
import java.util.*;

public class CalculatorTest
{
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("q") == 0)
					break;

				command(input);
			}
			catch (Exception e)
			{
				System.err.println("ERROR");
			}
		}
	}
	
	private static void command(String input) throws Exception 
	{
		String answer = "";
		List<String> postfixList = makePostfix(input);
		
		// List로 받은 postfix를 출력 조건에 맞게 가공 
		for(int i = 0; i < postfixList.size(); i ++) {
			if(i == postfixList.size() - 1) answer += postfixList.get(i);
			else {
				answer += postfixList.get(i) + " ";
			}
		}
		
		// postfix를 계산한 결과
		long result = calculate(postfixList);
		System.out.println(answer);
		System.out.println(result);
	}
	
	// 연산자들끼리 우선순위를 비교하기 위해 만든 함수
	private static int priority(String operator) {
		switch(operator) {
		case "^": return 4;
		case "~": return 3;
		case "*": return 2;
		case "/": return 2;
		case "%": return 2;
		case "+": return 1;
		case "-": return 1;
		case "(": return 0;
		}
		return -1;
	}
	
	// 후위표기법을 만드는 규칙을 정의한 함수
	private static void dealOperator(String operator, Stack<String> stack, List<String> list) {
		if (stack.empty()) {
			stack.push(operator);
			return;
		}
		if(priority(operator) > priority(stack.peek())) {
			stack.push(operator);
			return;
		}
		while(!stack.empty()) {
			String last = stack.peek();
			if(priority(operator) > priority(last)) {
				stack.push(operator);
				return;
			}
			else {
				// ^가 두 번 이상 제곱꼴로 나올때는 기존 법칙과 좀 다르게 작동하여 조건문 추가
				if(operator.equals("^") && last.equals("^")) {
					stack.push(operator);
					return;
				}
				last = stack.pop();
				list.add(last);
			}
		}
		if(stack.empty()) {
			stack.push(operator);
			return;
		}
	}
	
	// postfix를 List형태로 만드는 함수
	public static List<String> makePostfix(String input) throws Exception {
		//input의 공백 제거
		input = input.replaceAll(" ", "").replaceAll("\t", "");
		Stack<String> operStack = new Stack<>();
		//ArrayList 사용 가능여부 질문
		List<String> postfix = new ArrayList<>();
		
		String number = "";
		boolean isUnary = true;
		for(int i = 0; i < input.length(); i++) {
			String cur = Character.toString(input.charAt(i));
			// cur가 숫자면 number에 추가
			if(Character.isDigit(cur.charAt(0)) == true) {
				number += cur;
				isUnary = false;
				continue;
			}
			//cur가 부호면 넘버를 postfix리스트에 집어넣고 부호는 stack에서 처리(위에서 정의한 후위표기법 방식에 따라서)
			else {
				if(number != "") {
					postfix.add(number);
					number = "";
				}
				if(isUnary == true && cur.equals("-")) {
					dealOperator("~", operStack, postfix);
					continue;
				}
				if(cur.equals("(")) {
					operStack.push(cur);
					continue;
				}
				else if(cur.equals(")") ) {
					while(true) {
						String oper = operStack.pop();
						if (oper.equals("(") ) break;
						else postfix.add(oper);
					}
					continue;
				}
				
				switch(cur) {
				case "^":
					dealOperator(cur, operStack, postfix);
					break;
				case "*":
					dealOperator(cur, operStack, postfix);
					break;
				case "/":
					dealOperator(cur, operStack, postfix);
					break;
				case "%":
					dealOperator(cur, operStack, postfix);
					break;
				case "+":
					dealOperator(cur, operStack, postfix);
					break;
				case "-":
					dealOperator(cur, operStack, postfix);
					break;
				default:
					throw new Exception();
				}
				isUnary = true;
			}
		}
		
		// 위에서 미처 처리하지못한 number와 operator 처리
		if(number != "") postfix.add(number);
		
		while(!operStack.empty()) {
			String last = operStack.pop();
			postfix.add(last);
		}
		return postfix;
	}
	
	// 문자열이 숫자인지 확인하는 함수를 검색해 가져왔습니다
	// 출처 ==> https://baejangho.com/entry/JAVA-isNumeric
	public static boolean isNumber(String input) {
		try {
		Double.parseDouble(input);
		return true;
		}
		catch (NumberFormatException e) {
		return false;
		}
	}
	
	public static boolean isOperator(String input) {
		switch(input) {
		case "*": return true;
		case "/": return true;
		case "%": return true;
		case "+": return true;
		case "-": return true;
		case "^": return true;
		default: return false;
		}
	}
	
	// postfix를 List형태로 받아 이를 계산해주는 함수
	public static long calculate(List<String> postfix) throws Exception {
		long result = 0;
		// postfix = [3, ~] 이라면 --> postfix = [-3] 으로 바꿔주는 기능 
		while(true) {
			int i = 0;
			while(i < postfix.size()) {
				if(postfix.get(i) == "~") {
					String number = postfix.get(i - 1);
					number = "-" + number;
					postfix.remove(i - 1);
					postfix.remove(i - 1);
					postfix.add(i - 1, number);
					postfix.add(i, " ");
					break;
				}
				i++;
			}
			if(i == postfix.size()) break;
		}
		int count = 0;
		for(int j = 0; j < postfix.size(); j ++) {
			if(postfix.get(j).equals(" ")) count++;
		}
		for(int k = 0; k < count; k ++) {
			postfix.remove(" ");
		}
		
		int numCount = 0, operCount = 0;
		for(int n = 0; n < postfix.size(); n++) {
			if(isNumber(postfix.get(n))) numCount ++;
			else operCount ++;
		}
		// postfix 안에 number 의 개수는 부호의 개수보다 1개 많아야 올바른 식임
		if(numCount != operCount + 1) throw new Exception();
		
		// postfix의 원소를 하나씩 보면서 부호가 나오는 경우 후위표기식 계산법처럼 계산하는 과정
		while(postfix.size() != 1) {
			int cursor = 0;
			while(!isOperator(postfix.get(cursor))) {
				cursor ++;
			}
			long left = Long.parseLong(postfix.get(cursor - 2));
			long right =  Long.parseLong(postfix.get(cursor - 1));
			String operator = postfix.get(cursor);
			switch(operator) {
			case "*": 
				result = left * right;
				break;
			case "/": 
				result = left / right;
				break;
			case "%": 
				result = left % right;
				break;
			case "+": 
				result = left + right;
				break;
			case "-": 
				result = left - right;
				break;
			case "^":
				if(right < 0) throw new Exception();
				result = (long)Math.pow(left, right);
				break;
			}
			postfix.remove(cursor - 2);
			postfix.remove(cursor - 2);
			postfix.remove(cursor - 2);
			postfix.add(cursor - 2, Long.toString(result));
		}
		return Long.parseLong(postfix.get(0));
	}
}

