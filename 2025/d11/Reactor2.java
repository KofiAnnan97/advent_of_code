import java.io.*;
import java.util.*;

public class Reactor2 {   
    static String START = "svr";
    static String DAC = "dac";
    static String FFT = "fft";
    static String GOAL = "out";

    static HashMap<String, HashSet<String>> getGraph(ArrayList<String> lines){
        HashMap<String, HashSet<String>> graph = new HashMap<>();
        for(String line: lines){
            String[] segments = line.split(" ");
            String key = segments[0].substring(0, segments[0].length()-1);
            graph.put(key, new HashSet<>());
            for(int i = 1; i < segments.length; i++) 
                graph.get(key).add(segments[i]);
        }
        return graph;
    }

    static long getNumOfPathsRecursively(HashMap<String, HashSet<String>> graph, String curr, String goal, HashMap<String, Long> state){
        if(!goal.equals(GOAL) && curr.equals(GOAL)) return 0;
        else if(curr.equals(goal)) return 1;
        else if (state.containsKey(curr)) return state.get(curr);
        else{
            long paths = 0;
            for(String val: graph.get(curr))
                paths += getNumOfPathsRecursively(graph, val, goal, state);
            state.put(curr, paths);
            return paths;
        }
    }

    static long computeOutput(ArrayList<String> lines){
        HashMap<String, HashSet<String>> graph = getGraph(lines);
        long startToDac = getNumOfPathsRecursively(graph, START, DAC, new HashMap<>());
        long dacToFft = getNumOfPathsRecursively(graph, DAC, FFT, new HashMap<>()); 
        long fftToGoal = getNumOfPathsRecursively(graph, FFT, GOAL, new HashMap<>()); 
        long startToFft = getNumOfPathsRecursively(graph, START, FFT, new HashMap<>()); 
        long fftToDac = getNumOfPathsRecursively(graph, FFT, DAC, new HashMap<>()); 
        long dacToGoal = getNumOfPathsRecursively(graph, DAC, GOAL, new HashMap<>());
        return (startToDac*dacToFft*fftToGoal) + (startToFft*fftToDac*dacToGoal);
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
        // Answer is 296006754704850
    }
}
