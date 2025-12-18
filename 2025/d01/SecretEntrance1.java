import java.io.*;
import java.util.*;

public class SecretEntrance1 {
    static int getPassword(ArrayList<String> lines){
        int dial = 50;
        int zeros_num = 0;
        for(int i = 0; i < lines.size(); i++){
            char direction = lines.get(i).charAt(0);
            int val = Integer.parseInt(lines.get(i).substring(1));
            if(direction == 'L') dial -= val;
            else dial += val;
            dial = ((dial % 100) + 100) % 100;
            //System.out.printf("%s => %d\n", lines.get(i), dial);
            if(dial == 0) zeros_num++;
        }
        return zeros_num;
    }
    public static void main(String[] args){
        File file = new File("data.txt");
        ArrayList<String> lines = new ArrayList<>();
        try (Scanner scan = new Scanner(file)){
            while(scan.hasNextLine())
                lines.add(scan.nextLine());
        } catch (FileNotFoundException e) {
            System.out.println("Could not find file.");
        }
        System.out.printf("Answer: %d",getPassword(lines));
        // Answer is 1141
    }
}