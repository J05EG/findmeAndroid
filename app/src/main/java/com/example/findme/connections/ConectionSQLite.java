package com.example.findme.connections;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.findme.Utils.ConstantSQLite;

public class ConectionSQLite extends SQLiteOpenHelper {

    public ConectionSQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ConstantSQLite.CREAR_TABLA_USUARIO);
        db.execSQL(ConstantSQLite.CREAR_TABLA_MAIN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ConstantSQLite.TABLA_USUARIO);
        db.execSQL("DROP TABLE IF EXISTS "+ConstantSQLite.TABLA_MAIN_USER);
    }

    public void deleteAll(SQLiteDatabase db) {
        db.delete(ConstantSQLite.TABLA_USUARIO, null, null);
        db.delete(ConstantSQLite.TABLA_MAIN_USER, null, null);
    }
}
