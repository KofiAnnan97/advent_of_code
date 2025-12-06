import java.io.*;
import java.util.*;

public class Cafeteria2 { 
    static long[] truncateRange(long[] range1, long[] range2){
        long lower = (range1[0] < range2[0]) ? range1[0] : range2[0];
        long upper = (range1[1] > range2[1]) ? range1[1] : range2[1];
        return new long[]{lower, upper};
    }

    static boolean doRangesIntersect(long[] range1, long[] range2){
        return (
            (range1[0] <= range2[0] && range2[0] <= range1[1]) || 
            (range1[0] <= range2[1] && range2[1] <= range1[1]) ||
            (range2[0] <= range1[0] && range1[0] <= range2[1]) ||
            (range2[0] <= range1[1] && range1[1] <= range2[1])
        );
    }
    
    static long computeOutput(ArrayList<String> lines){
        int i = 0;
        ArrayList<long[]> idRanges = new ArrayList<>();
        while(!lines.get(i).isEmpty()){
            String[] ids = lines.get(i).split("-");
            long lower = Long.parseLong(ids[0]);
            long upper = Long.parseLong(ids[1]);
            idRanges.add(new long[]{lower, upper});
            i++;
        }

        ArrayList<long[]> uniqueIdRanges = new ArrayList<>();
        int uIdx = 0;
        int currSize = idRanges.size();
        while(!idRanges.isEmpty()){
            int lastIdx = idRanges.size()-1;
            //System.out.printf("Current: %s\n",Arrays.toString(idRanges.getLast()));
            if(!uniqueIdRanges.isEmpty()) uIdx++;//{
            uniqueIdRanges.add(idRanges.get(lastIdx));
            idRanges.remove(lastIdx);
            for(int k = idRanges.size()-1; k >=0; k--){
                boolean intersects = doRangesIntersect(uniqueIdRanges.get(uIdx), idRanges.get(k));
                //System.out.printf("%s U %s -> %s\n",Arrays.toString(uniqueIdRanges.get(uIdx)), Arrays.toString(idRanges.get(k)), intersects);
                if(intersects){
                    uniqueIdRanges.set(uIdx, truncateRange(uniqueIdRanges.get(uIdx), idRanges.get(k)));
                    //System.out.printf("New: %s\n", Arrays.toString(uniqueIdRanges.get(uIdx)));
                    idRanges.remove(k);
                }
            }
            if(idRanges.isEmpty() && currSize > uniqueIdRanges.size()){
                idRanges = new ArrayList<>(uniqueIdRanges);
                currSize = idRanges.size();
                uniqueIdRanges.clear();
                uIdx = 0;
            }        
        }
        System.out.println("Unique Ranges:");
        long freshIdSum = 0;
        for(long[] range: uniqueIdRanges){
            System.out.println(Arrays.toString(range));
            freshIdSum += (range[1]-range[0]+1);
        }
        return freshIdSum;
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
        // Answer is 352946349407338
    }
}