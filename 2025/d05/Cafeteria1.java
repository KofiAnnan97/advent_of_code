import java.io.*;
import java.util.*;

public class Cafeteria1 { 
    static boolean isIDFresh(long id, long[] range){
        return id >= range[0] && id <= range[1];
    }
    
    static int computeOutput(ArrayList<String> lines){
        int i = 0;
        ArrayList<long[]> idRanges = new ArrayList<>();
        while(!lines.get(i).isEmpty()){
            String[] ids = lines.get(i).split("-");
            long lower = Long.parseLong(ids[0]);
            long upper = Long.parseLong(ids[1]);
            idRanges.add(new long[]{lower, upper});
            i++;
        }


        i+=1;
        ArrayList<Long> ingredientIds = new ArrayList<>();
        for(int j=i ; j < lines.size(); j++){
            ingredientIds.add(Long.parseLong(lines.get(j))); 
        }
        //Collections.sort(ingredientIds);
        
        int sum = 0;
        for(long id: ingredientIds){
            for(long[] range: idRanges){
                //System.out.printf("%s -> %s\n", id, Arrays.toString(range));
                if(isIDFresh(id, range)){
                    sum++;
                    break;
                } 
            }
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
        // Answer is 638
    }
}