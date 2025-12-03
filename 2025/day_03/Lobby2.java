import java.io.*;
import java.util.*;

public class Lobby2 {   
    static long computeOutput(ArrayList<String> lines){
        long sum = 0;
        for(String line: lines){
            int batteriesCount = 12;
            int maxIdx = -1;
            String largestNumStr = "";
            while(batteriesCount > 0){
                int currentMax = 0;
                for(int i = maxIdx+1; i < line.length()-batteriesCount+1; i++){
                    int val = Integer.parseInt(line.substring(i, i+1));
                    //System.out.printf("%d : %s\n", i, val);
                    if(val > currentMax){
                        currentMax = val;
                        maxIdx = i;
                    }
                }
                largestNumStr += currentMax;
                batteriesCount--;
            }
            //System.out.printf("%s -> %s\n", line, largestNumStr);
            sum += Long.parseLong(largestNumStr);
        }       
        return sum;
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
        System.out.printf("Answer: %d",computeOutput(lines));
        // Answer is 171528556468625
    }
}