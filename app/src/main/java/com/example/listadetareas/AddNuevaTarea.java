package com.example.listadetareas;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.listadetareas.Modelo.ToDoModel;
import com.example.listadetareas.Utility.DataBase;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNuevaTarea extends BottomSheetDialogFragment {

    public static final String TAG = "AddNuevaTarea";

    private EditText mEditarTexto;
    private Button mGuardarBoton;
    private DataBase miDB;

    public static AddNuevaTarea newInstance(){
        return new AddNuevaTarea();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_nueva_tarea, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEditarTexto = view.findViewById(R.id.editText);
        mGuardarBoton = view.findViewById(R.id.addButton);

        miDB = new DataBase(getActivity());

        boolean isUpdate = false;

        Bundle bundle = getArguments();
        if (bundle != null){
            isUpdate =true;
            String tarea = bundle.getString("tarea");
            mEditarTexto.setText(tarea);

            if (tarea.length() > 0){
                mGuardarBoton.setEnabled(false);
            }
        }

        mEditarTexto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")){
                    mGuardarBoton.setEnabled(false);
                    mGuardarBoton.setBackgroundColor(Color.GRAY);
                }else {
                    mGuardarBoton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        boolean finalIsUpdate = isUpdate;
        mGuardarBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mEditarTexto.getText().toString();
                if (finalIsUpdate){
                    miDB.subirTarea(bundle.getInt("Id"),text );
                }else {
                    ToDoModel item = new ToDoModel();
                    item.setTarea(text);
                    item.setStatus(0);
                    miDB.instertTarea(item);
                }
                dismiss();
            }
        });

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof OnDialogCloseListener){
            ((OnDialogCloseListener)activity).onDialogClose(dialog);
        }
    }
}
