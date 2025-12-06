import java.io.*;
import java.util.*;

public class TrashCompactor1 {   
    static long computeOutput(ArrayList<String> lines){
        ArrayList<ArrayList<Long>> numCols = new ArrayList<>();
        for(int n = 0; n < lines.get(0).length(); n++)
            numCols.add(new ArrayList<>());
        String[] operators = lines.get(lines.size()-1).split("\s+");
        for(int i = 0; i < lines.size()-1; i++){
            String[] components = lines.get(i).trim().split("\s+");
            //System.out.printf("Components: %s\n", Arrays.toString(components));
            for(int j = 0; j < components.length; j++){
                numCols.get(j).add(Long.parseLong(components[j]));
            }
        }
        long total = 0;
        for(int k = 0; k < operators.length; k++){
            long subTotal = 0;
            for(long val: numCols.get(k)) {
                switch(operators[k]){
                    case "*":
                        if(subTotal == 0) subTotal++;
                        subTotal *= val;
                        break;
                    case "+":
                        subTotal += val;
                        break;
                    default:
                        break;
                }
            }
            //System.out.println("SubTotal: " + subTotal);
            total += subTotal;
        }
        return total;
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
        // Answer is 3785892992137
    }
}