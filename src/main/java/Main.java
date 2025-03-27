import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("$ ");
        while(true){
            String input =scanner.nextLine();
            System.out.println(input + ": command not found");
            System.out.print("$ ");

        }


    }
    //scanner.close();
}
