package com.trungquach.mydaiary.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.trungquach.mydaiary.model.NoteModel;

import java.util.ArrayList;

/**
 * Created by trungqn on 12/21/2017.
 */

public class NoteDataSource {
    private SQLiteOpenHelper sqLiteOpenHelper;
    private SQLiteDatabase sqLiteDatabase;
    private String[] allCols = {
            NoteSQLiteHelper.COL_ID,
            NoteSQLiteHelper.COL_TITLE,
            NoteSQLiteHelper.COL_CONTENT,
            NoteSQLiteHelper.COL_DATETIME,
            NoteSQLiteHelper.COL_IMAGE
    };

    private Context ctx;
    public NoteDataSource(Context ctx) {
        this.ctx = ctx;
        sqLiteOpenHelper = new NoteSQLiteHelper(ctx);
    }

    public void open() throws SQLiteException {
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
    }
    public void close() throws SQLiteException {
        sqLiteOpenHelper.close();
    }

    public void addNewNote (String title, String note, String image, String dateTime) {
        ContentValues values = new ContentValues();
        values.put(NoteSQLiteHelper.COL_TITLE, title);
        values.put(NoteSQLiteHelper.COL_CONTENT, note);
        values.put(NoteSQLiteHelper.COL_IMAGE, image);
        values.put(NoteSQLiteHelper.COL_DATETIME, dateTime);
        // insert
        sqLiteDatabase.insert(NoteSQLiteHelper.TABLE_NAME,null,values);
        Toast.makeText(this.ctx, "Create note Success",Toast.LENGTH_LONG);
    }

    public void deteleNote(int id){
        sqLiteDatabase.delete(NoteSQLiteHelper.TABLE_NAME,NoteSQLiteHelper.COL_ID+" = "+ id, null);
        Toast.makeText(this.ctx, "Delete note Success",Toast.LENGTH_LONG);
    }

    public ArrayList<NoteModel> getAllNotes(){
        ArrayList<NoteModel> arr = new ArrayList<>();

        Cursor cursor = sqLiteDatabase.query(NoteSQLiteHelper.TABLE_NAME,allCols,null,null,null,null,null);
        if(cursor == null)
            return null;

        //having notes
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            NoteModel note = cursorToModel(cursor);
            arr.add(note);
            cursor.moveToNext();
        }
        return arr;
    }

    private NoteModel cursorToModel(Cursor cursor) {
        NoteModel note = new NoteModel();
        note.setId(cursor.getInt(0));
        note.setTitle(cursor.getString(1));
        note.setContent(cursor.getString(2));
        note.setDateTime(cursor.getString(3));
        note.setImage(cursor.getString(4));

        return note;
    }

    public NoteModel getNote(int id){
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+ NoteSQLiteHelper.TABLE_NAME+" WHERE "+NoteSQLiteHelper.COL_ID+" = ?", new String[] {Integer.toString(id)});
        if(cursor == null)
            return null;

        //having notes
        NoteModel note = null;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            note = cursorToModel(cursor);
            cursor.moveToNext();
        }
        return note;
    }
}
