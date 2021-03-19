import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BigInteger {
    public static final String QUIT_COMMAND = "quit";
    public static final String MSG_INVALID_INPUT = "입력이 잘못되었습니다.";
    public String number;
    public char[] numArr = new char[201];
    public int size;
    // implement this
    public static final Pattern EXPRESSION_PATTERN = Pattern.compile("");

    public BigInteger(String s) {
        this.number = s;
        char[] arr = new char[201];
        size = s.length();

        // 입력받은 string을 한글자씩 거꾸로 배열에 옮기기 (연산 편해지도록)
        for (int i = size - 1, j = 0; i >= 0; i--, j++) {
            arr[j] = s.charAt(i);
        }

        // numArr는 입력받은 숫자를 거꾸로 배열에 저장
        this.numArr = arr;
    }

    public BigInteger add(BigInteger big) {
        int resultSize = Math.max(this.size, big.size) + 1;
        int[] resultArr = new int[resultSize];
        int nowTmp = 0;
        int nextTmp = 0;
        String result = "";
        for (int i = this.size; i < resultSize; i++) {
            numArr[i] = '0';
        }
        for (int i = big.size; i < resultSize; i++) {
            big.numArr[i] = '0';
        }
        // 한자리 씩 더하기 과정
        for (int i = 0; i < resultSize; i++) {
            int sum = (numArr[i] - '0') + (big.numArr[i] - '0');
            int res = sum + nowTmp;

            if (res >= 10) {
                nextTmp = 1;
                res -= 10;
            }

            resultArr[i] = res;
            nowTmp = nextTmp;
            nextTmp = 0;
        }

        if (resultArr[resultSize - 1] == 0) {
            for (int i = resultSize - 2; i >= 0; i--) {
                result += resultArr[i];
            }
        } else {
            for (int i = resultSize - 1; i >= 0; i--) {
                result += resultArr[i];
            }
        }

        return new BigInteger(result);
    }

    // 자신이 크면 1, 인자가 크면 2를 return 하는 함수
    public int findBigger(String num2) {
        String num1 = this.number;
        if (num1.length() > num2.length())
            return 1;
        else if (num1.length() < num2.length())
            return 2;
        else {
            for (int i = 0; i < num1.length(); i++) {
                int first = (int) (num1.charAt(i) - '0');
                int second = (int) (num2.charAt(i) - '0');
                if (first > second)
                    return 1;
                else if (first < second)
                    return 2;
                else
                    continue;
            }
        }
        return 3;
    }

    public BigInteger subtract(BigInteger big) {
        int resultSize = Math.max(this.size, big.size);
        int[] resultArr = new int[resultSize];
        String result = "";
        int nowTmp = 0;
        int nextTmp = 0;
        boolean flag = false;
        if (this.findBigger(big.number) == 3) {
            return new BigInteger("0");
        }
        // 더 큰 수 찾아서 빼고 부호조정하자
        if (this.findBigger(big.number) == 1) {
            // (num - big)
            for (int i = big.size; i < resultSize; i++) {
                big.numArr[i] = '0';
            }
            for (int i = 0; i < resultSize; i++) {
                int sum = (numArr[i] - '0') - (big.numArr[i] - '0');
                int res = sum + nowTmp;
                if (res < 0) {
                    nextTmp = -1;
                    res += 10;
                }
                resultArr[i] = res;
                nowTmp = nextTmp;
                nextTmp = 0;
            }

            for (int i = resultSize - 1; i >= 0; i--) {
                if (resultArr[i] == 0 && flag == false)
                    continue;
                if (resultArr[i] != 0)
                    flag = true;
                result += resultArr[i];
            }
        } else {
            result = '-' + big.subtract(this).number;
        }
        return new BigInteger(result);
    }

    public BigInteger multiply(BigInteger big) {
        int resultSize = 202;
        int[] resultArr = new int[resultSize];
        int nowTmp = 0;
        BigInteger total = new BigInteger("");

        for (int i = this.size; i < 102; i++) {
            numArr[i] = '0';
        }
        for (int i = big.size; i < 102; i++) {
            big.numArr[i] = '0';
        }
        for (int i = 0; i < big.size; i++) {
            nowTmp = 0;
            for (int j = 0; j < size; j++) {
                int mul = (numArr[j] - '0') * (big.numArr[i] - '0');
                int res = mul % 10 + nowTmp;
                if (res >= 10) {
                    res -= 10;
                    nowTmp = mul / 10 + 1;
                } else {
                    nowTmp = mul / 10;
                }
                resultArr[j + i] = res;
            }
            resultArr[i + size] = nowTmp;

            total = total.add(new BigInteger(BigInteger.toString(resultArr)));
            resultArr = new int[resultSize];
        }
        return new BigInteger(total.number);
    }

    @Override
    public String toString() {
        return number;
    }

    public static String toString(int[] arr) {
        String result = "";
        boolean flag = false;
        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i] == 0 && flag == false)
                continue;
            if (arr[i] != 0)
                flag = true;
            result += arr[i];
        }
        return result;
    }

    static BigInteger evaluate(String input) throws IllegalArgumentException {
        // implement here
        // parse input
        // using regex is allowed

        char firstOperator = ' ';
        char[] secondOperator = new char[2];
        String firstNumber = "";
        String secondNumber = "";
        int cursor = 0;

        // 1. 공백제거
        input = input.replaceAll("\\p{Z}", "");

        // 2. 첫번째 char가 부호인지 체크
        char first = input.charAt(0);
        if (first == '+' || first == '-') {
            firstOperator = first;
            cursor++;
        }
        int i = 0;
        // 3. 숫자, 부호, 숫자 순으로 배열에 하나씩 삽입
        while (true) {
            if (cursor >= input.length())
                break;
            char now = input.charAt(cursor);
            // now가 숫자가 아닌 경우
            if (now < 48 || now > 57) {
                secondOperator[i] = now;
                i++;
                cursor++;
            } else {
                // 부호가 한번도 나오지 않았으면 첫번째 숫자 체크 중
                if (i == 0) {
                    firstNumber += now;
                } else {
                    secondNumber += now;
                }
                cursor++;
            }
        }
        BigInteger num1 = new BigInteger(firstNumber);
        BigInteger num2 = new BigInteger(secondNumber);

        // 곱셈을 써야하는 경우
        if (secondOperator[0] == '*') {
            if (firstOperator == ' ' && secondOperator[1] == '\u0000') {
                return num1.multiply(num2);
            }
            if (firstOperator == ' ' && secondOperator[1] == '+') {
                return num1.multiply(num2);
            }
            if (firstOperator == ' ' && secondOperator[1] == '-') {
                String res = "-" + num1.multiply(num2).number;
                return new BigInteger(res);
            }
            if (firstOperator == '+' && secondOperator[1] == '\u0000') {
                return num1.multiply(num2);
            }
            if (firstOperator == '+' && secondOperator[1] == '+') {
                return num1.multiply(num2);
            }
            if (firstOperator == '+' && secondOperator[1] == '-') {
                String res = "-" + num1.multiply(num2).number;
                return new BigInteger(res);
            }
            if (firstOperator == '-' && secondOperator[1] == '\u0000') {
                String res = "-" + num1.multiply(num2).number;
                return new BigInteger(res);
            }
            if (firstOperator == '-' && secondOperator[1] == '+') {
                String res = "-" + num1.multiply(num2).number;
                return new BigInteger(res);
            }
            if (firstOperator == '-' && secondOperator[1] == '-') {
                return num1.multiply(num2);
            }
        }
        // 가운데가 덧셈인 경우
        if (secondOperator[0] == '+') {
            if (firstOperator == ' ' && secondOperator[1] == '\u0000') {
                return num1.add(num2);
            }
            if (firstOperator == ' ' && secondOperator[1] == '+') {
                return num1.add(num2);
            }
            if (firstOperator == ' ' && secondOperator[1] == '-') {
                String res = num1.subtract(num2).number;
                return new BigInteger(res);
            }
            if (firstOperator == '+' && secondOperator[1] == '\u0000') {
                return num1.add(num2);
            }
            if (firstOperator == '+' && secondOperator[1] == '+') {
                return num1.add(num2);
            }
            if (firstOperator == '+' && secondOperator[1] == '-') {
                String res = num1.subtract(num2).number;
                return new BigInteger(res);
            }
            if (firstOperator == '-' && secondOperator[1] == '\u0000') {
                String res = num2.subtract(num1).number;
                return new BigInteger(res);

            }
            if (firstOperator == '-' && secondOperator[1] == '+') {
                String res = num2.subtract(num1).number;
                return new BigInteger(res);

            }
            if (firstOperator == '-' && secondOperator[1] == '-') {
                String res = "-" + num1.add(num2).number;
                return new BigInteger(res);
            }
        }

        // 가운데가 뺄셈인 경우
        if (secondOperator[0] == '-') {
            if (firstOperator == ' ' && secondOperator[1] == '\u0000') {
                return num1.subtract(num2);
            }
            if (firstOperator == ' ' && secondOperator[1] == '+') {
                return num1.subtract(num2);
            }
            if (firstOperator == ' ' && secondOperator[1] == '-') {
                return num1.add(num2);
            }
            if (firstOperator == '+' && secondOperator[1] == '\u0000') {
                return num1.subtract(num2);
            }
            if (firstOperator == '+' && secondOperator[1] == '+') {
                return num1.subtract(num2);
            }
            if (firstOperator == '+' && secondOperator[1] == '-') {
                return num1.add(num2);
            }
            if (firstOperator == '-' && secondOperator[1] == '\u0000') {
                String res = "-" + num1.add(num2).number;
                return new BigInteger(res);
            }
            if (firstOperator == '-' && secondOperator[1] == '+') {
                String res = "-" + num1.add(num2).number;
                return new BigInteger(res);

            }
            if (firstOperator == '-' && secondOperator[1] == '-') {
                return num2.subtract(num1);
            }
        }
        return new BigInteger("0");
    }

    public static void main(String[] args) throws Exception {
        try (InputStreamReader isr = new InputStreamReader(System.in)) {
            try (BufferedReader reader = new BufferedReader(isr)) {
                boolean done = false;
                while (!done) {
                    String input = reader.readLine();

                    try { // input이 quit이었으면 done = true 아니면 false
                        done = processInput(input);
                    } catch (IllegalArgumentException e) {
                        System.err.println(MSG_INVALID_INPUT);
                    }
                }
            }
        }
    }

    static boolean processInput(String input) throws IllegalArgumentException {
        boolean quit = isQuitCmd(input);

        if (quit) {
            return true;
        } else {
            BigInteger result = evaluate(input);
            System.out.println(result.toString());
            return false;
        }
    }

    static boolean isQuitCmd(String input) { // input이 quit 이면 processInput에서 종료하도록 만들어줌
        return input.equalsIgnoreCase(QUIT_COMMAND);
    }
}
