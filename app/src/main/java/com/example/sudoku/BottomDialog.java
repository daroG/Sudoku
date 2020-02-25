package com.example.sudoku;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomDialog extends BottomSheetDialogFragment {


    private BottomDialogListener mListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        v.findViewById(R.id.action_play_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClick(R.id.action_play_again);
                dismiss();
            }
        });

        return v;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mListener = (BottomDialogListener) context;
    }

    public interface BottomDialogListener{
        void onButtonClick(int id);
    }
}
