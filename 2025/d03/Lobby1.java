import java.io.*;
import java.util.*;

public class Lobby1 {   
    static long computeOutput(ArrayList<String> lines){
        long sum = 0;
        for(String line: lines){
            String largestNumStr = "";
            int currentMax = 0;
            int maxIdx = 0;
            for(int i = 0; i < line.length()-1; i++){
                int val = Integer.parseInt(line.substring(i, i+1));
                if(val > currentMax){
                    currentMax = val;
                    maxIdx = i;
                }
            }
            largestNumStr += currentMax;
            currentMax = 0;
            for(int j = maxIdx+1; j < line.length(); j++){
                int val = Integer.parseInt(line.substring(j, j+1));
                currentMax = val > currentMax ? val : currentMax;
            }
            largestNumStr += currentMax;
            //System.out.printf("%s -> %s\n", line, largestNumStr);
            sum += Long.parseLong(largestNumStr);
        }       
        return sum;
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
        System.out.printf("Answer: %d",computeOutput(lines));
        // Answer is 17278
    }
}