import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class Factory1 {
    static char IND_ON = '#';
    static char IND_OFF = '.';

    static class Machine{
        ArrayList<Integer> goalIndicators;
        ArrayList<ArrayList<Integer>> schematics;
        ArrayList<Integer> joltage;

        Machine(){
            goalIndicators = new ArrayList<>();
            schematics = new ArrayList<>();
            joltage = new ArrayList<>();
        }

        @Override
        public String toString(){
            String data = "{Indicator: " + goalIndicators;
            data += ", Schematics: " + schematics;
            data += ", Joltages: " + joltage + "}";
            return data;
        }
    }

    static ArrayList<Machine> getMachines(ArrayList<String> lines){
        ArrayList<Machine> machines = new ArrayList<>();
        for(String line: lines){
            //System.out.println(line);
            Machine m = new Machine();
            String segments[] = line.split(" ");
            String indicatorStr = segments[0].substring(1, segments[0].length()-1);
            for(char c: indicatorStr.toCharArray()){
                if(c == IND_OFF) m.goalIndicators.add(1);
                else if(c == IND_ON) m.goalIndicators.add(0);
            }
            for(int i = 1; i < segments.length-2; i++){
                String schematic = segments[i].substring(1, segments[i].length()-1);
                String[] schematicStr = schematic.split(",");
                ArrayList<Integer> vals = new ArrayList<>();
                for(String s: schematicStr){
                    if(!s.isEmpty()) vals.add(Integer.parseInt(s));
                } 
                m.schematics.add(vals);
            }
            String joltageStr = segments[segments.length-1].substring(1, segments[segments.length-1].length()-1);
            String[] joltages = joltageStr.split(",");
            for(String j: joltages) m.joltage.add(Integer.parseInt(j));
            machines.add(m);
        }
        return machines;
    }

    static void pressButton(ArrayList<Boolean> indicators, ArrayList<Integer> input){
        for(int val: input){
            indicators.set(val, !indicators.get(val));
        }
    }

    static void swapRow(ArrayList<ArrayList<Integer>> matrix, int i, int j){
        ArrayList<Integer>  temp = matrix.get(i);
        matrix.set(i, matrix.get(j));    
        matrix.set(j, temp);
    }

    static void swapCol(ArrayList<ArrayList<Integer>> matrix, int i, int j){
        for(int k = 0; k < matrix.size(); k++){
            int temp = matrix.get(k).get(i);
            matrix.get(k).set(i, matrix.get(k).get(j));
            matrix.get(k).set(j, temp);
        }
    }

    static int solve(ArrayList<ArrayList<Integer>> matrix, ArrayList<ArrayList<Integer>> schematics, ArrayList<Integer> goal){
        
        int n = goal.size();
        for(int i = 0; i < n; i++){
            int max = i;
            for(int j = i+1; j < n; j++){
                if(Math.abs(matrix.get(j).get(i)) > Math.abs(matrix.get(max).get(i))){
                    max = j;
                }
            }
            swapRow(matrix, i, max);

            ArrayList<Integer> temp = schematics.get(i);
            schematics.set(i, schematics.get(max));
            schematics.set(max, temp);


        }

        
        return -1;
    }

    static void printMatrix(ArrayList<ArrayList<Integer>> matrix){
        for(ArrayList<Integer> m: matrix) System.out.println(m);
    }

    static int getFewestTotalPresses(Machine m){
        ArrayList<Integer> initIndicators = new ArrayList<>(Collections.nCopies(m.goalIndicators.size(), 0));
        ArrayList<Integer> goal = m.goalIndicators;
        ArrayList<ArrayList<Integer>> matrix = new ArrayList<>();
        for(int o = 0; o < m.goalIndicators.size(); o++){
            matrix.add(new ArrayList<Integer>());
            for(int p = 0; p < m.schematics.size(); p++) matrix.get(o).add(0);
        }

        for(int j = 0; j < m.schematics.size(); j++){
            for(int val: m.schematics.get(j)){
                matrix.get(val).set(j, 1);
            }
        }
        printMatrix(matrix);
        
        return 0;
    }

    static int computeOutput(ArrayList<String> lines){
        ArrayList<Machine> machines = getMachines(lines);
        System.out.println(machines);
        int sum = 0;
        //for(Machine m: machines)
            sum += getFewestTotalPresses(machines.get(0));
        return sum;
    }
    public static void main(String[] args){
        File file = new File("sample.txt");
        ArrayList<String> lines = new ArrayList<>();
        try (Scanner scan = new Scanner(file)){
            while(scan.hasNextLine())
                lines.add(scan.nextLine());
        } catch (FileNotFoundException e) {
            System.out.println("Could not find file.");
        }
        System.out.printf("Answer: %d\n",computeOutput(lines));
        // Answer is
    }
}