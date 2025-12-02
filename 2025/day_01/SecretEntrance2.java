import java.io.*;
import java.util.*;

public class SecretEntrance2 {
    static int[] zerosFromTicks(char dirChar, int tickNum, int dial){
        int counter = 0;
        int dirVal = dirChar == 'L' ? -1 : 1;
        while(tickNum != 0){
            dial+=dirVal;
            if(dial < 0) dial+=100;
            else if(dial > 99) dial-=100;
            if(dial==0) counter++;
            tickNum--;
        }
        return new int[]{counter, dial};
    }

    static int getPassword(ArrayList<String> lines){
        int dial = 50;
        int zeros_num = 0;
        //System.out.println("Start => " + dial);
        for(int i = 0; i < lines.size(); i++){
            char direction = lines.get(i).charAt(0);
            int val = Integer.parseInt(lines.get(i).substring(1));
            int initDial = dial;
            int[] state = zerosFromTicks(direction, val, dial);
            zeros_num += state[0];
            dial = state[1];
            //System.out.printf("%s => %d\n", lines.get(i), dial);
        }
        return zeros_num;
    }
    public static void main(String[] args){
        File file = new File(".\\data.txt");
        ArrayList<String> lines = new ArrayList<>();
        try (Scanner scan = new Scanner(file)){
            while(scan.hasNextLine())
                lines.add(scan.nextLine());
        } catch (FileNotFoundException e) {
            System.out.println("Could not find file.");
        }
        System.out.printf("Answer: %d",getPassword(lines));
        // Answer is 6634
    }
}