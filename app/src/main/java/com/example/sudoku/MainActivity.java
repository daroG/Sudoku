package com.example.sudoku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements CellChangeListener, BottomDialog.BottomDialogListener {

    private final String TAG = "Sudoku";
    private Sudoku sudoku;
    private Board mainBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button[] buttons = {
                findViewById(R.id.btn1),
                findViewById(R.id.btn2),
                findViewById(R.id.btn3),
                findViewById(R.id.btn4),
                findViewById(R.id.btn5),
                findViewById(R.id.btn6),
                findViewById(R.id.btn7),
                findViewById(R.id.btn8),
                findViewById(R.id.btn9)
        };

        mainBoard = findViewById(R.id.sudokuBoard);

        for (int i = 0; i<buttons.length;i++){
            final int finalI = i;
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainBoard.cellChanged(finalI + 1);
                }
            });
        }

        findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sudoku.clearCell(mainBoard.getSelectedRow(), mainBoard.getSelectedCol());
                mainBoard.setBoard(sudoku.getBoard());
            }
        });

        sudoku = new Sudoku();

        mainBoard.setBoard(sudoku.getBoard());

        mainBoard.setCellChangeListener(this);

    }

    @Override
    public boolean onCellChange(int row, int column, int value) {
        boolean isOk = sudoku.isOk(row, column, value);
        sudoku.updateBoard(row, column, value);
        isSudokuDone();
        return isOk;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_new){
            mainBoard.setBoard(sudoku.getRandomizedCells());
            Log.i(TAG, "Action: new game");
            return true;
        }else if (id == R.id.action_hint){
            sudoku.getHint();
            mainBoard.setBoard(sudoku.getBoard());
            isSudokuDone();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onButtonClick(int id) {
        if(id == R.id.action_play_again){
            mainBoard.setBoard(sudoku.getRandomizedCells());
        }
    }

    private void isSudokuDone(){
        if (sudoku.isDone()){
            BottomDialog dialog = new BottomDialog();
            dialog.show(getSupportFragmentManager(), "bottomDialog");
        }
    }
}
