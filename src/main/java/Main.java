import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.print("$ ");
            String input =scanner.nextLine().trim();
            if(input.isEmpty()){
                continue;
            }
            if(input.equalsIgnoreCase("exit")){
                break;
            }
        }

        System.out.println(input + ": command not found");
    }
    scanner.close();
}
