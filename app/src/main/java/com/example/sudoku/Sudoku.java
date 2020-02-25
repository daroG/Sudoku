package com.example.sudoku;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Sudoku {


    private final Random random = new Random();


    public static final int EMPTY = 0;
    public static final int SIZE = 9;

    private int[][] solvedGrid = {
        {9,3,4,1,7,2,6,8,5},
        {7,6,5,8,9,3,2,4,1},
        {8,1,2,6,4,5,3,9,7},
        {4,2,9,5,8,1,7,6,3},
        {6,5,8,7,3,9,1,2,4},
        {1,7,3,4,2,6,8,5,9},
        {2,9,7,3,5,8,4,1,6},
        {5,4,6,2,1,7,9,3,8},
        {3,8,1,9,6,4,5,7,2},
    };
    private int[][] actualGrid = new int[9][9];
    private int[][] startingGrid = new int[9][9];

    private int lastHintR = -1;
    private int lastHintC = -1;

    public Sudoku(){
        resetGrid();
    }

    public void resetGrid(){
        assignArrayToArray(solvedGrid, actualGrid);

        randomize(actualGrid);

        assignArrayToArray(actualGrid, solvedGrid);

        removeElements(60, actualGrid);

        assignArrayToArray(actualGrid, startingGrid);
    }

    public boolean solve() {

        int[][] tempGrid = new int[9][9];
        assignArrayToArray(actualGrid, tempGrid);

        for (int i = 0; i < SIZE * SIZE; i++){
            int row = i / SIZE;
            int col = i % SIZE;
            if (tempGrid[row][col] == EMPTY){
                for (int number = 1; number <= SIZE; number++) {
                    if (isOk(row, col, number)) {
                        tempGrid[row][col] = number;

                        if (solve()) {
                            return true;
                        } else {
                            tempGrid[row][col] = EMPTY;
                        }
                    }
                }

                return false;
            }
        }

        return true;
    }

    public boolean isSolvable(){

        if (solve()){
            return true;
        }
        return false;
    }

    public void updateBoard(int row, int column, int value){
        actualGrid[row][column] = value;
    }

    public void getHint(){
        for (;;){
            int r = random.nextInt(SIZE * SIZE);
            lastHintR = r/9;
            lastHintC = r%9;
            if(actualGrid[lastHintR][lastHintC] == EMPTY){
                actualGrid[lastHintR][lastHintC] = solvedGrid[lastHintR][lastHintC];

                return;
                //return new Cell(r/9, r%9, solvedGrid[r/9][r%9], 3);
            }
        }
    }

    private boolean isInRow(int row, int number){
        for (int i = 0; i < SIZE; i++){
            if (actualGrid[row][i] == number){
                return true;
            }
        }
        return false;
    }

    private boolean isInCol(int col, int number){
        for (int i = 0; i < SIZE; i++){
            if (actualGrid[i][col] == number){
                return true;
            }
        }
        return false;
    }


    private boolean isInBox(int row, int col, int number){

        int r = row - row % 3;
        int c = col - col % 3;

        for (int i = r; i < r + 3; i++)
            for (int j = c; j < c + 3; j++)
                if (actualGrid[i][j] == number)
                    return true;

        return false;
    }


    public boolean isOk(int row, int col, int number) {
        return !isInRow(row, number)  &&  !isInCol(col, number)  &&  !isInBox(row, col, number);
    }

    private void permutateRow(int[][] grid){

        int row1 = random.nextInt(9);

        int r = row1/3;
        int row2 = r * 3 + ((row1%3 == 0) ? getRandomFrom2(1,2) : (row1%3 == 1 ? getRandomFrom2(0,2) : getRandomFrom2(0,1)));

        int[] tempRow = grid[row2];
        grid[row2] = grid[row1];
        grid[row1] = tempRow;


    }


    private void permutateCol(int[][] grid){

        int col1 = random.nextInt(SIZE);

        int c = col1/3;
        int col2 = c * 3 + ((col1%3 == 0) ? getRandomFrom2(1,2) : (col1%3 == 1 ? getRandomFrom2(0,2) : getRandomFrom2(0,1)));


        for (int i = 0; i < SIZE;i++){
            int tempCell = grid[i][col2];
            grid[i][col2] = grid[i][col1];
            grid[i][col1] = tempCell;
        }


    }

    private void permutateBoxH(int[][] grid){

        int col1 = random.nextInt(3);
        int col2 = (col1 == 0) ? getRandomFrom2(1,2) : (col1 == 1 ? getRandomFrom2(0,2) : getRandomFrom2(0,1));

        for (int i = 0; i < 3; i++){
            for (int j = 0; j < SIZE;j++){
                int tempCell = grid[j][col2*3 + i];
                grid[j][col2*3 + i] = grid[j][col1*3 + i];
                grid[j][col1*3 + i] = tempCell;
            }
        }
    }

    private int[][] permutateBoxV(int[][] grid){

        int row1 = random.nextInt(3);
        int row2 = (row1 == 0) ? getRandomFrom2(1,2) : (row1 == 1 ? getRandomFrom2(0,2) : getRandomFrom2(0,1));

        for (int i = 0; i < 3; i++){
            int[] tempRow = grid[row2*3 + i];
            grid[row2*3 + i] = grid[row1*3 + i];
            grid[row1*3 + i] = tempRow;
        }

        return grid;
    }

    private int getRandomFrom2(int first, int second){
        return random.nextInt(2) == 0 ? first : second;
    }

    private void randomize(int[][] grid){
        for (int i = 0;i < random.nextInt(20) + 30; i++){

            //Get random permutation
            switch (random.nextInt(4)){
                case 0:
                    permutateRow(grid);
                    break;
                case 1:
                    permutateCol(grid);
                    break;
                case 2:
                    permutateBoxH(grid);
                    break;
                default:
                    permutateBoxV(grid);
                    break;
            }
        }

    }

    public CellList getRandomizedCells(){
        resetGrid();

        return getBoard();
    }

    private int[][] removeElements(int n, int[][] grid){


        List<Pair<Integer,Integer>> pairs = new ArrayList<>();
        for (int i = 0;i<n;i++){

            int row = random.nextInt(9);
            int col = random.nextInt(9);

            Pair<Integer, Integer> p = new Pair<>(row, col);
            if (pairs.contains(p) || grid[row][col] == EMPTY){
                n++;
            }else {
                grid[row][col] = EMPTY;
                pairs.add(p);
            }

            if(n>100)
                break;
        }

        return grid;

    }

    public CellList getBoard(){
        return new CellList(){{
            for (int r = 0; r < 9; r++){
                for (int c = 0; c< 9; c++){
                    add(new Cell(r, c, actualGrid[r][c], startingGrid[r][c] != 0 ? 0 : (r == lastHintR && c == lastHintC) ? 3 : 1));
                }
            }
        }};
    }

    public static void printArray(int[][] a) {
        System.out.println("[ ");
        for (int i = 0; i < a.length; i++) {

            System.out.print("[ ");
            for (int j = 0;j<a[i].length; j++){
                System.out.print(a[i][j] + " ");
            }
            System.out.println("]");
        }
        System.out.println("]");
    }

    public void clearCell(int row, int col){
        if(startingGrid[row][col] == EMPTY)
            actualGrid[row][col] = EMPTY;
    }

    public boolean isDone(){

        for (int r = 0; r < SIZE;r++)
            for (int c = 0; c <SIZE;c++)
                if(actualGrid[r][c] != solvedGrid[r][c])
                    return false;

        return true;
    }

    public void assignArrayToArray(int[][] array1, int[][] array2){
        for (int i = 0; i < array1.length; i++)
            for (int j = 0; j < array1[i].length; j++)
                array2[i][j] = array1[i][j];
    }

}
