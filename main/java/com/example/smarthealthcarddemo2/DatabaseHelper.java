package com.example.smarthealthcarddemo2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Blob;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="Patient.db";
    public static final String Table_Name="Patient_Table";
    public static final String Col_0="ID";
    public static final String Col_1="Name";
    public static final String Col_2="Age";
    public int flag = 0;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ Table_Name+" (ID Integer primary key not null,Name Text, Date Text,Prescriptions blob not null )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists Patient_Table");
        onCreate(db);
    }
    public boolean insertData(String Name, String age,String x,Integer i){
        try {
            FileInputStream fs=new FileInputStream(x);
            byte[] imgByte=new byte[fs.available()];
            fs.read(imgByte);
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(Col_1, Name);
            contentValues.put(Col_2, age);
            contentValues.put("Prescriptions",imgByte);
            long result = db.insert(Table_Name, null, contentValues);
            fs.close();
            if (result == -1)
                return false;
            else
                return true;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("Select * from " + Table_Name, null);
        return res;
    }

    public boolean updateData(String ID,String Name,String age){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_0,ID);
        contentValues.put(Col_1,Name);
        contentValues.put(Col_2,age);
        db.update(Table_Name,contentValues,"ID = ?",new String[]{ ID});
        return true;
    }
    public Integer deleteData(String ID){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(Table_Name,"ID = ?",new String[]{ID});
    }
    public Bitmap getImage(Integer ID){
        SQLiteDatabase db=this.getWritableDatabase();
        Bitmap bt=null;
        Cursor cursor=db.rawQuery("select * from "+Table_Name+" where id=?",new String[]{String.valueOf(ID)});
        if(cursor.moveToNext()){
            byte []imag=cursor.getBlob(3);
            bt= BitmapFactory.decodeByteArray(imag,0,imag.length);
        }
        return bt;
    }
}