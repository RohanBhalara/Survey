package com.example.survey;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SurveyDatabase";

    public DBHelper(Context context) {
        super(context,DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE forms (id integer, formsList text)");
        db.execSQL("CREATE TABLE formsData (formNumber integer, formdata text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS forms");
        db.execSQL("DROP TABLE IF EXISTS formsData");
        onCreate(db);
    }

    public boolean insertForms(int id, String formsList){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",id);
        contentValues.put("formsList",formsList);
        db.insert("forms",null, contentValues);
        return true;
    }

    public String getForms(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM forms where id = "+id,null);

        if((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            String formsList = cursor.getString(1);
            return formsList;
        }
        else{
            return null;
        }
    }

    public boolean updateForms(int id, String formsList){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",id);
        contentValues.put("formsList",formsList);
        db.update("forms",contentValues,"id = ?", new String[] {Integer.toString(id)});
        return true;
    }

    public boolean insertFormData(int formNumber, String formdata){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("formNumber", formNumber);
        contentValues.put("formdata", formdata);
        db.insert("formsData", null, contentValues);
        return true;
    }

    public String getFormData(int formNumber){
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM formsData where formNumber = " + formNumber, null);
            if((cursor != null) && (cursor.getCount() > 0)){
                cursor.moveToFirst();
                String formAllData = cursor.getString(1);
                return formAllData;
            }
            else{
                return null;
            }
        }
        catch (Exception e){
            Log.d("DEBUG", e.getMessage());
        }
        finally {
            return null;
        }
    }

    public boolean updateFormData(int formNumber, String formData){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("formNumber", formNumber);
        contentValues.put("formdata", formData);
        db.update("formsData",contentValues,"formNumber = ?", new String[] {Integer.toString(formNumber)});
        return true;
    }
}
