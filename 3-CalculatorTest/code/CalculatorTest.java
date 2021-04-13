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
				System.out.println("ERROR");
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
	public static int priority(String operator) {
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
	
	// 괄호가 쌍이 잘 맞는지 체크하는 함수 
	public static boolean checkBracket(String input) {
		int openBracket = 0;
		
		for(int i = 0; i < input.length(); i ++) {
			char now = input.charAt(i);
			if (now == '(') openBracket ++;
			else if(now == ')'){
				openBracket --;
				if(openBracket < 0) return false;
			}
		}
		if (openBracket != 0) return false;
		return true;
	}
	
	// 후위표기법으로 계산하는 방식을 구현한 함수
	public static void dealOperator(String operator, Stack<String> stack, List<String> list) {
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
				// ^와 unary는 right-associative 라서 조건문 추가
				if((operator.equals("^") && last.equals("^")) || (operator.equals("~") && last.equals("~"))) {
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
		String status = "";
		boolean init = false;
		int check = 0;
		// 숫자-공백-숫자 순서대로 나온 경우 에러 발생
		for(int i = 0; i < input.length(); i ++) {
			char now = input.charAt(i);
			if(!init && Character.isDigit(now)) {
				init = true;
				status = "Number";
				check += 2;
				continue;
			}
			if(init && Character.isDigit(now) && !status.equals("Number")) {
				status = "Number";
				check += 2;
			}
			else if(init && (now == ' ' || now == '\t') && !status.equals("Space")) {
				status = "Space";
				check += 1;
			}
			else if(init && !(now == ' ' || now == '\t') && !Character.isDigit(now)) {
				status = "Operator";
				check = 0;
			}
			
			if(check >= 5) throw new Exception();
		}
		
		//input의 공백 제거
		input = input.replaceAll(" ", "").replaceAll("\t", "");
		
		// 괄호가 유효하지 않은 경우 에러 발생
		if(checkBracket(input) == false) throw new Exception();
		
		//부호 뒤에 바로 닫는 괄호가 나오면 에러 발생
		boolean isOperator = false;
		for(int i = 0; i < input.length(); i ++) {
			char now = input.charAt(i);
			if(now == '*' || now == '/' || now == '%' || now == '+' || now == '-' || now == '^') {
				isOperator = true;
			}
			else {
				if(now == ')' && isOperator == true) throw new Exception();
				else isOperator = false;
			}
		}
		
		Stack<String> operStack = new Stack<>();
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
			//cur가 부호면 지금까지 쌓아온 number를 postfix리스트에 집어넣고
			//부호는 stack에서 처리(위에서 구현한 후위표기법 방식에 따라서)
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
		if(input.equals("-^")) return true;
		switch(input) {
		case "*": return true;
		case "/": return true;
		case "%": return true;
		case "+": return true;
		case "-": return true;
		case "^": return true;
		case "~": return true;
		default: return false;
		}
	}
	
	// postfix를 List형태로 받아 이를 계산해주는 함수
	public static long calculate(List<String> postfix) throws Exception {
		
		// postfix의 원소를 하나씩 보면서 부호가 나오는 경우 후위표기식 계산법에 따라 계산하는 과정
		while(postfix.size() != 1) {
			int cursor = 0;
			while(!isOperator(postfix.get(cursor))) {
				cursor ++;
			}
						
			String left = "";
			if(cursor >= 2) left = postfix.get(cursor - 2);
			String right = postfix.get(cursor - 1);
			String operator = postfix.get(cursor);
			
			if(operator.equals("~")) {
				if (right.charAt(0) == '-') {
					right = right.substring(1);
				}
				else right = "-" + right;
				
				postfix.remove(cursor - 1);
				postfix.remove(cursor - 1);
				postfix.add(cursor - 1, right);
			}
			
			else {
				long result = 0;
				long leftL = Long.parseLong(left);
				long rightL = Long.parseLong(right);
			
				switch(operator) {
				case "*": 
					result = leftL * rightL;
					break;
				case "/": 
					result = leftL / rightL;
					break;
				case "%": 
					result = leftL % rightL;
					break;
				case "+": 
					result = leftL + rightL;
					break;
				case "-": 
					result = leftL - rightL;
					break;
				case "^":
					if(leftL == 0 && rightL < 0) throw new Exception();
					result = (long)Math.pow(leftL, rightL);
					break;
				default: throw new Exception();
			}
			
			postfix.remove(cursor - 2);
			postfix.remove(cursor - 2);
			postfix.remove(cursor - 2);
			postfix.add(cursor - 2, Long.toString(result));
			}
		}
		return Long.parseLong(postfix.get(0));
	}
}

