package com.example.alarm;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class AlarmAdd extends Activity {
	
	Button btnAdd2List;
	Button btnVList;
	String uid = "";
	final Context context = this;
	
	@Override
	public void onBackPressed() {
		startActivity(new Intent(AlarmAdd.this, AlarmViewList.class).putExtra("uid", uid));
		this.finish();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarmadd);
		
		uid = getIntent().getStringExtra("uid");
		final pafap_db db = new pafap_db(this);
		if(!db.Exists()){
			try{
				ArrayList<TypeModel> brandarraylist = new ArrayList<TypeModel>();
				brandarraylist.add(new TypeModel("Spdh", "G12"));
				brandarraylist.add(new TypeModel("Spdh", "G11"));
				db.addContents(brandarraylist);
			}
			catch(Exception ex){
				mbview("Error Model Insert db!", ex.getMessage());
			}
	
			try{
				ArrayList<ModelCommand> modelarraylist = new ArrayList<ModelCommand>();
				modelarraylist.add(new ModelCommand("G12", "Arm", "%1A1"));
				modelarraylist.add(new ModelCommand("G12", "Disarm", "%1A2"));
				modelarraylist.add(new ModelCommand("G12", "Status", "%1W1"));
				modelarraylist.add(new ModelCommand("G12", "Power", "%1W2"));
				modelarraylist.add(new ModelCommand("G12", "DeleteAlarmPhoneNumbers", "%1DD1"));
				modelarraylist.add(new ModelCommand("G12", "SetMobilePhoneNumbers",  "%1DD$"));
				modelarraylist.add(new ModelCommand("G12", "Panic",  "%1A3"));
				modelarraylist.add(new ModelCommand("G12", "Monitor", "%2"));
				modelarraylist.add(new ModelCommand("G12", "ChangeZoneAlarm", "%1DM06"));
				modelarraylist.add(new ModelCommand("G12", "ChangeHostPassword", "%1DD7$"));
				modelarraylist.add(new ModelCommand("G11", "Arm", "%1A1"));
				modelarraylist.add(new ModelCommand("G11", "Disarm", "%1A2"));
				modelarraylist.add(new ModelCommand("G11", "Status", "%1W1"));
				modelarraylist.add(new ModelCommand("G11", "Power", "%1W2"));
				modelarraylist.add(new ModelCommand("G11", "DelAlarmPhoneNumbers", "%1DD1"));
				modelarraylist.add(new ModelCommand("G11", "SetMobilePhoneNumbers", "%1DD$"));
				modelarraylist.add(new ModelCommand("G11", "Panic", "%1A3"));
				modelarraylist.add(new ModelCommand("G11", "Monitor", "%2"));
				modelarraylist.add(new ModelCommand("G11", "ChangeZoneAlarm", "%1DM06"));
				modelarraylist.add(new ModelCommand("G11", "ChangeHostPassword", "%1DD7$"));
				db.addContentsFromArraylist(modelarraylist);
			}
			catch(Exception e){
				mbview("Error command Insert db!", e.getMessage());
			}
		}
		
		Cursor cursor = db.getTableContents("SELECT DISTINCT brand FROM brands");
		bindSpinnerFromCursor(cursor, R.id.spbrand);
		Spinner sp = (Spinner)findViewById(R.id.spbrand);
		String sel = sp.getSelectedItem().toString();
		bindSpinnerByArray(R.id.spmodel, sel);
		cursor.close();

		sp.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Spinner sp = (Spinner)findViewById(R.id.spbrand);
				String sel = sp.getSelectedItem().toString();
				Cursor cursor = db.getTableContent("brands", new String[]{"model"}, "brand", new String[]{sel});
				bindSpinnerFromCursor(cursor, R.id.spmodel);
				cursor.close();
				db.close();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
			
		});
		
		//add button
		btnAdd2List = (Button) findViewById(R.id.btnAddList);
		btnAdd2List.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				EditText txtname = (EditText)findViewById(R.id.txtAlarmName);
				Spinner spb = (Spinner)findViewById(R.id.spbrand);
				Spinner spm = (Spinner)findViewById(R.id.spmodel);
				EditText txtpn = (EditText)findViewById(R.id.txtPhoneNumber);
				EditText txtps = (EditText)findViewById(R.id.txtPassword);
				try{
					Locale defloc = Locale.getDefault();
					if(!txtname.getText().toString().equals("") && !txtpn.getText().toString().equals("") && !txtps.getText().toString().equals("")){
						db.addAlarmContent(new alarm(Integer.parseInt(uid), txtname.getText().toString().toUpperCase(defloc), spb.getSelectedItem().toString(), spm.getSelectedItem().toString(), txtpn.getText().toString(), txtps.getText().toString(), "#909090"));
						int lasttid = db.getLastId("id", "alarms");
						ArrayList<ModelCommand> mcalarm = new ArrayList<ModelCommand>();
						mcalarm = db.getCommandsByModel(spm.getSelectedItem().toString());
						db.addAlarmCommandsFromArraylist(mcalarm, lasttid);
						mbview("Add", "Content Added!");
						startActivity(new Intent(AlarmAdd.this, AlarmViewList.class).putExtra("uid", uid));
						AlarmAdd.this.finish();
					}
					else{
						mbview("Add", "Fill Form!");
					}
				}
				catch (SQLiteException ex){
					mbview("Error db!", ex.getMessage());
				}
			}
		});
	}
	
	
	public void bindSpinner(int strarr, int spnr)
	{
		Spinner spinner = (Spinner) findViewById(spnr);
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
	        this, strarr, android.R.layout.simple_spinner_item);
	     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	     spinner.setAdapter(adapter);
	}
	
	public void bindSpinnerFromCursor(Cursor cs, int spnr){
		List<String> tmp = new ArrayList<String>();
		if(cs.moveToFirst()){
			do{
				tmp.add(cs.getString(0).toString());
			}while(cs.moveToNext());
		}
		Spinner sp = (Spinner) findViewById(spnr);
		ArrayAdapter<String> adp = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, tmp);
		adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(adp);
	}
	

	public void bindSpinnerByArray(int spnr, String key)
	{
		ArrayList<alarm> arraylist = new ArrayList<alarm>();
		arraylist = fillArray();
		ArrayList<String> tmp = new ArrayList<String>();
		for (int i = 0;i<arraylist.size();i++){
			if(arraylist.get(i).getBrand().toString().equals(key)){
				tmp.add(arraylist.get(i).getModel().toString());
			}
		}
		Spinner sp = (Spinner) findViewById(spnr);
		ArrayAdapter<String> adp = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, tmp);
		adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(adp);
	}
	
	public ArrayList<alarm> fillArray()
	{
		ArrayList<alarm> arraylist = new ArrayList<alarm>();
		arraylist.add(new alarm("Spdh", "G12"));
		arraylist.add(new alarm("Spdh", "G11"));
		return arraylist;
	}
	
	public void mbview(String title, String message){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			alertDialogBuilder.setTitle(title);
			alertDialogBuilder
				.setMessage(message)
				.setCancelable(false)
				.setPositiveButton("OK",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					}
				  });
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.alarm_add_brand_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.alarm_add_new_brand:
			startActivity(new Intent(AlarmAdd.this, AlarmNewBrand.class).putExtra("uid", uid));
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
}
