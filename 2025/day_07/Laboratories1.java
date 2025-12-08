import java.io.*;
import java.util.*;

public class Laboratories1 {   
    static char START = 'S';
    static char BEAM = '|';
    static char SPLITTER = '^';

    static void printManifold(ArrayList<String> lines){
        System.out.println("\nCurrent Manifold:");
        for(String line: lines) System.out.println(line);
    }

    static long computeOutput(ArrayList<String> lines){
        long splits = 0;
        Set<Integer> newBeams = new HashSet<>();
        Set<Integer> currBeams = new HashSet<>();

        int startIdx = -1;
        for(int s = 0; s < lines.get(0).length(); s++){
            if(lines.get(0).charAt(s) == START){
                startIdx = s;
                break;
            }
        }
        currBeams.add(startIdx);

        for(int i = 1; i < lines.size(); i++){
            StringBuilder line = new StringBuilder(lines.get(i));
            //System.out.printf("Current: %s\n", line.toString());
            if(!currBeams.isEmpty()){
                for(int lineIdx: currBeams){
                    //System.out.println("Line idx: " + lineIdx);
                    if(lineIdx < 0 && lineIdx >= line.length()) continue;
                    else if(line.charAt(lineIdx) == SPLITTER){
                        //System.out.printf("%s -> %s\n", lineIdx, line.charAt(lineIdx));
                        int left = lineIdx - 1;
                        int right = lineIdx + 1;
                        if(left >= 0 && line.charAt(left) != BEAM && line.charAt(left) != SPLITTER){
                            line.setCharAt(left, BEAM); 
                            newBeams.add(left);              
                        }
                        if(right < line.length() && line.charAt(right) != BEAM && line.charAt(right) != SPLITTER){
                            line.setCharAt(right, BEAM);
                            newBeams.add(right);
                        }
                        splits++;
                    }
                    else {
                        line.setCharAt(lineIdx, BEAM);
                        newBeams.add(lineIdx);
                    }
                }
                lines.set(i, line.toString());
            }
            currBeams = new HashSet<>(newBeams);
            newBeams.clear(); 
            //System.out.printf("    New: %s\n", line.toString());
            //printManifold(lines);
        }
        return splits;
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
        // Answer is 1638
    }
}