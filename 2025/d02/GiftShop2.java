import java.io.*;
import java.util.*;

public class GiftShop2 {
    static boolean isIDInvalid(long val){
        boolean doesMatch = true;
        String valStr = Long.toString(val);
        int segmentSize = 1;
        while(segmentSize <= valStr.length()/2){
            String checkSegment = valStr.substring(0, segmentSize);
            doesMatch = true;
            for(int i = segmentSize; i < valStr.length(); i+=segmentSize){
                int endIdx = i+segmentSize < valStr.length() ? i+segmentSize : valStr.length();
                String nextSegment = valStr.substring(i, endIdx);
                //System.out.printf("%s == %s\n", checkSegment, nextSegment);
                if(!checkSegment.equals(nextSegment)){
                    doesMatch = false;
                    break;
                }
            }
            if(doesMatch) return true;
            segmentSize++;
        }
        return false;
    }
    static long getInvalidIDSum(String aStr, String bStr){
        long invalidSum = 0;
        long a = Long.parseLong(aStr);
        long b = Long.parseLong(bStr);
        for(long j= a; j <= b; j++){
            if(isIDInvalid(j)){
                invalidSum += j;
                //System.out.println("Invalid: " + j);
            }
        }
        return invalidSum;
    }
    static long computeOutput(String line){
        long sum = 0;
        String[] data = line.split(",");
        //System.out.println(Arrays.toString(data));
        for(int i = 0; i < data.length; i++){
            String[] vals = data[i].split("-");
            //System.out.println(Arrays.toString(vals));
            sum += getInvalidIDSum(vals[0], vals[1]);
        }
        return sum;
    }
    public static void main(String[] args){
        File file = new File("data.txt");
        String line = "";
        try (Scanner scan = new Scanner(file)){
            line = scan.nextLine();
        } catch (FileNotFoundException e) {
            System.out.println("Could not find file.");
        }
        System.out.printf("Answer: %d",computeOutput(line));
        // Answer is 28915664389
    }
}
