package com.trungquach.mydaiary.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by trungqn on 12/21/2017.
 */

public class NoteSQLiteHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "mydiary.db";
    public static final String TABLE_NAME = "mydiaries";
    //columns
    public static final String COL_ID = "_id";
    public static final String COL_TITLE = "title";
    public static final String COL_CONTENT = "content";
    public static final String COL_IMAGE= "image";
    public static final String COL_DATETIME = "datetime";
    //DB info
    public static final int DB_VERSION = 1;
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( "
            + COL_ID + " INTEGER PRIMARY KEY autoincrement, "
            + COL_TITLE + " text not null, "
            + COL_CONTENT + " text not null, "
            + COL_DATETIME + " text not null, "
            + COL_IMAGE + " text);";

    public NoteSQLiteHelper (Context ctx){
        super(ctx, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + TABLE_NAME);
        onCreate(db);
    }
}
