import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BigInteger
{
    public static final String QUIT_COMMAND = "quit";
    public static final String MSG_INVALID_INPUT = "입력이 잘못되었습니다.";

    int[] value = new int[201]; // 들어온 문자열 숫자를 저장할 배열
    int size; // 숫자의 자리 수
    boolean isMinus;
    boolean isStartWithOperator;

    public BigInteger(String s) {
        isMinus = s.startsWith("-"); // 음수인지 아닌지 체크 -> 음수라면 true 양수라면 false
        isStartWithOperator = (s.startsWith("-") || s.startsWith("+"));
        this.size = isStartWithOperator ? s.length() - 1 : s.length();

        // 문자열을 배열에 저장
        if (!isStartWithOperator) {
            for (int i = 0; i < s.length(); i++) {
                value[i] = s.charAt(i) - '0';
            }
        } else {
            for (int i = 1; i < s.length(); i++) {
                value[i - 1] = s.charAt(i) - '0';
            }
        }
    }

    //== 더하기 ==//
    public BigInteger add(BigInteger big) {
        // 둘 다 양수인 경우
        if (!this.isMinus && !big.isMinus) {
            return addPositiveValues(this, big);
        }
        // 둘다 음수인 경우
        else if (this.isMinus && big.isMinus) {
            BigInteger result = addPositiveValues(this, big);
            result.isMinus = true;
            return result;
        }
        // 하나만 음수인 경우
        else if (this.isMinus) {
            BigInteger num = new BigInteger(this.toString().substring(1));
            return big.subtract(num);
        }
        else {
            BigInteger num = new BigInteger(big.toString().substring(1));
            return this.subtract(num);
        }
    }

    // 둘 다 양수인 경우 덧셈
    public BigInteger addPositiveValues(BigInteger num1, BigInteger num2) {
        int[] reverseNum1 = getReverseArray(num1.value, num1.size);
        int[] reverseNum2 = getReverseArray(num2.value, num2.size);
        int[] result = new int[201];
        int carry = 0;

        // 뒤에서부터 한자리씩 덧셈
        for (int i = 0; i < 201; i ++) {
            int sum = reverseNum1[i] + reverseNum2[i] + carry;
            result[i] = sum % 10;
            if (sum >= 10) carry = 1;
            else carry = 0;
        }

        int size = getSize(result);
        String resultString = getResultString(result, size);

        return new BigInteger(resultString);
    }



    //== 빼기 ==//
    public BigInteger subtract(BigInteger big) {
        if (!this.isMinus && big.isMinus) {
            // 음수부 제거하고 덧셈시키기
            BigInteger num = new BigInteger(big.toString().substring(1));
            return this.add(num);
        }
        else if (this.isMinus && !big.isMinus) {
            BigInteger num = new BigInteger(this.toString().substring(1));
            return new BigInteger("-" + num.add(big).toString());
        }
        else if (!this.isMinus && !big.isMinus) {
            if (findBigger(this, big) > 0) {
                return substractByBiggerToSmaller(this, big);
            } else {
                return new BigInteger("-" + substractByBiggerToSmaller(big, this).toString());
            }
        }
        else {
            BigInteger num = new BigInteger(big.toString().substring(1));
            return this.add(num);
        }
    }

    private BigInteger substractByBiggerToSmaller(BigInteger num1, BigInteger num2) {
        int[] reverseNum1 = getReverseArray(num1.value, num1.size);
        int[] reverseNum2 = getReverseArray(num2.value, num2.size);
        int[] result = new int[201];
        int carry = 0;

        // 뒤에서부터 한자리씩 뺄셈
        for (int i = 0; i < 201; i ++) {
            int sub = reverseNum1[i] - reverseNum2[i] + carry;
            if (sub < 0) {
                carry = -1;
                result[i] = 10 + sub;
            } else {
                carry = 0;
                result[i] = sub;
            }
        }

        int size = getSize(result);
        String resultString = getResultString(result, size);

        return new BigInteger(resultString);
    }

    public BigInteger multiply(BigInteger big) {
        if ((this.isMinus && big.isMinus) || (!this.isMinus && !big.isMinus)) {
            return multiplyPositiveValues(this, big);
        } else {
            return new BigInteger("-" + multiplyPositiveValues(this, big).toString());
        }
    }

    private BigInteger multiplyPositiveValues(BigInteger num1, BigInteger num2) {
        int[] reverseNum1 = getReverseArray(num1.value, num1.size);
        int[] reverseNum2 = getReverseArray(num2.value, num2.size);
        BigInteger resultBigInteger = new BigInteger("0");
        int carry = 0;

        for (int i = 0; i < num1.size; i++) {
            int[] res = new int[201];
            for (int j = 0; j < num2.size; j++) {
                int mul = reverseNum1[i] * reverseNum2[j] + carry;
                int now = mul % 10;
                carry = mul / 10;
                res[i + j] = now;
            }

            if (carry > 0) {
                res[i + num2.size] = carry;
                carry = 0;
            }

            int size = getSize(res);
            String resultString = getResultString(res, size);

            resultBigInteger = resultBigInteger.add(new BigInteger(resultString));
        }
        return resultBigInteger;
    }

    // 배열 뒤집기
    private int[] getReverseArray(int[] arr, int size) {
        int[] reverseArray = new int[201];
        for (int i = 0; i < size; i++) {
            reverseArray[i] = arr[size - i - 1];
        }
        return reverseArray;
    }

    // 배열의 사이즈 구하기
    // "1234"인 경우 result 배열은 [1,2,3,4,0,0,0,...] 인데 이때 size = 4
    private int getSize(int[] result) {
        int size = 0;
        for (int i = 200; i >= 0; i--) {
            if (result[i] != 0) {
                size = i;
                break;
            }
        }
        return size;
    }

    // result 배열 -> 문자열로
    // [1,2,3,4] -> "1234"
    private String getResultString(int[] result, int size) {
        String resultString = "";
        for (int i = size; i >= 0; i--) resultString += result[i];
        return resultString;
    }

    // num1이 크면 양수, num2가 크면 음수 리턴, 같으면 1 리턴
    private int findBigger(BigInteger num1, BigInteger num2) {
        if (num1.size != num2.size) return num1.size - num2.size;
        else {
            for (int i = 0; i < size; i++) {
                if (num1.value[i] == num2.value[i]) continue;
                if (num1.value[i] > num2.value[i]) return 1;
                else return -1;
            }
        }
        return 1;
    }

    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < size; i ++) result += value[i];
        if (isMinus) result = "-" + result;
        return result;
    }

    static BigInteger evaluate(String input) throws IllegalArgumentException {
        Pattern pattern = Pattern.compile("(?<num1>[\\+\\-]?[0-9]+)(?<operator>[\\+|\\-|\\*])(?<num2>[\\+\\-]?[0-9]+)");
        input = input.replaceAll(" ", "");

        Matcher m = pattern.matcher(input);
        m.find();

        BigInteger num1 = new BigInteger(m.group("num1"));
        BigInteger num2 = new BigInteger(m.group("num2"));
        String operator = m.group("operator");

        BigInteger result;

        switch(operator) {
            case "+":
                result = num1.add(num2);
                break;
            case "-":
                result = num1.subtract(num2);
                break;
            case "*":
                result = num1.multiply(num2);
                break;
            default:
                result = null;
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        try (InputStreamReader isr = new InputStreamReader(System.in))
        {
            try (BufferedReader reader = new BufferedReader(isr))
            {
                boolean done = false;
                while (!done)
                {
                    String input = reader.readLine();

                    try
                    {
                        done = processInput(input);
                    }
                    catch (IllegalArgumentException e)
                    {
                        System.err.println(MSG_INVALID_INPUT);
                    }
                }
            }
        }
    }

    static boolean processInput(String input) throws IllegalArgumentException {
        boolean quit = isQuitCmd(input);

        if (quit)
        {
            return true;
        }
        else
        {
            BigInteger result = evaluate(input);
            System.out.println(result.toString());

            return false;
        }
    }

    static boolean isQuitCmd(String input)
    {
        return input.equalsIgnoreCase(QUIT_COMMAND);
    }
}
