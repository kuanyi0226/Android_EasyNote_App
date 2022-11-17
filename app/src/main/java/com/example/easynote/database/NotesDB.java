package com.example.easynote.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by flyan on 6/19/18.
 */

public class NotesDB extends SQLiteOpenHelper {
    public final static String DB_NAME = "notes.db";
    public final static int DB_VERSION = 1;

    //数据库里需要的东西
    public final static String TABLE_NAME = "notes";
    public final static String ID = "_id";
    public final static String CONTENT = "content";
    public final static String PATH = "path";
    public final static String VIDEO = "video";
    public final static String TIME = "time";

    public NotesDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createSQL = "create table " + TABLE_NAME + "(" +
                ID + " integer primary key autoincrement," +
                CONTENT + " text," +
                PATH + " text," +
                VIDEO + " text," +
                TIME + " text" +
                ")";
        db.execSQL(createSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
