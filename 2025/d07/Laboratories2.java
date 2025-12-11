import java.io.*;
import java.util.*;

public class Laboratories2 {
    static char START = 'S';
    static char BEAM = '|';
    static char SPLITTER = '^';

    static void printManifold(ArrayList<String> lines){
        System.out.println("\nCurrent Manifold:");
        for(String line: lines) System.out.println(line);
    }

    static int getStartIdx(ArrayList<String> lines){
        int startIdx = -1;
        for(int s = 0; s < lines.get(0).length(); s++){
            if(lines.get(0).charAt(s) == START){
                startIdx = s;
                break;
            }
        }
        return startIdx;
    }

    static ArrayList<String> getSplitterRows(ArrayList<String> lines){
        ArrayList<String> splitterRows = new ArrayList<>();
        splitterRows.add(lines.get(0));
        for(String line: lines){
            for(int j = 0; j < line.length(); j++){
                if(line.charAt(j) == SPLITTER) {
                    splitterRows.add(line);
                    break;
                }
            }
        }
        return splitterRows;
    }
   
    // TOO SLOW
    static long computeOutputRecursive(ArrayList<String> beams, int row, int col){
        if(row >= beams.size()) return 1L;
        if(col < 0 || col >= beams.get(row).length()) return 0L;
        if(beams.get(row).charAt(col) == SPLITTER) {
            long leftPaths = computeOutputRecursive(beams, row+1, col-1);
            long rightPaths = computeOutputRecursive(beams, row+1, col+1);
            return leftPaths + rightPaths;
        }
        return computeOutputRecursive(beams, row+1, col);
    }

    // TOO SLOW
    static long computeOutputIteratively(ArrayList<String> beams, int startIdx){
        long paths = 0;
        Stack<int[]> q = new Stack<>();
        q.push(new int[]{1, startIdx});
        while(!q.isEmpty()){
            int[] curr = q.pop();
            int row = curr[0];
            while(row < beams.size()){
                if(beams.get(row).charAt(curr[1]) != SPLITTER) {
                    row++;
                }
                else break;
                //System.out.printf("Going down: (%d,%d)\n" , row, curr[1]);
            }
            if(row+1 >= beams.size()) paths++;
            else{
                int[] new_pt = new int[]{row+1, curr[1]};
                //System.out.printf("(%d,%d)\n", new_pt[0], new_pt[1]);
                if(new_pt[1] >= 0) q.push(new int[]{new_pt[0], new_pt[1]-1});
                if(new_pt[1] < beams.get(new_pt[0]).length()) q.push(new int[]{new_pt[0], new_pt[1]+1});
            }
        }
        return paths;
    }
   
    static long computeOutput(ArrayList<String> lines){
        //int startIdx = getStartIdx(lines);
        //long paths = computeOutputIteratively(lines, startIdx);
        //long paths = computeOutputRecursive(lines, 1, startIdx);
        
        long paths = 0;
        ArrayList<String> splitterRows = getSplitterRows(lines);
        ArrayList<Long> pathNums = new ArrayList<>(Collections.nCopies(splitterRows.get(0).length(), 0L));
        for(int k = 0; k < splitterRows.size(); k++){
            String line = splitterRows.get(k);
            for(int l = 0; l < line.length(); l++){
                if(line.charAt(l) == START) 
                    pathNums.set(l, pathNums.get(l)+1);
                else if(line.charAt(l) == SPLITTER && l-1 >= 0 && l+1 < line.length()){
                    pathNums.set(l-1, pathNums.get(l-1)+pathNums.get(l));
                    pathNums.set(l+1, pathNums.get(l+1)+pathNums.get(l));
                    pathNums.set(l, 0L);
                }
            }
            //System.out.println(Arrays.toString(line.toCharArray()));
            //System.out.println(pathNums);
        }
        for(Long num: pathNums) paths+=num;
        return paths;
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
        System.out.printf("Answer: %d\n", computeOutput(lines));
        // Answer is 7759107121385
    } 
}
