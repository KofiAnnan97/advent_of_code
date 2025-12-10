import java.io.*;
import java.util.*;

public class Playground2 { 

    static class Pt {
        public double x, y, z;

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

        public boolean isOrigin(){
            return this.x == 0 && this.y == 0 && this.z == 0;
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

        @Override
        public Pt clone(){
            return new Pt(this.x, this.y, this.z);
        }
    }
    
    static double distance(Pt a, Pt b){
        return Math.sqrt(Math.pow(a.x-b.x, 2) +
                        Math.pow(a.y-b.y, 2) + 
                        Math.pow(a.z-b.z, 2));
    }


    static Pt nearestNeighbor(Pt curr, ArrayList<Pt> boxes){
        Pt nearest = new Pt();
        double closestDist = Double.MAX_VALUE;
        for(Pt box: boxes){
            double dist = distance(curr, box);
            if(!curr.equals(box) && dist < closestDist){
                closestDist = dist;
                nearest = box;
            }
        }
        return nearest;
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

    static double computeOutput(ArrayList<String> lines){
        HashMap<Double, ArrayList<Pt>> edges = new HashMap<>();
        double result = 0;
        double lastDist = 0;
        ArrayList<Pt> boxes = getJunctionBoxes(lines);
        for(int i = 0; i < boxes.size(); i++){
            Pt curr = boxes.get(i);
            for(int j = i+1; j < boxes.size(); j++){

                Pt node = boxes.get(j);
                double dist = distance(curr, node);
            //Arraylist<Pt> pair = new ArrayList<>(Arrays.asList(curr, closest));
                edges.put(dist, new ArrayList<>(Arrays.asList(curr, node)));
            }
            //Pt closest = nearestNeighbor(curr, boxes);
            //double dist = distance(curr, closest);
            //Arraylist<Pt> pair = new ArrayList<>(Arrays.asList(curr, closest));
            //edges.put(dist, new ArrayList<>(Arrays.asList(curr, closest)));
            //lastDist = dist;
        }

        List<Map.Entry<Double, ArrayList<Pt>>> edgesList = new ArrayList<>(edges.entrySet());
        edgesList.sort(Map.Entry.comparingByKey());

        HashMap<Pt, HashSet<Pt>> circuits = new HashMap<>();
        for(Pt pt: boxes) circuits.put(pt, new HashSet<Pt>());

        int idx = 0;
        while(true){
            if(idx >= edgesList.size()) break;
                ArrayList<Pt> pair = edgesList.get(idx).getValue();
                HashSet<Pt> a = circuits.get(pair.get(0));
                HashSet<Pt> b = circuits.get(pair.get(1));
                //System.out.printf("%s | %s v. %s\n", pair, a, b);
                
                if(!a.contains(pair.get(1)) || !b.contains(pair.get(0))){
                    HashSet<Pt> union = new HashSet<>();
                    union.add(pair.get(0));
                    union.add(pair.get(1));
                    union.addAll(a);
                    union.addAll(b);
                    //System.out.print(pair);
                    //System.out.println(union);
                    for(Pt pt: union) circuits.put(pt, union);
                    
                    //System.out.println(union.size());
                    //System.out.println(pair.get(0));
                    //System.out.println(pair.get(1));
                    //circuits.put(pair.get(1), union);
                    if(edgesList.size() <= union.size()){
                        System.out.println("Hello");
                        result = pair.get(0).x * pair.get(1).x;
                        break;
                    }
                }
         
            idx++;
        }
        return result;
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
        System.out.printf("\nAnswer: %.0f\n",computeOutput(lines));
        // Answer is 
        /*
            Between 33643246 and 6710617088
         */
    }
}