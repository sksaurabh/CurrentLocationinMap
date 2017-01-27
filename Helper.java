package com.mmadapps.ava.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.mmadapps.ava.planyourday.beans.PlaceName;
import com.mmadapps.ava.planyourday.beans.Plan_Data;

import com.mmadapps.ava.beans.FlightByDateAndFlightNo;
import com.mmadapps.ava.beans.FlightNames;
import com.mmadapps.ava.weather.beans.WeatherDetails;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

/**
 * Created by bhaskara.reddy on 04-01-2016.
 */

public class Helper extends SQLiteOpenHelper{

    private static String DB_PATH = "/data/data/com.mmadapps.ava/databases/";
    private static String DB_NAME = "AVADatabase.sqlite";
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    private String TAG = "Helper";
    Cursor cursorGetData;
    String sigment;

    public Helper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /**
     * creating database at first time of application Setup
     *
     * @throws IOException
     */
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (!dbExist) {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    /**
     * checking the database Availability based on Availability copying database
     * to the device data
     *
     * @return true (if Available)
     */
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            File file = new File(myPath);
            if (file.exists() && !file.isDirectory())
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
            Log.e(TAG, "Error is" + e.toString());
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    /**
     * copying database from asserts to package location in mobile data
     *
     * @throws IOException
     */
    private void copyDataBase() throws IOException {
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    /**
     * Opening database for retrieving/inserting information
     *
     * @throws SQLException
     */
    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null,SQLiteDatabase.OPEN_READWRITE);
    }

