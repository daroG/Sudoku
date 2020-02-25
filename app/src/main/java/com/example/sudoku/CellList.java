package com.example.sudoku;

import java.util.ArrayList;

public class CellList extends ArrayList<Cell> {

    public Cell get(int row, int col){
        return this.get(row * 9 + col);
    }

}
