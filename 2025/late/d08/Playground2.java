import java.io.*;
import java.util.*;

public class Playground2 { 
    static class Pt {
        double x, y, z;

        public Pt(){
            this.x = 0;
            this.y = 0;
            this.z = 0;
        }

        public Pt(double x, double y, double z){
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public String toString() {
            return "(" + this.x + ", " + this.y + ", " + this.z + ")";
        }    
        
        @Override
        public boolean equals(Object o){
            Pt other = (Pt) o;
            boolean result = this.x == other.x && this.y == other.y && this.z == other.z;
            //System.out.printf("%s ?= %s -> %s\n", this.toString(), other.toString(), result);
            return result;
        }
    }

    static double distance(Pt a, Pt b){
        return Math.sqrt(Math.pow(a.x-b.x, 2) +
                        Math.pow(a.y-b.y, 2) + 
                        Math.pow(a.z-b.z, 2));
    }

    static ArrayList<Pt> getJunctionBoxes(ArrayList<String> lines){
        ArrayList<Pt> boxes = new ArrayList<>();
        for(int i = 0; i < lines.size(); i++){
            String[] ptStr = lines.get(i).split(",");
            double x = Double.parseDouble(ptStr[0]);
            double y = Double.parseDouble(ptStr[1]);
            double z = Double.parseDouble(ptStr[2]);
            boxes.add(new Pt(x, y, z));
        }
        return boxes;
    }

    static HashMap<Pt, HashSet<Pt>> initCircuits(ArrayList<Pt> boxes){
        HashMap<Pt, HashSet<Pt>> circuits = new HashMap<>();
        for(Pt pt: boxes) circuits.put(pt, new HashSet<>());
        return circuits;
    }

    static HashMap<Double, ArrayList<Pt>> getEdges(ArrayList<Pt> boxes){
        HashMap<Double, ArrayList<Pt>> edges = new HashMap<>(boxes.size());
        for(int i = 0; i < boxes.size(); i++){
            Pt curr = boxes.get(i);
            for(int j = i+1; j < boxes.size(); j++){
                Pt node = boxes.get(j);
                double dist = distance(curr, node);
                if(!edges.containsKey(dist))
                    edges.put(dist, new ArrayList<>(Arrays.asList(curr, node)));
            }
        }
        return edges;
    }

    static double computeOutput(ArrayList<String> lines){
        ArrayList<Pt> boxes = getJunctionBoxes(lines);
        HashMap<Double, ArrayList<Pt>> edges = getEdges(boxes);
        
        HashMap<Pt, HashSet<Pt>> circuits = initCircuits(boxes);
        ArrayList<Double> distances = new ArrayList<>(edges.keySet());
        Collections.sort(distances);
        
        int i = 0;
        while(true){
            if(i >= distances.size()) break;
            ArrayList<Pt> edge = edges.get(distances.get(i));
            Pt a = edge.get(0);
            Pt b = edge.get(1);
            HashSet<Pt> circuitA = circuits.get(a);
            HashSet<Pt> circuitB = circuits.get(b);
            if(!circuitA.contains(b) || !circuitB.contains(a)){
                HashSet<Pt> union = new HashSet<>(circuitA);
                union.addAll(circuitB);
                union.add(a);
                union.add(b);
                for(Pt pt: union) circuits.put(pt, union);
                if(union.size() == circuits.size()) return a.x * b.x;
            }
            i++;
        }
        return -1;
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
        System.out.printf("Answer: %.0f\n",computeOutput(lines));
        // Answer is 170629052
    }
}