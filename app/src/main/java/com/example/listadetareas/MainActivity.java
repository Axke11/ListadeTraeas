package com.example.listadetareas;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listadetareas.Adaptador.ToDoAdaptador;
import com.example.listadetareas.Modelo.ToDoModel;
import com.example.listadetareas.Utility.DataBase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDialogCloseListener {

    RecyclerView recyclerView;
    FloatingActionButton addBoton;
    DataBase midb;
    private List<ToDoModel> mList;
    private ToDoAdaptador adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        addBoton = findViewById(R.id.addButton);
        midb = new DataBase(MainActivity.this);
        mList = new ArrayList<>();
        adaptador = new ToDoAdaptador(midb, MainActivity.this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptador);


        mList = midb.getAllTareas();
        Collections.reverse(mList);
        adaptador.setTareas(mList);

        addBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNuevaTarea.newInstance().show(getSupportFragmentManager(), AddNuevaTarea.TAG);
            }
        });


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(adaptador));
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList = midb.getAllTareas();
        Collections.reverse(mList);
        adaptador.setTareas(mList);
        adaptador.notifyDataSetChanged();
    }
}