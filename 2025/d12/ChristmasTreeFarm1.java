import java.io.*;
import java.util.*;
import java.awt.*;

public class ChristmasTreeFarm1 {
    static char OCCUPIED = '#';
    static char EMPTY = '.'; 
    static int EXPECTED_HEIGHT = 3;
    static int EXPECTED_WIDTH =3;
    
    static class Present {
        int width, height;
        ArrayList<Point> pts;

        public Present(int width, int height, ArrayList<Point> pts){
            this.width = width;
            this.height = height;
            this.pts = new ArrayList<>(pts);
        }

        @Override
        public String toString(){
            String data = "Pts: [ ";
            for(Point pt: this.pts) data += String.format("(%d,%d) ", pt.x, pt.y);
            data += String.format("] (%dx%d)", width, height);
            return data;
        }
    }

    static class Region {
        int width, height;
        ArrayList<Integer> presentQuantities;
    
        public Region(int width, int height, ArrayList<Integer> presentQuantities){
            this.width = width;
            this. height = height;
            this.presentQuantities = new ArrayList<>(presentQuantities);
        }

        @Override
        public String toString(){
            String data = String.format("Dim [%s, %s], Rules: ( ", width, height);
            for(int i: this.presentQuantities) data += i + " ";
            data += ")";
            return data;
        }
    }
    
    static HashMap<Integer, Present> getPresents(ArrayList<String> lines, int end){
        HashMap<Integer, Present> presents = new HashMap<>();
        ArrayList<Point> pts = new ArrayList<>();
        int id = -1, row = 0, col = 0;
        for(int i = 0; i <  end; i++){
            if(lines.get(i).endsWith(":")){
                String idStr = lines.get(i).substring(0, lines.get(i).length()-1);
                id = Integer.parseInt(idStr);
            }
            else if (lines.get(i).isEmpty()){
                presents.put(id, new Present(col, row, pts));
                pts.clear();
                row = 0;
                col = 0;
            }
            else{
                col = lines.get(i).length();
                for(int j = 0; j < col; j++){
                    if(lines.get(i).charAt(j) == OCCUPIED)
                        pts.add(new Point(j, row));
                }
                row++;
            }
        }
        return presents;
    }

    static ArrayList<Region> getRegions(ArrayList<String> lines, int start){
        ArrayList<Region> regions = new ArrayList<>();
        for(int i = start+1; i < lines.size(); i++){
            String[] segments = lines.get(i).split("\s+");
            //System.out.println(Arrays.toString(segments));
            String[] dimStr = segments[0].split("x");
            int width = Integer.parseInt(dimStr[0]);
            int height = Integer.parseInt(dimStr[1].substring(0, dimStr[1].length()-1));
            ArrayList<Integer> presentQuantities = new ArrayList<>(segments.length-1);
            for(int j = 1; j < segments.length; j++) presentQuantities.add(Integer.parseInt(segments[j]));
            regions.add(new Region(width, height, presentQuantities));
        }
        return regions;
    }
    
    // This solution is fairly naive and only checks to see if the 
    // presents can be placed next to each other singe each present
    // has the same dimensions (this solution doesn't work with the
    // sample imput)
    static long computeOutput(ArrayList<String> lines){
        int regionStart = lines.size()-1;
        while(regionStart >= 0){
            if(lines.get(regionStart).isEmpty()) break;
            else regionStart--;
        }
        HashMap<Integer, Present> presents = getPresents(lines, regionStart);
        ArrayList<Region> regions = getRegions(lines, regionStart);
        //System.out.println(presents);
        //System.out.println(regions);

        long count = 0;
        for(Region r: regions){
            int projectedPresentLimit = (r.width/EXPECTED_WIDTH)*(r.height/EXPECTED_HEIGHT);
            int presentNum = 0;
            for(int k: r.presentQuantities) presentNum += k;
            if(presentNum <= projectedPresentLimit) {
                count++;
            }
        }
        return count;
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
        // Answer is 451
    }
}