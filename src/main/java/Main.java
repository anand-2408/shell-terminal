import java.util.Scanner;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        String input, typeSubString;
        String[] commands = {"echo", "exit", "type"};

        while(true){
            System.out.print("$ ");

            input =scanner.nextLine();

            if(input.equals("exit 0")){
                break;
            }
            else if(input.startsWith("echo ")){
                System.out.println(input.subString(5));
            }
            else if(input.startsWith("type ")){
                typeSubString = input.subString(5);
                if (Arrays.asList(commands).contains(typeSubstring)) {
                    System.out.println(typeSubstring + " is a shell builtin");
                }
                else {
                    System.out.println(typeSubstring + " not found");
                }
            }
            else{
                System.out.println(input + ": command not found");
            }

        }
    }
    //scanner.close();
}
