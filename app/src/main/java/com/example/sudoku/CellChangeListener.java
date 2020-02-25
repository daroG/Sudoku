package com.example.sudoku;

public interface CellChangeListener {
    boolean onCellChange(int row, int column, int value);
}
