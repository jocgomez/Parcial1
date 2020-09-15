package com.example.parcial1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminSQLiteOpenHelperP1 extends SQLiteOpenHelper{
    public AdminSQLiteOpenHelperP1(Context context, String nombre, CursorFactory
            factory, int version) {
        super(context, nombre, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table pacientes(cedula integer primary key, nombre text, eps text, patologia text, tipoPatologia text, sintoma text)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnte, int versionNue) {
        db.execSQL("drop table if exists pacientes");
        db.execSQL("create table pacientes(cedula integer primary key, nombre text, eps text, patologia text, tipoPatologia text, sintoma text)");
    }
}
