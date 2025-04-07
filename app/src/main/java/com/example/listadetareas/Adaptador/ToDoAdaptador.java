package com.example.listadetareas.Adaptador;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listadetareas.AddNuevaTarea;
import com.example.listadetareas.MainActivity;
import com.example.listadetareas.Modelo.ToDoModel;
import com.example.listadetareas.R;
import com.example.listadetareas.Utility.DataBase;

import java.util.List;

public class ToDoAdaptador extends RecyclerView.Adapter<ToDoAdaptador.MiViewHolder> {


    private List<ToDoModel> mList;
    private MainActivity activity;
    private DataBase miDB;

    public ToDoAdaptador(DataBase miDB, MainActivity activity){
        this.activity = activity;
        this.miDB = miDB;
    }

    @NonNull
    @Override
    public MiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ventana_de_tareas, parent, false );
        return new MiViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MiViewHolder holder, int position) {
        final ToDoModel item = mList.get(position);
        holder.checkBox.setText(item.getTarea());
        holder.checkBox.setChecked(tooBoolean(item.getStatus()));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()){
                    miDB.subirStatus(item.getId(), 1);
                }else {
                    miDB.subirStatus(item.getId(), 0);
                }
            }
        });

    }

    public boolean tooBoolean(int num){
        return num!=0;
    }


    public Context getContext() {
        return activity;
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setTareas(List<ToDoModel> mList){
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void eliminarTarea(int position){
        ToDoModel item = mList.get(position);
        miDB.eliminarTarea(item.getId());

        mList.remove(position);
        notifyItemRemoved(position);
    }


    public void editItems(int position){
        ToDoModel item = mList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("Id", item.getId());
        bundle.putString("tarea", item.getTarea());

        AddNuevaTarea tarea = new AddNuevaTarea();
        tarea.setArguments(bundle);
        tarea.show(activity.getSupportFragmentManager(), tarea.getTag());
    }

    public static class  MiViewHolder extends RecyclerView.ViewHolder{


        CheckBox checkBox;

        public MiViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}
