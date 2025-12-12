import java.io.*;
import java.util.*;

public class Reactor1 {   
    static String START = "you";
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

    static long getNumOfPaths(HashMap<String, HashSet<String>> graph, String start, String goal){
        Queue<ArrayList<String>> q = new LinkedList<>();
        ArrayList<String> initPath = new ArrayList<>();
        initPath.add(start);
        q.add(initPath);

        long count = 0;
        while(!q.isEmpty()){
            ArrayList<String> path = q.poll();
            String curr = path.get(path.size()-1);
            if(curr.equals(goal)) count++;
            else if(!graph.containsKey(curr)) continue;
            else {
                for(String adj: graph.get(curr)){
                    ArrayList<String> newPath = new ArrayList<>(path);
                    newPath.add(adj);
                    q.add(newPath);
                }
            }
        }
        return count;
    } 

    static long computeOutput(ArrayList<String> lines){
        HashMap<String, HashSet<String>> graph = getGraph(lines);
        long sum = getNumOfPaths(graph, START, GOAL);
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
        System.out.printf("Answer: %d\n",computeOutput(lines));
        // Answer is 494
    }
}