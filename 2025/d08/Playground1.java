import java.io.*;
import java.util.*;

public class Playground1 { 
    static int MAX_CONNECTIONS = 10;
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
   
    static boolean boxPairExists(ArrayList<Pt[]> boxPairs, Pt a, Pt b){
        if(boxPairs.isEmpty()) return false;
        if(a.isOrigin() || b.isOrigin()) return false;
        for(Pt[] bp: boxPairs){
            //System.out.println(a);
            //System.out.println(b);
            //System.out.println(Arrays.toString(bp));
            if((a.equals(bp[0]) && b.equals(bp[1])) ||
               (a.equals(bp[1]) && b.equals(bp[0]))){
                return true;
            }
        }
        return false;
    }

    static double computeOutput(ArrayList<String> lines){
        MAX_CONNECTIONS = (lines.size() > 50) ? lines.size() : 10;
        double product = 1;
        ArrayList<HashSet<Pt>> connections = new ArrayList<>();
        ArrayList<Pt> boxes = getJunctionBoxes(lines);
        ArrayList<Pt[]> boxPairs = new ArrayList<>();
        
        // Get the # of closest neighbors by order
        for(int i = 0; i < MAX_CONNECTIONS; i++){
            Pt closestPt = new Pt();
            Pt closestPt2 = new Pt();
            double closestDist = Double.MAX_VALUE;
            for(int j = 0; j < boxes.size(); j++){
                Pt currPt = boxes.get(j);
                for(int k = 0; k < boxes.size(); k++){
                    double dist = distance(currPt, boxes.get(k));
                    //if(closestPt.isOrigin() || closestPt2.isOrigin()) continue;
                    if(dist < closestDist && j != k && !boxPairExists(boxPairs, currPt, boxes.get(k))){ //&&
                       // !closestPt.isOrigin() && !closestPt2.isOrigin()){
                        closestDist = dist;
                        closestPt = boxes.get(k);
                        closestPt2= currPt;
                    }
                }
            }
            //if(!boxPairExists(boxPairs, closestPt, closestPt2)){
            boxPairs.add(new Pt[]{closestPt, closestPt2});
            //}
            //System.out.printf("\nClosest pair: %s >-< %s\n", closestPt, closestPt2);
            //for(Pt[] bp: boxPairs) System.out.println(Arrays.toString(bp));
        }

        // Add the remaining boxes
        for(int j=0; j < boxes.size(); j++){
            Pt curr = boxes.get(j);
            int k = 0;
            boolean canAdd = true;
            while(k < boxPairs.size()){
                Pt c = boxPairs.get(k)[0];
                if(boxPairs.get(k).length == 2){
                    Pt d = boxPairs.get(k)[1];
                    if(curr.equals(c) || curr.equals(d)){
                        //boxPairs.add(new Pt[]{curr});
                        canAdd = false;
                        break;
                    }
                }
                else if(boxPairs.get(k).length == 1){
                    if(curr.equals(c)){
                        //boxPairs.add(new Pt[]{curr});
                        canAdd = false;
                        break;
                    }
                }
                k++;     
            }
            if(canAdd) boxPairs.add(new Pt[]{curr});
        }
    
        //for(Pt[] bp: boxPairs) System.out.println(Arrays.toString(bp));
        //System.out.println("Size: " + boxPairs.size());
        
        for(Pt[] bp: boxPairs){
            if(connections.isEmpty()){
                HashSet<Pt> circuit = new HashSet<>();
                circuit.add(bp[0]);
                if(bp.length > 1) circuit.add(bp[1]);
                connections.add(circuit);
            }
            else {
                boolean inExistingCircuit = false;
                for(HashSet<Pt> circuit: connections){
                    if(bp.length == 2 && (circuit.contains(bp[0]) || circuit.contains(bp[1]))){
                        circuit.add(bp[0]);
                        circuit.add(bp[1]);
                        inExistingCircuit = true;
                    }
                }
                if(!inExistingCircuit) {
                    HashSet<Pt> pair = new HashSet<>();
                    pair.add(bp[0]);
                    if(bp.length > 1) pair.add(bp[1]);
                    connections.add(pair);
                }
            }
        }

        //System.out.println("\nORIGINAL:");
        //    for(HashSet<Pt> c: connections) System.out.println(c);

        //System.out.println();

        for(int l = connections.size()-1; l > 0; l--){
            HashSet<Pt> curr = connections.get(l);
            //System.out.println(curr);
            for(int m = l-1; m >= 0; m--){
                HashSet<Pt> prev = connections.get(m);
                for(Pt pt: curr) {
                    if(prev.contains(pt)){
                        connections.get(m).addAll(curr);
                        connections.remove(l);
                        break;
                    }
                }
            }
            //System.out.println("\nConnections:");
            //for(HashSet<Pt> c: connections) System.out.println(c);
        }
        
        //System.out.println("\nConnections:");
        //for(HashSet<Pt> c: connections) System.out.println(c);
        
        int idx = 0;
        double[] largestCircuts  = new double[3];
        while(idx < largestCircuts.length){
            double highestCircuitNum = 0;
            int highestCircuitIdx = 0;
            for(int k = 0; k < connections.size(); k++){
                int circuitNum = connections.get(k).size();
                if(highestCircuitNum < circuitNum ){
                    highestCircuitNum = circuitNum;
                    highestCircuitIdx = k;
                }
            }
            largestCircuts[idx] = highestCircuitNum;
            if(!connections.isEmpty())
                connections.remove(highestCircuitIdx);
            idx+=1;
        }
        System.out.println("LARGEST: " + Arrays.toString(largestCircuts));
        for(double cn: largestCircuts) product *= cn;
        return product;
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
        // Answer is 42840
    }
}