import java.io.*;
import java.util.*;

public class PrintingDepartment1 {
    static int MAX_ADJACENT_ROLLS = 4;
    static char ROLL_CHAR = '@';
    static char VISITED_CHAR = 'X';

    static void printBoard(char[][] board){
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++)
                System.out.printf("%s",board[i][j]);
            System.out.println();
        }
    }

    static int getAdjacentRolls(char[][] board, int row, int col){
        int adjacentRolls = 0;
        for(int i = row-1; i <= row+1; i++){
            for(int j = col-1; j <= col+1; j++){
                //System.out.println("Pt: " + i + "," + j);
                if(i == row && j == col) continue;
                else if(i >= 0 && j >= 0 && i < board.length && j < board[i].length && 
                        (board[i][j] == ROLL_CHAR || board[i][j] == VISITED_CHAR)) 
                    adjacentRolls++;
            }
        }
        return adjacentRolls;
    }

    static int computeOutput(ArrayList<String> lines){
        int totalRollsRemoved = 0;
        int height = lines.size();
        int width = lines.get(0).length();
        char[][] board = new char[height][width];
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++) board[i][j] = lines.get(i).charAt(j);
        }
        //System.out.printf("Rows: %d, Cols: %d\n",board.length,board[0].length);
        for(int row = 0; row < height; row++){
            for(int col = 0; col < width; col++){
                //System.out.printf("Looking at Board position (%d,%d) -> %s\n", row, col, board[row][col]);                
                if(board[row][col] == ROLL_CHAR && getAdjacentRolls(board, row, col) < MAX_ADJACENT_ROLLS){
                    ++totalRollsRemoved;
                    board[row][col] = VISITED_CHAR;
                }  
            }
        }
        //printBoard(board);
        return totalRollsRemoved;
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
        System.out.printf("Answer: %d",computeOutput(lines));
        // Answer is 1553
    }
}
