import java.io.*;
import java.util.*;

public class MovieTheater2 {  
    static class Pt{
        int x, y;

        public Pt(int x, int y){
            this.x = x;
            this.y = y;
        }

        public Pt(Pt pt){
            new Pt(pt.x, pt.y);
        }

        @Override
        public String toString(){
            return "(" + this.x + ", " + this.y + ")";
        }

        @Override
        public boolean equals(Object o){
            Pt other = (Pt) o;
            return this.x == other.x && this.y == other.y;
        }
    }
    
    static class Rectangle{
        int yMin, yMax, xMin, xMax;

        public Rectangle(int yMin, int yMax, int xMin, int xMax){
            this.yMin = yMin;
            this.yMax = yMax;
            this.xMin = xMin;
            this.xMax = xMax;
        }
        
        public Rectangle(Pt a, Pt b){
            this.yMin = Math.min(a.y, b.y);
            this.yMax = Math.max(a.y, b.y);
            this.xMin = Math.min(a.x, b.x);
            this.xMax = Math.max(a.x, b.x);
        }

        @Override
        public String toString(){
            return "[" + yMin + ", " + xMin + ", " + xMax + ", "+ yMax + "]";
        }
    }

    static class RectangleWithArea {
        int area;
        Rectangle rect;

        public RectangleWithArea(int area, Rectangle rect){
            this.area = area;
            this.rect = rect;
        }

        @Override
        public String toString(){
            return rect.toString() + " -> " + area;
        }
    }

     static boolean collision(Rectangle a, Rectangle b){
        boolean yMin_check = a.yMin >= b.yMax;
        boolean yMax_check = a.yMax <= b.yMin;
        boolean xMin_check = a.xMin >= b.xMax;
        boolean xMax_check = a.xMax <= b.xMin;
        return !(yMin_check || yMax_check || xMin_check || xMax_check);
    }

    static ArrayList<Pt> getPts(ArrayList<String> lines){
        ArrayList<Pt> pts = new ArrayList<>();
        for(String line: lines){
            String[] ptStrArr = line.split(",");
            Integer x = Integer.parseInt(ptStrArr[0]);
            Integer y = Integer.parseInt(ptStrArr[1]);
            pts.add(new Pt(x, y));
        }
        return pts;
    }

    static ArrayList<Pt> getPerimeter(ArrayList<Pt> pts){
        ArrayList<Pt> perimeter = new ArrayList<>();
        for(int i = 0; i < pts.size(); i++){
            if (pts.size() == 1) {
                //HashSet<Pt> hs = new HashSet<>();
                //hs.add(pts.get(0));
                return pts;
            }
            Pt a = pts.get(i);
            Pt b = (i < pts.size()-1) ? pts.get(i+1) : pts.get(0);
            int xDiff = a.x - b.x;
            int yDiff = a.y - b.y;
            perimeter.add(a);
            if(yDiff == 0) {
                int dx = (a.x > b.x) ? -1 : 1;
                int cx = a.x;
                while(cx != b.x){
                    perimeter.add(new Pt(cx,a.y));
                    cx+=dx;
                }
            }
            else if(xDiff == 0){
                int dy = (a.y < b.y) ? 1 : -1;
                int cy = a.y;
                while(cy != b.y){
                    perimeter.add(new Pt(a.x, cy));
                    cy+=dy;
                }
            }
            perimeter.add(b);
        }
        return perimeter;
    }

    static ArrayList<Pt> getFilledPerimeter(ArrayList<Pt> perimeter){
        ArrayList<Pt> filled = new ArrayList<>();
        for(int i = 0; i < perimeter.size()-1; i++){
            Pt a = perimeter.get(i);
            Pt b = perimeter.get(i+1);
            
            if((a.y != b.y) || a.equals(b)) continue;
            else if(b.x - a.x > 1 && a.y == b.y){
                //System.out.printf("Fill between %s and %s\n", a, b);
                int j = a.x;
                while (j <= b.x) {
                    filled.add(new Pt(j, a.y));
                    j++;
                }
            }
            else  filled.add(a);
        }
        filled.add(perimeter.get(perimeter.size()-1));
        return filled;
    }

     static int getArea(Pt a, Pt b){
        return (Math.abs(a.x-b.x)+1) * (Math.abs(a.y - b.y)+1);
    }

    static long computeOutput(ArrayList<String> data){
        ArrayList<Pt> pts = getPts(data);
        
        /* CODE WAS TOO SLOW
        ArrayList<Pt> perimeter = getPerimeter(pts);
        Collections.sort(perimeter);
        ArrayList<Pt> filled = getFilledPerimeter(perimeter);
        int largestArea = getLargestArea(pts, filled);*/

        // Retrieve the straight lines of the perimeter;
        ArrayList<Rectangle> lines = new ArrayList<>();
        for(int i = 0; i < pts.size(); i++){
            if (i == 0)  lines.add(new Rectangle(pts.get(i), pts.get(pts.size()-1)));
            else lines.add(new Rectangle(pts.get(i-1), pts.get(i)));
        }

        //System.out.println(lines);

        // Get all rectangles with associated areas
        ArrayList<RectangleWithArea> rwa = new ArrayList<>();
        for(int i = 0; i < pts.size()-1; i++){
            for(int j = i+1; j < pts.size(); j++){
                Pt a = pts.get(i);
                Pt b = pts.get(j);
                int yMin = Math.min(a.y, b.y);
                int yMax = Math.max(a.y, b.y);
                int xMin = Math.min(a.x, b.x);
                int xMax = Math.max(a.x, b.x);
                rwa.add(new RectangleWithArea(getArea(a, b), new Rectangle(yMin, yMax, xMin, xMax)));
            }
        }

        rwa.sort(Comparator.comparingInt((RectangleWithArea ra) -> ra.area).reversed());
        //System.out.println(rwa);
        
        // Find the first area within boundary (from largest to smallest area)
        for(RectangleWithArea ra: rwa){
            boolean validArea = true;
            for(Rectangle rect: lines){
                if(collision(rect, ra.rect)){
                    validArea = false;
                    break;
                }
            }
            if(validArea) return ra.area;    
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
        System.out.printf("Answer: %d\n",computeOutput(lines));
        // Answer is 1577956170
    }
}