    /**
     * Closing database after operation done
     */
    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }

    /**
     * getting information based on SQL Query
     *
     * @param sql
     * @return Output of Query
     */
    public Cursor getData(String sql) {
        try {
            createDataBase();
            openDataBase();
            cursorGetData = getReadableDatabase().rawQuery(sql, null);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return cursorGetData;
    }

    public Cursor getData_cityname()
    {
        //Cursor c=myDataBase.query("city-name");
        //Cursor c=myDataBase.query(true,"city-name",null,null,null,null,null,null,null,null);
        String table_name="city_name";
        Cursor c=getData("SELECT * FROM "+table_name);
        return c;
    }


    public ArrayList<String> getData_cityname_auto()
    {

        ArrayList<String> citynamesList = null;
        Cursor cursor = getData("SELECT * FROM city_name");
        if(cursor != null || cursor.getCount() > 0){
            citynamesList = new ArrayList<String>();
            int len = cursor.getCount();
            cursor.moveToFirst();
            for(int i = 0; i<len; i++) {
                //FlightNames flightNames=new FlightNames();
                String name=cursor.getString(cursor.getColumnIndex("place_name"));
                String adress=cursor.getString(cursor.getColumnIndex("adress"));
                citynamesList.add(name+","+adress);
                //flightNames.setmName(cursor.getString(0));
               // flightNames.setmIata(cursor.getString(1));
                //flightNamesList.add(flightNames);
                cursor.moveToNext();
            }
        }
        return citynamesList;
    }

//getting stored stored planned data
    public ArrayList<Plan_Data> getData_plan_data()
    {

        ArrayList<Plan_Data> planned_datas = null;
        Cursor cursor = getData("SELECT * FROM plan_day_details ORDER BY _id DESC");
        if(cursor != null || cursor.getCount() > 0){
            planned_datas = new ArrayList<Plan_Data>();
            int len = cursor.getCount();
            Log.e("current count",len+"");
            cursor.moveToFirst();
            for(int i = 0; i<len; i++) {
                Plan_Data mPlan_data=new Plan_Data();
                //FlightNames flightNames=new FlightNames();
              //  String name=cursor.getString(cursor.getColumnIndex("place_name"));
               // citynamesList.add(name);
                mPlan_data.setdata_date(cursor.getString(cursor.getColumnIndex("date")));
                mPlan_data.setdata_time(cursor.getString(cursor.getColumnIndex("time")));
                mPlan_data.setdata_place(cursor.getString(cursor.getColumnIndex("destination")));
                mPlan_data.setdata_cab_flag(cursor.getString(cursor.getColumnIndex("cabflag")));
                mPlan_data.setdata_sync_flag(cursor.getString(cursor.getColumnIndex("sync_flag")));
                // flightNames.setmIata(cursor.getString(1));
                //flightNamesList.add(flightNames);
                planned_datas.add(mPlan_data);
                cursor.moveToNext();
            }
        }
        return planned_datas;
    }



    public ArrayList<Plan_Data> getData_post()  {

       //Helper helper=new Helper(this);
        try {
            createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        openDataBase();



        ArrayList<Plan_Data> planned_datas = null;
        String query="false";
        //Cursor cursor = getData("SELECT * FROM plan_day_details WHERE sync_flag ="+query);
        Cursor cursor = getData("SELECT * FROM plan_day_details");
       // if(cursor != null || cursor.getCount() > 0){
            if(cursor !=null)
            {
            planned_datas = new ArrayList<Plan_Data>();
            int len = cursor.getCount();
            Log.e("current count",len+"");
            cursor.moveToFirst();
            for(int i = 0; i<len; i++) {
                Plan_Data mPlan_data=new Plan_Data();
                //FlightNames flightNames=new FlightNames();
                //  String name=cursor.getString(cursor.getColumnIndex("place_name"));
                // citynamesList.add(name);
                mPlan_data.setdata_date(cursor.getString(cursor.getColumnIndex("date")));
                mPlan_data.setdata_time(cursor.getString(cursor.getColumnIndex("time")));
                mPlan_data.setdata_place(cursor.getString(cursor.getColumnIndex("destination")));
                mPlan_data.setdata_cab_flag(cursor.getString(cursor.getColumnIndex("cabflag")));
                mPlan_data.setdata_sync_flag(cursor.getString(cursor.getColumnIndex("sync_flag")));
                mPlan_data.setdata_user_id(cursor.getString(cursor.getColumnIndex("user_id")));
                // flightNames.setmIata(cursor.getString(1));
                //flightNamesList.add(flightNames);
                planned_datas.add(mPlan_data);
                cursor.moveToNext();
            }
        }
        return planned_datas;
    }




    public void updateStatus(){

        //String sycnValue="unsync_status";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            openDataBase();
            ContentValues initialValues = new ContentValues();
            initialValues.put("sync_flag", "true");
            updateData("plan_day_details", initialValues, null);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("update Qtn_details", "Error");
        }

    }







/*
    public ArrayList<String> getData_plan_data()
    {
        ArrayList<String> aa=new ArrayList<String>();
        //Cursor c=myDataBase.query("city-name");
        //Cursor c=myDataBase.query(true,"city-name",null,null,null,null,null,null,null,null);
        //String table_name="city_name";
        Cursor c=getData("SELECT * FROM city_name");


        while(c.moveToNext())
        {
           // Toast.makeText(PlanYourDayActivity.this,"starting", Toast.LENGTH_SHORT).show();
            String name = c.getString(c.getColumnIndex("place_name"));
            aa.add(name);
           // c.moveToNext()
        }
        return aa;
    }
    */


    /**
     * Inserting information based on table name and values
     *
     * @param tableName
     * @param values
     * @return
     */
    private long insertData(String tableName, ContentValues values) {
        try {
            openDataBase();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return myDataBase.insert(tableName, null, values);
    }




    public void insert_plan_Data(Plan_Data plan_data) {

        String place=plan_data.getdata_destination();
        //String month=plan_data.getdata_month();
        String time=plan_data.getdata_time();
        String date=plan_data.getdata_date();
        String cab=plan_data.getdata_cab_flag();
        String sync=plan_data.getdata_sync_flag();
        ContentValues cv=new ContentValues();
        cv.put("destination",place);
        cv.put("date",date);
        cv.put("time",time);
        cv.put("cabflag",cab);
        cv.put("sync_flag",sync);
        insertData("plan_day_details",cv);





    }



    public void insert_place_names(PlaceName placeName) {


        try {
            createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        openDataBase();

        String place_id=placeName.getPlace_id();
        String place_name=placeName.getPlace_name();
        String place_adress=placeName.getPlace_adress();
       // String sync_flag="false";
        int city_num=1;

        ContentValues cv=new ContentValues();
        cv.put("place_name",place_name);
        cv.put("_id",place_id);
        cv.put("adress",place_adress);
        cv.put("city_no",1);

        insertData("city_name",cv);





    }






    /**
     * Updating information based on table name and Condition
     *
     * @param tableName
     * @param values
     * @return
     */
    private int updateData(String tableName, ContentValues values, String condition) {
        try {
            openDataBase();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return myDataBase.update(tableName, values, condition, null);
    }


//--------------------------------Inserting Flight Details------------------------------------------------

    public void insertFlightDetails(ArrayList<FlightByDateAndFlightNo> flightNamesList){

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(tbl_flight_details,null,null);
            Log.e(TAG,tbl_flight_details+" Deleted successfully");
            int mTotalInsertedRows = 0;
            for(FlightByDateAndFlightNo flightNames : flightNamesList) {
                ContentValues cv = new ContentValues();
                cv.put("carrier_fscode",flightNames.getmCarrierFsCode());
                cv.put("flight_number",flightNames.getmFlightNumber());
                cv.put("departure_airport_fscode",flightNames.getmDepartureAirportFsCode());
                cv.put("arrival_airport_fscode",flightNames.getmArrivalAirportFsCode());
                cv.put("departure_time",flightNames.getmDepartureTime());
                cv.put("arrival_time",flightNames.getmArrivalTime());
                cv.put("airline_name",flightNames.getmAirLinename());
                long rowId = insertData(tbl_flight_details, cv);
                if (rowId > 0) {
                    mTotalInsertedRows++;
                }
            }
            Log.e(TAG,"InsertAllocation TotalRows"+mTotalInsertedRows);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //---------------------------------------Get Flight Names----------------------------------------


    public ArrayList<FlightNames> getFlightDetails(){

        ArrayList<FlightNames> flightNamesList = null;
        Cursor cursor = getData("SELECT name,iata FROM "+tbl_flight_names);
        if(cursor != null || cursor.getCount() > 0){
            flightNamesList = new ArrayList<FlightNames>();
            int len = cursor.getCount();
            cursor.moveToFirst();
            for(int i = 0; i<len; i++) {
                FlightNames flightNames=new FlightNames();
                flightNames.setmName(cursor.getString(0));
                flightNames.setmIata(cursor.getString(1));
                flightNamesList.add(flightNames);
                cursor.moveToNext();
            }
        }
        return flightNamesList;
    }

    //-------------------------------get Flight Trip Details-----------------------------------------------


    public ArrayList<FlightByDateAndFlightNo> getFlightTripDetails(){

        ArrayList<FlightByDateAndFlightNo> flightTripDetailsList = null;
        Cursor cursor = getData("SELECT * FROM "+tbl_flight_details);
        if(cursor != null || cursor.getCount() > 0){
            flightTripDetailsList = new ArrayList<FlightByDateAndFlightNo>();
            int len = cursor.getCount();
            cursor.moveToFirst();
            for(int i = 0; i<len; i++) {
                FlightByDateAndFlightNo flightTripDetails=new FlightByDateAndFlightNo();
                flightTripDetails.setmCarrierFsCode(cursor.getString(0));
                flightTripDetails.setmFlightNumber(cursor.getString(1));
                flightTripDetails.setmDepartureAirportFsCode(cursor.getString(2));
                flightTripDetails.setmArrivalAirportFsCode(cursor.getString(3));
                flightTripDetails.setmDepartureTime(cursor.getString(4));
                flightTripDetails.setmArrivalTime(cursor.getString(5));
                flightTripDetails.setmAirLinename(cursor.getString(6));
                flightTripDetailsList.add(flightTripDetails);
                cursor.moveToNext();
            }
        }
        return flightTripDetailsList;
    }

    public void exportDatabse() {
        try {

            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//com.mmadapps.ava//databases//AVADatabase.sqlite";
                // String currentDBPath = DB_PATH + DB_NAME;
                String backupDBPath = "AVADatabase_db.sqlite";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB)
                            .getChannel();
                    FileChannel dst = new FileOutputStream(backupDB)
                            .getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Log.e("db", "copied");
                } else {
                    Log.e("db", "dbnotexist");
                }
            } else {
                Log.e("db", "notcopied");
            }
        } catch (Exception e) {
            Log.e("db", "error");
        }
    }
// Tables

    //--------------------------get City Names-----------------------------------------------------


    public String getCityName(String citycode){
        String city="";
        Cursor cursor = getData("SELECT city FROM "+tbl_city_details+" WHERE city_code='"+citycode+"'");
        if(cursor != null || cursor.getCount() > 0){
                cursor.moveToFirst();
                city=cursor.getString(0);
        }
        return city;
    }



    private static final String tbl_flight_names = "tbl_flights_names";
    private static final String tbl_flight_details = "tbl_flight_details";
    private static final String tbl_city_details = "tbl_city_names";


    public void insertWeatherDetails(WeatherDetails mWeatherDetails) {
        SQLiteDatabase db = this.getWritableDatabase();
        int mTotalInsertedValues = 0;
        try {
            openDataBase();
            db.execSQL("DELETE FROM weather_deatils");
            Log.e("weather_deatils working", "Done");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("weather_deatils working", "Error");
        }
        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put("created", mWeatherDetails.getmCreated());
            initialValues.put("city",  mWeatherDetails.getmCity());
            initialValues.put("country",  mWeatherDetails.getmCountry());
            initialValues.put("region", mWeatherDetails.getmRegion());
            initialValues.put("windchill", mWeatherDetails.getmWindchill());
            initialValues.put("winddirection", mWeatherDetails.getmWinddirection());
            initialValues.put("windspeed", mWeatherDetails.getmWindspeed());
            initialValues.put("humidity",  mWeatherDetails.getmHumidity());
            initialValues.put("weekonedate", mWeatherDetails.getmWeekDetails().get(0).getmDate());
            initialValues.put("weekoneday", mWeatherDetails.getmWeekDetails().get(0).getmDay());
            initialValues.put("weekonehigh", mWeatherDetails.getmWeekDetails().get(0).getmHigh());
            initialValues.put("weekonetext", mWeatherDetails.getmWeekDetails().get(0).getmStatus());
            initialValues.put("weektwodate", mWeatherDetails.getmWeekDetails().get(1).getmDate());
            initialValues.put("weektwoday", mWeatherDetails.getmWeekDetails().get(1).getmDay());
            initialValues.put("weektwohigh", mWeatherDetails.getmWeekDetails().get(1).getmHigh());
            initialValues.put("weektwotext", mWeatherDetails.getmWeekDetails().get(1).getmStatus());
            initialValues.put("weekthreedate", mWeatherDetails.getmWeekDetails().get(2).getmDate());
            initialValues.put("weekthreeday", mWeatherDetails.getmWeekDetails().get(2).getmDay());
            initialValues.put("weekthreehigh", mWeatherDetails.getmWeekDetails().get(2).getmHigh());
            initialValues.put("weekthreetext",  mWeatherDetails.getmWeekDetails().get(2).getmStatus());
            initialValues.put("weekfourdate",  mWeatherDetails.getmWeekDetails().get(3).getmDate());
            initialValues.put("weekfourday", mWeatherDetails.getmWeekDetails().get(3).getmDay());
            initialValues.put("weekfourhigh", mWeatherDetails.getmWeekDetails().get(3).getmHigh());
            initialValues.put("weekfourtext",  mWeatherDetails.getmWeekDetails().get(3).getmStatus());
            initialValues.put("weekfivedate",  mWeatherDetails.getmWeekDetails().get(4).getmDate());
            initialValues.put("weekfiveday", mWeatherDetails.getmWeekDetails().get(4).getmDay());
            initialValues.put("weekfivehigh",  mWeatherDetails.getmWeekDetails().get(4).getmHigh());
            initialValues.put("weekfivetext", mWeatherDetails.getmWeekDetails().get(4).getmStatus());
            long rowId = insertData("weather_deatils", initialValues);
            if (rowId != 0) {
            }
            Log.e("weather_deatils", "Done" + mTotalInsertedValues);
        } catch (Exception e) {
            Log.e("weather_deatils", "Error");
            e.printStackTrace();
        }

    }

}
