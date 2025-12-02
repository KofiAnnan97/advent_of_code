import java.io.*;
import java.util.*;

public class GiftShop1 {
    static boolean isIDInvalid(long val){
        String valStr = Long.toString(val);
        if(valStr.length()%2 != 0) return false;
        String firstSegment = valStr.substring(0, valStr.length()/2);
        String secondSegment = valStr.substring(valStr.length()/2);
        //System.out.printf("%s v. %s\n", firstSegment, secondSegment);
        return firstSegment.equals(secondSegment);
    }
    static long getInvalidIDSum(String aStr, String bStr){
        long invalidSum = 0;
        long a = Long.parseLong(aStr);
        long b = Long.parseLong(bStr);
        for(long j= a; j <= b; j++){
            if(isIDInvalid(j)){
                invalidSum += j;
                //System.out.println(j);
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
        File file = new File(".\\data.txt");
        String line = "";
        try (Scanner scan = new Scanner(file)){
            line = scan.nextLine();
        } catch (FileNotFoundException e) {
            System.out.println("Could not find file.");
        }
        System.out.printf("Answer: %d",computeOutput(line));
        // Answer is 21898734247
    }
}
