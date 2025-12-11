import java.io.*;
import java.util.*;

public class TrashCompactor2 { 
    static char EMPTY_CHAR = ' ';
    static char ADD ='+';
    static char MULTIPLY = '*';
    
    static long computeOutput(ArrayList<String> lines){
        long total = 0;
        char operator = EMPTY_CHAR;
        ArrayList<Long> numColsSingle = new ArrayList<>();
        for(int col = lines.get(0).length()-1; col >= 0; col--){
            String numStr = "";
            for(int row = 0; row < lines.size(); row++){
                //System.out.println(lines.get(row));
                char valStr = lines.get(row).charAt(col);
                if(valStr == ADD || valStr == MULTIPLY) operator = valStr;
                else if(valStr != EMPTY_CHAR) numStr += valStr;
            }
            //System.out.printf("numStr: \'%s\'\n", numStr);
            if(!numStr.isEmpty()) numColsSingle.add(Long.parseLong(numStr));
            //System.out.printf("Nums to operate on: %s\n", numColsSingle.toString());
            if(operator != EMPTY_CHAR){
                long subTotal = 0;
                for(long num: numColsSingle){
                    if(operator == MULTIPLY){
                        if(subTotal == 0) subTotal++;
                        subTotal *= num;
                    }
                    else if(operator == ADD) subTotal += num;
                }
                total += subTotal;
                operator = EMPTY_CHAR;
                numColsSingle.clear();
            }
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
        // Answer is 7669802156452
    }
}