package com.reimondpc.gpad.Adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.ContentView;

public class AdaptadorBD extends SQLiteOpenHelper {
    public static final String TABLE_ID = "idNote";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";

    private static final String DATABASE = "Note";
    private static final String TABLE = "notes";

    public AdaptadorBD(Context context) {
        super(context, DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " (" +
                TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TITLE + " TEXT," + CONTENT + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    //Metodo para Crear una nota
    public void addNote(String title, String content){
        ContentValues valores = new ContentValues();
        valores.put(TITLE, title);
        valores.put(CONTENT, content);
        this.getWritableDatabase().insert(TABLE,null,valores);
    }

    //Metodo para obtener una nota
    public Cursor getNote(String condition){
        String columnas[] = {TABLE_ID, TITLE, CONTENT};
        String [] args = new String [] {condition};
        Cursor c = this.getReadableDatabase().query(TABLE, columnas, TITLE + "=?", args, null, null, null);
        return c;
    }

    //Metodo para eliminar las notas
    public void deleteNote(String condition){
        String args[] = {condition};
        this.getWritableDatabase().delete(TABLE, TITLE + "=?", args);
    }

    //Metodo para actualizar las notas
    public void updateNote(String title, String content, String condition){
        String args[] = {condition};
        ContentValues valores = new ContentValues();
        valores.put(TITLE, title);
        valores.put(CONTENT, content);
        this.getWritableDatabase().update(TABLE, valores,TITLE + "=?", args);
    }

    //Metodo para devolver las notas
    public Cursor getNotes(){
        String columnas[] = {TABLE_ID, TITLE, CONTENT};
        Cursor c = this.getReadableDatabase().query(TABLE, columnas, null, null, null, null, null);
        return c;
    }

    public void deleteNotes(){
        this.getWritableDatabase().delete(TABLE, null, null);
    }
}
