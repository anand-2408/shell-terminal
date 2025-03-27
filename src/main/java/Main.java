import java.util.Scanner;
import javax.swing.plaf.TreeUI;

public class Main {
    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);

        while(true){
            System.out.print("$ ");

            String input =scanner.nextLine();

            if(input.equals("exit 0")){
                break;
            }
            else if(input.startsWith("echo")){
                System.out.println(input.subString(input.indexOf("echo ")+5));
            }
            else{
                System.out.println(input + ": command not found");
            }

        }


    }
    //scanner.close();
}
