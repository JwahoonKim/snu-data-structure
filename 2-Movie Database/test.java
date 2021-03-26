import java.util.Arrays;

public class test {
    public static void main(String[] args) {
        String input = "        INSERT   %ACTION%            %BATMAN BEGINS%";
        String[] arr = input.replaceAll(" ", "").split(" *% *%? *");
        System.out.println(arr[0]);
        System.out.println(arr[1]);
        System.out.println(arr[2]);
        System.out.println(Arrays.toString(arr));
    }
}
