package com.example.listadetareas.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.listadetareas.Modelo.ToDoModel;

import java.util.ArrayList;
import java.util.List;


public class DataBase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "TODO_DATABASE";
    private static final String TABLE_NAME = "TODO_TABLE";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "TAREA";
    private static final String COL_3 = "STATUS";
    private static final int DATABASE_VERSION = 1;

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +"(ID INTEGER PRIMARY KEY AUTOINCREMENT, TAREA TEXT, STATUS INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void instertTarea(ToDoModel model){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, model.getTarea());
        contentValues.put(COL_3, 0);

        db.insert(TABLE_NAME, null, contentValues);
    }

    public void subirTarea(int id, String tarea){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, tarea);

        db.update(TABLE_NAME, contentValues, "ID=?", new String[]{String.valueOf(id)});
    }

    public void subirStatus(int id, int status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_3, status);
        db.update(TABLE_NAME, contentValues, "ID=?", new String[]{String.valueOf(id)});
    }

    public void eliminarTarea(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "ID=?", new String[]{String.valueOf(id)});
    }

    public List<ToDoModel> getAllTareas(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        List<ToDoModel> modelList =new ArrayList<>();

        db.beginTransaction();
        try{
            cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
            if (cursor!= null){
                if (cursor.moveToFirst()){
                    do {
                        ToDoModel toDoModel = new ToDoModel();
                        toDoModel.setId(cursor.getInt(cursor.getColumnIndex(COL_1)));
                        toDoModel.setTarea(cursor.getString(cursor.getColumnIndex(COL_2)));
                        toDoModel.setStatus(cursor.getInt(cursor.getColumnIndex(COL_3)));
                        modelList.add(toDoModel);
                    }while (cursor.moveToNext());
                }
            }
        }finally {
            db.endTransaction();
            cursor.close();
        }
        return modelList;
    }


}
