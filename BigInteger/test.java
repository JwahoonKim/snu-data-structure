import java.util.Arrays;

public class test {
    public static void main(String[] args) {
        // char[] b = { '1', '2', '3' };
        // char[] c = { '1', '2', '4' };
        // int[] d = new int[3];
        // for (int i = 0; i < 3; i++) {
        // d[i] = (b[i] - '0') + (c[i] - '0');
        // }
        // for (int e : d) {
        // System.out.println(e);
        // }

        int resultSize = Math.max(3, 4) + 1;
        int[] resultArr = new int[5];
        int nowTmp = 0;
        int nextTmp = 0;
        char[] numArr = new char[101];
        char[] numArr2 = new char[101];
        String result = "";
        numArr[0] = '8';
        numArr[1] = '5';
        numArr[2] = '6';
        for (int i = 3; i < 5; i++) {
            numArr[i] = '0';
        }
        numArr2[0] = '1';
        numArr2[1] = '2';
        numArr2[2] = '7';
        numArr2[3] = '9';
        numArr2[4] = '3';
        for (int i = 5; i < resultSize; i++) {
            numArr2[i] = '0';
        }
        for (int i = 0; i < resultSize; i++) {
            int sum = (numArr[i] - '0') + (numArr2[i] - '0');

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

        System.out.println(result);

        System.out.println("");
    }
}
