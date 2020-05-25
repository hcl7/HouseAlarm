package com.example.alarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
 
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

@SuppressLint("Registered")
public class pafap_db extends SQLiteOpenHelper {
	final Context context = null;
	private String uid;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "alarmManager";
    
    //users table
    private static final String TABLE_USERS = "users";
    private static final String UID = "uid";
    private static final String USER = "user";
    private static final String PASSWORD = "pass";
    private static final String DATE_CREATED = "date_created";
    private static final String STATUS = "status";
    
    //alarms table
    private static final String TABLE_ALARM = "alarms";
    private static final String KEY_ID = "id";
    private static final String KEY_UID = "uid";
    private static final String KEY_NAME = "name";
    private static final String KEY_BRAND = "brand";
    private static final String KEY_MODEL = "model";
    private static final String KEY_PH_NO = "phone";
    private static final String KEY_PASS = "pass";
    private static final String KEY_COLOR = "stcolor";
    
    //brand table
    private static final String TABLE_BRAND = "brands";
    private static final String BRAND_ID = "id";
    private static final String BRAND_NAME = "brand";
    private static final String BRAND_MODEL = "model";
    
    //model command table
    private static final String TABLE_COMMAND = "commands";
    private static final String COMM_ID = "id";
    private static final String COMM_MODEL = "model";
    private static final String COMM_NAME = "name";
    private static final String COMM_COMMAND = "command";
    
    //alarm command table
    private static final String ALARM_COMMAND = "alcommands";
    private static final String AL_COMM_ID = "id";
    private static final String AL_COMM_AID = "aid";
    private static final String AL_COMM_MODEL = "model";
    private static final String AL_COMM_NAME = "name";
    private static final String AL_COMM_COMMAND = "command";
    private static final String AL_COMM_STATUS = "status";
    private static final String AL_COMM_COLOR = "stcolor";
    
    //alarms log
    private static final String TABLE_ALARM_LOG = "logs";
    private static final String LOG_ID = "id";
    private static final String LOG_UID = "uid";
    private static final String LOG_SIM = "sim";
    private static final String LOG_SMS = "sms";
    private static final String LOG_DATE = "date_created";
    private static final String LOG_COLOR = "stcolor";
    
    public String error = null;
 
