package com.example.wordsoliter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public class HintDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = "Хотите использовать подсказку за " + getResources().getInteger(R.integer.moneyforhint) + " монет";
        String message = "";
        String button1String = "Да";
        String button2String = " Нет";

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setPositiveButton(button1String, (dialog, id) -> ((GameActivity) Objects.requireNonNull(getActivity())).useHint());
        builder.setNegativeButton(button2String, (dialog, id) -> ((GameActivity) Objects.requireNonNull(getActivity())).cancel());
        builder.setCancelable(true);

        return builder.create();
    }
}
