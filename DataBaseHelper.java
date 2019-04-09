package com.example.lucifer.androidappforcrimereporting;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Lucifer on 12/9/2017.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    private static String DB_PATH = "/data/data/com.example.lucifer.androidappforcrimereporting/databases/";

    //replace this with name of your db file which you copied into asset folder
    private static String DB_NAME = "AppForCrimeReporting.db";
    public static final String TABLE_NAME = "IncidentReports";
    public  static  final String Table="UserProfile";
    public static final String COL_1 = "name";
    public static final String COL_2 = "address";
    public static final String COL_3 = "email";
    public static final String COL_4 = "Phone";
    public static final String COL_5 = "cnic";
    public static final String COL_6 = "Password";


    private SQLiteDatabase myDataBase;

    private final Context myContext;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DataBaseHelper(Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;
        this.createDataBase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }
    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase(){
        try {
            boolean dbExist = checkDataBase();

            if(dbExist==true){
                Toast.makeText(myContext, "Databse Created", Toast.LENGTH_SHORT).show();
                //do nothing - database already exist
                this.getReadableDatabase();


                copyDataBase();
            }else{
                //By calling this method and empty database will be created into the default system path
                //of your application so we are gonna be able to overwrite that database with our database.
                this.getReadableDatabase();


                copyDataBase();

            }
        }
        catch (Exception e) {

        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);


        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){
         //

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase(){

        try{
            //Open your local db as the input stream
            InputStream myInput = myContext.getAssets().open(DB_NAME);

            // Path to the just created empty db
            String outFileName = DB_PATH + DB_NAME;

            //Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);

            //transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer))>0){
                myOutput.write(buffer, 0, length);
            }

            //Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        }
        catch (Exception e) {
            //catch exception
        }
    }

    public SQLiteDatabase openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        return myDataBase;

    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
        { myDataBase.close();}

        super.close();

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public boolean insertData(String date,String time,String category,double longitude,double latitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,date);
        contentValues.put(COL_2,time);
        contentValues.put(COL_3,longitude);
        contentValues.put(COL_4,latitude);

        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }


    public boolean insertUsserData(String name,String address,String email,String phone,String cnic,String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,name);
        contentValues.put(COL_2,address);
        contentValues.put(COL_3,email);
        contentValues.put(COL_4,phone);
        contentValues.put(COL_5,cnic);
        contentValues.put(COL_6,password);
      // contentValues(COL_5,password);

        long result = db.insert(Table,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }


}
