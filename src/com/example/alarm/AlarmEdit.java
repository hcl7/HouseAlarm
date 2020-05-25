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
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class AlarmEdit extends Activity {
	final Context context = this;
	String aid = "";
	String uid = "";
	final ArrayList<alarm> alarmSelected = new ArrayList<alarm>();
	
	
	@Override
	public void onBackPressed() {
		startActivity(new Intent(AlarmEdit.this, AlarmViewList.class).putExtra("uid", uid));
		this.finish();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarmedit);
		
		aid = getIntent().getStringExtra("aid");
		uid = getIntent().getStringExtra("uid");
		final pafap_db db = new pafap_db(this);
		Cursor cursor = db.getTableContents("SELECT DISTINCT brand FROM brands");
		bindSpinnerFromCursor(cursor, R.id.spEditBrand);
		Spinner sp = (Spinner)findViewById(R.id.spEditBrand);
		String sel = sp.getSelectedItem().toString();
		bindSpinnerByArray(R.id.spEditModel, sel);
		cursor.close();
		
		sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Spinner sp = (Spinner)findViewById(R.id.spEditBrand);
				String sel = sp.getSelectedItem().toString();
				Cursor cursor = db.getTableContent("brands", new String[]{"model"}, "brand", new String[]{sel});
				bindSpinnerFromCursor(cursor, R.id.spEditModel);
				cursor.close();
				db.close();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		try{
			alarm alrm = db.getAlarmContent(Integer.parseInt(aid));
			alarmSelected.add(alrm);
			db.close();
			
			EditText editalmname = (EditText) findViewById(R.id.txtEditAlarmName);
			EditText editphone = (EditText) findViewById(R.id.txtEditPhoneNumber);
			EditText editpass = (EditText) findViewById(R.id.txtEditPassword);
			
			for (int i = 0; i < alarmSelected.size(); ++i) {
				editalmname.setText(alarmSelected.get(i).getName().toString());
			      editphone.setText(alarmSelected.get(i).getPhoneNumber().toString());
			      editpass.setText(alarmSelected.get(i).getPassword().toString());
			      uid = String.valueOf(alarmSelected.get(i).getUID());
			}
		}catch(Exception ex) {
			mbview("getAlarmContent db error! ", ex.getMessage());
		}
		
		//update alarm
		Button btnupdatealarm = (Button) findViewById(R.id.btnAlarmUpdate);
		btnupdatealarm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try{
					EditText editplate = (EditText) findViewById(R.id.txtEditAlarmName);
					EditText editphone = (EditText) findViewById(R.id.txtEditPhoneNumber);
					EditText editpass = (EditText) findViewById(R.id.txtEditPassword);
					Spinner sptrbrand = (Spinner) findViewById(R.id.spEditBrand);
					Spinner sptrmodel = (Spinner) findViewById(R.id.spEditModel);
					String brandSelected = sptrbrand.getSelectedItem().toString();
					String modelSelected = sptrmodel.getSelectedItem().toString();
					Locale defloc = Locale.getDefault();
					if (!editplate.getText().toString().equals("") && !editphone.getText().toString().equals("") && !editpass.getText().toString().equals("")){
						db.updateAlarmContent(new alarm(Integer.parseInt(aid), editplate.getText().toString().toUpperCase(defloc), brandSelected.toString(), modelSelected.toString(), editphone.getText().toString(), editpass.getText().toString(), "CHECK"));
						db.deleteTableContent("alcommands", "aid", Integer.parseInt(aid));
						ArrayList<ModelCommand> mcalarm = new ArrayList<ModelCommand>();
						mcalarm = db.getCommandsByModel(sptrmodel.getSelectedItem().toString());
						db.addAlarmCommandsFromArraylist(mcalarm, Integer.parseInt(aid));
						mbview("Update Alarm! ", "Alarm Updated!");
						db.close();
						startActivity(new Intent(AlarmEdit.this, AlarmViewList.class).putExtra("uid", uid));
						AlarmEdit.this.finish();
					}
				}
				catch(Exception ex){
					mbview("Update Error!", "Update Button Error!" + ex.getMessage());
				}
			}
		});
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

}
