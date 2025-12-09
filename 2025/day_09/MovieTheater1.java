import java.io.*;
import java.util.*;

public class MovieTheater1 {  
    
    static long getArea(long[]a, long[]b){
        return Math.abs(a[0]-b[0]+1) * Math.abs(a[1] - b[1]+1);
    }

    static ArrayList<long[]> getPts(ArrayList<String> lines){
        ArrayList<long[]> pts = new ArrayList<>();
        for(String line: lines){
            String[] ptStrArr = line.split(",");
            long x = Long.parseLong(ptStrArr[0]);
            long y = Long.parseLong(ptStrArr[1]);
            pts.add(new long[]{x, y});
        }
        return pts;
    }
    
    static long computeOutput(ArrayList<String> lines){
        long largestRect = 0;
        ArrayList<long[]> pts = getPts(lines);
        for(int i = 0; i < pts.size(); i++){
            for(int j = 0; j < pts.size(); j++){
                if(i == j){
                    continue;
                }
                else{
                    long area = getArea(pts.get(i), pts.get(j));
                    if(area > largestRect) largestRect = area;
                }
            }
        }
        return largestRect;
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
        System.out.printf("Answer: %d\n",computeOutput(lines));
        // Answer is 4758121828
    }
}