package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sagor Ahamed on 3/30/2018.
 */

public class MyDBHandler extends SQLiteOpenHelper {

    public static final String DBName="Tictactoe";
    public static final String TableName="Info";
    public static final String Col1="Col1";
    public static final String Col2="Col2";
    public static final int DBVersion=1;

     /*   Create Table*/
     //Create Table Info(ID integer primary key autoincrement, col1 text, col2 text );
     public static  final String CreateTable="CREATE TABLE "+TableName+"(ID INTEGER PRIMARY KEY AUTOINCREMENT,"+Col1+" TEXT,"+Col2+" TEXT)";


    MyDBHandler(Context context){
        super(context,DBName,null,DBVersion);
    }

    public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CreateTable);
            }catch (Exception e){

            }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + CreateTable);
            onCreate(db);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void insertData(){
        SQLiteDatabase db = this.getWritableDatabase();

    }
}
