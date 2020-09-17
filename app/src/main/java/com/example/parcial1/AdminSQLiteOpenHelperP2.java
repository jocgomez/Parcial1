package com.example.parcial1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminSQLiteOpenHelperP2 extends SQLiteOpenHelper {

    public AdminSQLiteOpenHelperP2(Context context, String nombre, CursorFactory
            factory, int version) {
        super(context, nombre, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table pacientes2(correo text primary key, nombre text, edad integer, sexo text, tiempo text, nHemo double, anemia text)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnte, int versionNue) {
        db.execSQL("drop table if exists pacientes2");
        db.execSQL("create table pacientes2(correo text primary key, nombre text, edad integer, sexo text, tiempo text, nHemo double, anemia text)");
    }

}
