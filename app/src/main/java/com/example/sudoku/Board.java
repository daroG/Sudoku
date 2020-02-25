package com.example.sudoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Board extends View implements CellChanged{

    private final String TAG = "Board";

    private int cellsInSquare = 3;
    private int size = 9;

    private float cellSizePixels = 0f;

    public int getSelectedRow() {
        return selectedRow;
    }

    public int getSelectedCol() {
        return selectedCol;
    }

    private int selectedRow = 0;
    private int selectedCol = 0;

    private Paint thickLinePaint = new Paint(){{
        setStyle(Paint.Style.STROKE);
        setColor(Color.BLACK);
        setStrokeWidth(4f);
    }};
    private Paint thinLinePaint = new Paint(){{
        setStyle(Paint.Style.STROKE);
        setColor(Color.BLACK);
        setStrokeWidth(2f);
    }};
    private Paint selectedCellPaint = new Paint(){{
        setStyle(Paint.Style.FILL_AND_STROKE);
        setColor(Color.parseColor("#6ead3a"));
    }};
    private Paint highlightedCellPaint = new Paint(){{
        setStyle(Paint.Style.FILL_AND_STROKE);
        setColor(Color.parseColor("#efedef"));
    }};
    private Paint textPaint = new Paint(){{
        setStyle(Paint.Style.FILL_AND_STROKE);
        setColor(Color.BLACK);
        setTextSize(90f);
    }};
    private Paint textBluePaint = new Paint(){{
        setColor(Color.parseColor("#4a90e2"));
        setTextSize(90f);
        setStyle(Paint.Style.FILL_AND_STROKE);
    }};
    private Paint textWrongPaint = new Paint(){{
        setColor(Color.parseColor("#ff0000"));
        setTextSize(90f);
        setStyle(Paint.Style.FILL_AND_STROKE);
    }};
    private Paint textHintPaint = new Paint(){{
        setColor(Color.parseColor("#4eb62f"));
        setTextSize(90f);
        setStyle(Paint.Style.FILL_AND_STROKE);
    }};
    private Paint boldTextPaint = new Paint(Paint.FAKE_BOLD_TEXT_FLAG){{
        setStyle(Paint.Style.FILL_AND_STROKE);
        setColor(Color.BLACK);
        setTextSize(90f);
    }};


//    private List<Cell> cells = new ArrayList<>();
    private CellList cells = new CellList();

    private CellChangeListener cellChangeListener = null;


    public Board(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        for (int i = 0; i < size*size;i++){
            cells.add(i, new Cell(i / size, i % size, i % size, 1) );
        }




    }

    @Override
    protected void onDraw(Canvas canvas) {
        cellSizePixels = (float)getWidth() / size;
        fillCells(canvas);
        drawLines(canvas);
        fillText(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizePixel = Math.min(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(sizePixel, sizePixel);
    }

    private void drawLines(Canvas canvas){
        canvas.drawRect(0f, 0f, (float) getWidth(), (float) getHeight(), thickLinePaint);

        for (int i = 0; i<size;i++){
            Paint paintToUse = i % cellsInSquare == 0 ? thickLinePaint : thinLinePaint;

            canvas.drawLine(i * cellSizePixels, 0F, i * cellSizePixels, (float) getHeight(), paintToUse);
            canvas.drawLine(0f, i * cellSizePixels, (float) getWidth(), i * cellSizePixels, paintToUse);


        }
    }

    private void fillCells(Canvas canvas){
        if(selectedCol == -1 || selectedRow == -1)return;

        Log.i(TAG, "cellsSize : " + cells.size());

        for (int i = 0; i<cells.size();i++){
            int row = cells.get(i).getRow();
            int column = cells.get(i).getColumn();
            if( row == selectedRow && column == selectedCol ){
                fillCell(canvas, row, column, selectedCellPaint);
            } else if (row == selectedRow || column == selectedCol ){
                fillCell(canvas, row, column, highlightedCellPaint);
            } else if (row / cellsInSquare == selectedRow / cellsInSquare && column / cellsInSquare == selectedCol / cellsInSquare){
                fillCell(canvas, row, column, highlightedCellPaint);
            } else  if (cells.get(row, column).getValue() != 0 && cells.get(row, column).getValue() == cells.get(selectedRow, selectedCol).getValue()){
                fillCell(canvas, row, column, highlightedCellPaint);
            }
        }
    }

    private void fillText(Canvas canvas){
        Paint actualPaint;
        for (int i = 0; i<cells.size();i++) {
            Cell cell = cells.get(i);

            if(cell.getValue() == 0){
                continue;
            }

            switch (cell.getStatus()){
                case 0:
                    actualPaint = textPaint;
                    break;
                case 2:
                    actualPaint = textWrongPaint;
                    break;
                case 3:
                    actualPaint = textHintPaint;
                    break;
                default:
                    actualPaint = textBluePaint;
            }

            Rect textBounds = new Rect();
            actualPaint.getTextBounds(String.valueOf(cell.getValue()), 0, String.valueOf(cell.getValue()).length(), textBounds);
            float textWidth = actualPaint.measureText(String.valueOf(cell.getValue()));
            float textHeight = textBounds.height();

            canvas.drawText(String.valueOf(cell.getValue()), (cell.getColumn() * cellSizePixels) + cellSizePixels / 2 - textWidth / 2, ((cell.getRow())  * cellSizePixels) + cellSizePixels/2 + textHeight /2 , actualPaint);

        }
    }

    private void fillCell(Canvas canvas, int row, int column, Paint paint){
        canvas.drawRect(column * cellSizePixels, row*cellSizePixels, (column + 1) * cellSizePixels, (row + 1) * cellSizePixels, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){

            selectedRow = (int)(event.getY() / cellSizePixels);
            selectedCol = (int)(event.getX() / cellSizePixels);
            invalidate();
            return true;
        }
        return false;
    }

    public void setBoard(CellList cells){
        this.cells = cells;
        invalidate();
    }

    public void addHint(Cell cell){

    }


    public Cell getCell(int row, int column){
        return cells.get(row*size + column);
    }

    public void cellChanged(int i){
        if(selectedCol == -1 || selectedRow == -1)return;

        if(!getCell(selectedRow, selectedCol).isStarting()){
            if(cellChangeListener.onCellChange(selectedRow, selectedCol, i)){
                getCell(selectedRow, selectedCol).setStatus(1);
            }else {
                getCell(selectedRow, selectedCol).setStatus(2);
            }
            getCell(selectedRow, selectedCol).setValue(i);
        }
        invalidate();
    }


    public void setCellChangeListener(CellChangeListener listener){
        this.cellChangeListener = listener;
    }

}
