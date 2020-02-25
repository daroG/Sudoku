package com.example.sudoku;

public class Cell {
    private int row;
    private int column;
    private int value;
    //0 - starting, 1 - normal, 2 - wrong, 3 - hint
    private int status;

    public Cell(int row, int column, int value, int status) {
        this.row = row;
        this.column = column;
        this.value = value;
        this.status = status;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getValue() {
        return value;
    }

    public int getStatus(){return status;}

    public void setValue(int value) {
        this.value = value;
    }

    public void setStatus(int status){this.status = status;}

    public boolean isStarting() {
        return status == 0;
    }
}