    public pafap_db(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Create Table
    @Override
    public void onCreate(SQLiteDatabase db) {
    	String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + "("
    			+ UID + " INTEGER PRIMARY KEY," + USER + " TEXT UNIQUE,"
    			+ PASSWORD + " TEXT," + DATE_CREATED + " TEXT," + STATUS + " TEXT" + ")";
    	db.execSQL(CREATE_USERS_TABLE);
    	
        String CREATE_ALARMS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ALARM + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_UID + " INTEGER REFERENCES " + TABLE_USERS + "(" + UID + ")," + KEY_NAME + " TEXT UNIQUE," + KEY_BRAND + " TEXT," 
        		+ KEY_MODEL + " TEXT," + KEY_PH_NO + " TEXT," +  KEY_PASS + " TEXT, " + KEY_COLOR + " TEXT" + ")";
        db.execSQL(CREATE_ALARMS_TABLE);
        
        String CREATE_BRAND_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_BRAND + "("
        		+ BRAND_ID + " INTEGER PRIMARY KEY," + BRAND_NAME + " TEXT," 
        		+ BRAND_MODEL + " TEXT" + ")";
        db.execSQL(CREATE_BRAND_TABLE);
        
        String CREATE_COMMAND_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_COMMAND + "("
        		+ COMM_ID + " INTEGER PRIMARY KEY," + COMM_MODEL + " TEXT,"
        		+ COMM_NAME + " TEXT," + COMM_COMMAND + " TEXT" + ")";
        db.execSQL(CREATE_COMMAND_TABLE);
        
        String CREATE_COMMAND_ALARMS = "CREATE TABLE IF NOT EXISTS " + ALARM_COMMAND + "("
        		+ AL_COMM_ID + " INTEGER PRIMARY KEY," + AL_COMM_AID + " INTEGER REFERENCES " + TABLE_ALARM + "(" + KEY_ID + ")," + AL_COMM_MODEL + " TEXT,"
        		+ AL_COMM_NAME + " TEXT," + AL_COMM_COMMAND + " TEXT," + AL_COMM_STATUS + " TEXT," + AL_COMM_COLOR + " TEXT" +")";
        db.execSQL(CREATE_COMMAND_ALARMS);
        
        String CREATE_LOG_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ALARM_LOG + "("
        		+ LOG_ID + " INTEGER PRIMARY KEY," + LOG_UID + " INTEGER REFERENCES " + TABLE_USERS + "(" + UID + ")," + LOG_SIM + " TEXT," 
        		+ LOG_SMS + " TEXT," + LOG_DATE + " TEXT," + LOG_COLOR + " TEXT" + ")";
        db.execSQL(CREATE_LOG_TABLE);

    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BRAND);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMAND);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARM_LOG);
        // Create tables again
        onCreate(db);
    }
    
    /**
     * All(Create, Read, Update, Delete) Operations
     */
    
    //add new brand of alarm;
    void addContents(ArrayList<TypeModel> tm){
    	SQLiteDatabase db = this.getWritableDatabase();
    	try{
    		for(int i=0;i<tm.size();i++){
    			ContentValues values = new ContentValues();
        		values.put(BRAND_NAME, tm.get(i).getBrand().toString());
        		values.put(BRAND_MODEL, tm.get(i).getModel().toString());
        		db.insert(TABLE_BRAND, null, values);
    		}
    	}
    	catch (SQLiteException ex){
    		error = "db error addContents!" + ex.getMessage();
    	}
    	finally{
    		db.close();
    	}
    }
    
    //add user contents on db
    void addUsrContents(ArrayList<usrAlarm> ut){
    	SQLiteDatabase db = this.getWritableDatabase();
    	try{
    		for(int i=0;i<ut.size();i++){
    			ContentValues values = new ContentValues();
        		values.put(USER, ut.get(i).getUser().toString());
        		values.put(PASSWORD, ut.get(i).getPass().toString());
        		db.insert(TABLE_USERS, null, values);
    		}
    	}
    	catch (SQLiteException ex){
    		error = "db error addUsrContents!" + ex.getMessage();
    	}
    	finally{
    		db.close();
    	}
    }
    
    //add model commands from array list
    void addContentsFromArraylist(ArrayList<ModelCommand> mc){
    	SQLiteDatabase db = this.getWritableDatabase();
    	try{;
    		for(int i=0;i<mc.size();i++){
    			ContentValues values = new ContentValues();
        		values.put(COMM_MODEL, mc.get(i).getModel().toString());
        		values.put(COMM_NAME, mc.get(i).getName().toString());
        		values.put(COMM_COMMAND, mc.get(i).getCommand().toString());
        		db.insert(TABLE_COMMAND, null, values);
    		}
    	}
    	catch (SQLiteException ex){
    		error = "db error addContentsFromArraylist!" + ex.getMessage();
    	}
    	finally{
    		db.close();
    	}
    }
    // add alarm commands from arraylist;
    void addAlarmCommandsFromArraylist(ArrayList<ModelCommand> mc, int aid){
    	SQLiteDatabase db = this.getWritableDatabase();
    	try{;
    		for(int i=0;i<mc.size();i++){
    			ContentValues values = new ContentValues();
    			values.put(AL_COMM_AID, aid);
        		values.put(AL_COMM_MODEL, mc.get(i).getModel().toString());
        		values.put(AL_COMM_NAME, mc.get(i).getName().toString());
        		values.put(AL_COMM_COMMAND, mc.get(i).getCommand().toString());
        		values.put(AL_COMM_STATUS, "ACTIVATED");
        		values.put(AL_COMM_COLOR, "#909090");
        		db.insert(ALARM_COMMAND, null, values);
    		}
    	}
    	catch (SQLiteException ex){
    		error = "db error addAlarmCommandsFromArraylist!" + ex.getMessage();
    	}
    	finally{
    		db.close();
    	}
    }
 
    // Adding new alarm
    void addAlarmContent(alarm al) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	try{
    		ContentValues values = new ContentValues();
    		values.put(KEY_UID, al.getID());
    		values.put(KEY_NAME, al.getName());
    		values.put(KEY_BRAND, al.getBrand());
    		values.put(KEY_MODEL, al.getModel());
    		values.put(KEY_PH_NO, al.getPhoneNumber());
    		values.put(KEY_PASS, al.getPassword());
    		values.put(KEY_COLOR, al.getColor());
    		db.insert(TABLE_ALARM, null, values);
    	}
    	catch (SQLiteException ex){
    		error = "db error addAlarmContent!" + ex.getMessage();
    	}
    	finally{
    		db.close();
    	}
    }
 
    // Getting single alarm
    alarm getAlarmContent(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_ALARM, new String[] { KEY_ID, KEY_UID, KEY_NAME,
                KEY_BRAND, KEY_MODEL, KEY_PH_NO, KEY_PASS }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        alarm amalarm = new alarm(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
        return amalarm;
    }
    
    //get single value from table
    public Cursor getTableContent(String table, String[] fields, String where, String[] wherevalue){
    	Cursor cursor = null;
    	SQLiteDatabase db = this.getReadableDatabase();
    	cursor = db.query(table, fields, where + "=?", wherevalue, null, null, null, null);
    	return cursor;
    }
    
    //get multi values from table
    public Cursor getTableContents(String query){
    	Cursor cursor = null;
    	SQLiteDatabase db = this.getReadableDatabase();
    	cursor = db.rawQuery(query, null);
    	return cursor;
    }
     
    // Getting All Content
    public List<alarm> getAllAlarmContent() {
        List<alarm> alarmList = new ArrayList<alarm>();
        String selectQuery = "SELECT * FROM " + TABLE_ALARM;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
            	alarm alrm = new alarm();
                alrm.setID(Integer.parseInt(cursor.getString(0)));
                alrm.setName(cursor.getString(1));
                alrm.setBrand(cursor.getString(2));
                alrm.setModel(cursor.getString(3));
                alrm.setPhoneNumber(cursor.getString(4));
                alrm.setPassword(cursor.getString(5));
                alarmList.add(alrm);
            } while (cursor.moveToNext());
        }
        return alarmList;
    }
    
 // Get command by alarm model;
    public ArrayList<ModelCommand> getCommandsByModel(String model) {
        ArrayList<ModelCommand> ACList = new ArrayList<ModelCommand>();
        String selectQuery = "SELECT model, name, command FROM " + TABLE_COMMAND + " WHERE " + COMM_MODEL + " = '" + model + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
            	ModelCommand Calarm = new ModelCommand();
            	Calarm.setModel(cursor.getString(0));
            	Calarm.setName(cursor.getString(1));
            	Calarm.setCommand(cursor.getString(2));
            	ACList.add(Calarm);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ACList;
    }
 
    // Updating single
    public int updateAlarmContent(alarm al) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, al.getName());
        values.put(KEY_BRAND, al.getBrand());
        values.put(KEY_MODEL, al.getModel());
        values.put(KEY_PH_NO, al.getPhoneNumber());
        values.put(KEY_PASS, al.getPassword());
 
        // updating row
        return db.update(TABLE_ALARM, values, KEY_ID + " = ?",
                new String[] { String.valueOf(al.getID()) });
    }
    
    //update alarm color
    public int updateAlarmColor(String color, String sim) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_COLOR, color);
        // updating field
        return db.update(TABLE_ALARM, values, KEY_PH_NO + " = ?",
                new String[] { sim });
    }
    
    //update alarm command color
    public int updateAlarmCommandColor(String color, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(AL_COMM_COLOR, color);
        // updating field
        return db.update(ALARM_COMMAND, values, AL_COMM_NAME + " = ?",
                new String[] { name });
    }
    
    //update logs color
    public int updateLogsColor(String color, String sim, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(LOG_COLOR, color);
        // updating field
        return db.update(TABLE_ALARM_LOG, values, (LOG_SIM + " = ? AND " + LOG_ID + " = " + id),
                new String[] { sim });
    }
    
    //update table field;
    public void updateTableField(String table, String field, String value, String where, int id){
    	SQLiteDatabase db = this.getWritableDatabase();
    	try{
    		ContentValues values = new ContentValues();
        	values.put(field, value);
        	db.update(table, values, where + " = ?", new String[] {String.valueOf(id)});
    	}catch (SQLiteException ex){
    		error = "updateTableField error!" + ex.getMessage();
    	}finally{
    		db.close();
    	}
    }
    
    //get table field;
    public String getTableField(String table, String field, String where, int id){
    	SQLiteDatabase db = this.getWritableDatabase();
    	String value = "";
    	try{
    		String selectQuery = "SELECT " + field + " FROM " + table + " WHERE " + where + " = '" + id + "' LIMIT 1";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                	value = cursor.getString(0);
                } while (cursor.moveToNext());
            }
    	}catch (SQLiteException ex){
    		error = "updateTableField error!" + ex.getMessage();
    	}finally{
    		db.close();
    	}
    	return value;
    }
 
    // Deleting single alarm by int;
    public void deleteAlarmContent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ALARM, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }
    
    // deleting single alarm command by int
    public void deleteAlarmCommContent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ALARM_COMMAND, AL_COMM_AID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }
    
    //Deleting single alarm by string;
    public void deleteAlarmContent(String key) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ALARM, KEY_NAME + " = ?",
                new String[] { String.valueOf(key) });
        db.close();
    }
    
    //delete table content by id;
    public void deleteTableContent(String table, String field, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table, field + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }
    
    //delete alarm logs my sim;
    public void deleteAlarmLogsContent(String key) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ALARM_LOG, LOG_SIM + " = ?",
                new String[] { String.valueOf(key) });
        db.close();
    }
    // Getting Count
    public int getAlarmContentCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ALARM;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }
    
    public void deleteTable(String table){
    	SQLiteDatabase db = this.getWritableDatabase();
    	db.execSQL("DROP TABLE IF EXISTS " + table);
    }
    
    public boolean Exists() {
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BRAND, null);
   	    boolean exists = (cursor.getCount() > 0);
    	cursor.close();
    	return exists;
    }
    
    public boolean alExists() {
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ALARM, null);
   	    boolean exists = (cursor.getCount() > 0);
    	cursor.close();
    	return exists;
    }
    
    public boolean usrExists(String usr, String pass) {
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery("SELECT " + UID + ", " + USER + ", " + PASSWORD + " FROM " + TABLE_USERS + " WHERE " + USER + " = '" + usr + "' AND " + PASSWORD + " = '" + pass + "'", null);
   	    boolean exists = (cursor.getCount() > 0);
   	    if (cursor.moveToFirst()){
   	    	do {
   	    		int usrid = Integer.parseInt(cursor.getString(0));
   	    		uid = String.valueOf(usrid);
   	    	}while (cursor.moveToNext());
   	    }
    	cursor.close();
    	db.close();
    	return exists;
    }
    
    public boolean checkField(String table, String field, String value) {
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery("SELECT " + field + " FROM " + table + " WHERE " + field + " = '" + value + "'", null);
   	    boolean exists = (cursor.getCount() > 0);
    	cursor.close();
    	db.close();
    	return exists;
    }
    
    //add alarms log
    void addAlarmLog(int uid, String sim, String sms) {
    	String now = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
    	SQLiteDatabase db = this.getWritableDatabase();
    	try{
    		ContentValues values = new ContentValues();
    		values.put(LOG_UID, uid);
    		values.put(LOG_SIM, sim);
    		values.put(LOG_SMS, sms);
    		values.put(LOG_DATE, now);
    		values.put(LOG_COLOR, "#909090");
    		db.insert(TABLE_ALARM_LOG, null, values);
    	}
    	catch (SQLiteException ex){
    		error = "db error addAlarmLog!" + ex.getMessage();
    	}
    	finally{
    		db.close();
    	}
    }
    
    int getAlarmUidBySim(String sim){
    	SQLiteDatabase db = this.getReadableDatabase();
    	int usrid = 0;
    	String sign = "%";
    	Cursor cursor = db.rawQuery("SELECT " + KEY_UID + " FROM " + TABLE_ALARM + " WHERE " + KEY_PH_NO + " LIKE '" + sign + sim + sign + "'", null);
   	    if (cursor.moveToFirst()){
   	    	do {
   	    		usrid = Integer.parseInt(cursor.getString(0));
   	    	}while (cursor.moveToNext());
   	    }
    	cursor.close();
    	return usrid;
    }
    
    String getLastLogByUid(int uid){
    	SQLiteDatabase db = this.getReadableDatabase();
    	String usrid = "";
    	Cursor cursor = db.rawQuery("SELECT " + LOG_SMS + " FROM " + TABLE_ALARM_LOG + " WHERE " + LOG_UID + " = '" + uid + "'", null);
   	    if (cursor.moveToFirst()){
   	    	do {
   	    		usrid = cursor.getString(0);
   	    	}while (cursor.moveToNext());
   	    }
    	cursor.close();
    	return usrid;
    }
    
    public String getUID(){
    	return this.uid;
    }
    
    public String getDBError(){
    	return this.error;
    }
    
    public int getLastId(String id, String table){
    	SQLiteDatabase db = this.getReadableDatabase();
    	int lid = 0;
    	Cursor cursor = db.rawQuery("SELECT " + id + " FROM " + table + " ORDER BY " + id + " DESC LIMIT 1", null);
    	if (cursor.moveToFirst()){
    		do {
    			lid = Integer.parseInt(cursor.getString(0));
    		}while (cursor.moveToNext());
    	}
    	cursor.close();
    	return lid;
    }
     
}
