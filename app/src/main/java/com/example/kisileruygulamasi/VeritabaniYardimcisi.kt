package com.example.kisileruygulamasi

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/*
Burası veri tabanı tablolarının oluşturulduğu satırların sütunların oluşturulduğu yerdir.
*/

class VeritabaniYardimcisi(context: Context) : SQLiteOpenHelper(context,"rehber.sqlite",null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE kisiler (kisi_id INTEGER PRIMARY KEY AUTOINCREMENT, kisi_ad TEXT, kisi_tel TEXT);")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS kisiler")
        onCreate(db)
    }


}