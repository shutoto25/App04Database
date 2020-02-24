package com.example.app004database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class TestOpenHelper extends SQLiteOpenHelper {
    /**
     * version.
     */
    public static final int DATABASE_VERSION = 1;
    /**
     * Database name.
     */
    public static final String DATABASE_NAME = "testDB.db";
    /**
     * Table name.
     */
    public static final String TABLE_NAME = "testDB";
    /**
     * Column id.
     */
    public static final String ID = "id";
    /**
     * Column name title(社員番号).
     */
    public static final String COLUMN_NAME_TITLE = "number";
    /**
     * Column name subTitle(名前).
     */
    public static final String COLUMN_NAME_SUB_TITLE = "name";

    /**
     * コンストラクタ.
     */
    TestOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(
                "CREATE TABLE " +
                        TABLE_NAME + " (" +
                        ID + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME_TITLE + " INTEGER UNIQUE," +
                        COLUMN_NAME_SUB_TITLE + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

    @Override
    public void onDowngrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        onUpgrade(database, oldVersion, newVersion);
    }

}
