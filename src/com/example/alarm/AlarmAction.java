package com.example.alarm;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AlarmAction extends Activity {
	
	final Context context = this;
	String uid = "";
	String aid = "";
	Button btnarm;
	Button btndisarm;
	Button btnstatus;
	Button btnpower;
	Button btndelnr;
	Button btnsetnr;
	Button btnpanic;
	Button btnmonitor;
	Button btnalrmzone;
	Button btnhostpass;
	//final pafap_db dbt = new pafap_db(this);
	final ArrayList<alarmCommand> commandList = new ArrayList<alarmCommand>();
	String phonenumber = "";
	String password = "";
	String command = "";
	
	@Override
	public void onBackPressed() {
		//startActivity(new Intent(AlarmAction.this, AlarmViewList.class).putExtra("uid", uid));
		this.finish();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarmaction);
		
		
		aid = getIntent().getStringExtra("aid");
		uid = getIntent().getStringExtra("uid");
		phonenumber = getIntent().getStringExtra("num");
		password = getIntent().getStringExtra("pass");
		
		//fill commandList with data
		try{
			bindListCommand();
		}catch(Exception ex){
			mbview("bindListView Error!", ex.getMessage());
		}
		
		//arm alarm
		btnarm = (Button) findViewById(R.id.btnArm);
		btnarm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				for (int i=0;i<commandList.size();i++){
					if(commandList.get(i).getName().equals("Arm")){
						command = commandList.get(i).getCommand();
						pafap_strings cmdstr = new pafap_strings();
						command = cmdstr.replaceString(command, "%1", password);
						sendSMS(phonenumber, command);
						btnarm.setBackgroundColor(Color.parseColor(commandList.get(i).getColor()));
						Toast.makeText(getApplicationContext(), "Command Sent!", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		
		//disarm alarm
		btndisarm = (Button) findViewById(R.id.btnDisarm);
		btndisarm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				for (int i=0;i<commandList.size();i++){
					if(commandList.get(i).getName().equals("Disarm")){
						command = commandList.get(i).getCommand();
						pafap_strings cmdstr = new pafap_strings();
						command = cmdstr.replaceString(command, "%1", password);
						sendSMS(phonenumber, command);
						btndisarm.setBackgroundColor(Color.parseColor(commandList.get(i).getColor()));
						Toast.makeText(getApplicationContext(), "Command Sent!", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		
		//status alarm
		btnstatus = (Button) findViewById(R.id.btnStatus);
		btnstatus.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				for (int i=0;i<commandList.size();i++){
					if(commandList.get(i).getName().equals("Status")){
						command = commandList.get(i).getCommand();
						pafap_strings cmdstr = new pafap_strings();
						command = cmdstr.replaceString(command, "%1", password);
						sendSMS(phonenumber, command);
						Toast.makeText(getApplicationContext(), "Command Sent!", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		
		//power alarm
		btnpower = (Button) findViewById(R.id.btnCheckPower);
		btnpower.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				for (int i=0;i<commandList.size();i++){
					if(commandList.get(i).getName().equals("Power")){
						command = commandList.get(i).getCommand();
						pafap_strings cmdstr = new pafap_strings();
						command = cmdstr.replaceString(command, "%1", password);
						sendSMS(phonenumber, command);
						btnpower.setBackgroundColor(Color.parseColor(commandList.get(i).getColor()));
						Toast.makeText(getApplicationContext(), "Command Sent!", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		
		//panic alarm
		btnpanic = (Button) findViewById(R.id.btnPanic);
		btnpanic.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				for (int i=0;i<commandList.size();i++){
					if (commandList.get(i).getName().equals("Panic")){
						command = commandList.get(i).getCommand();
						pafap_strings cmdstr = new pafap_strings();
						command = cmdstr.replaceString(command, "%1", password);
						sendSMS(phonenumber, command);
						btnpanic.setBackgroundColor(Color.parseColor(commandList.get(i).getColor()));
						Toast.makeText(getApplicationContext(), "Command Sent!", Toast.LENGTH_SHORT).show();
					}
				}
					
			}
		});
		
		
		//delete alarm phone number
		btndelnr = (Button) findViewById(R.id.btnDeleteAlarmPhoneNumber);
		btndelnr.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				for (int i=0;i<commandList.size();i++){
					if (commandList.get(i).getName().equals("DeleteAlarmPhoneNumbers")){
						command = commandList.get(i).getCommand();
						pafap_strings cmdstr = new pafap_strings();
						command = cmdstr.replaceString(command, "%1", password);
						sendSMS(phonenumber, command);
						Toast.makeText(getApplicationContext(), "Command Sent!", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		
		//monitor alarm
		btnmonitor = (Button) findViewById(R.id.btnMonitor);
		btnmonitor.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				for (int i=0;i<commandList.size();i++){
					if (commandList.get(i).getName().equals("Monitor")){
						command = commandList.get(i).getCommand();
						pafap_strings cmdstr = new pafap_strings();
						command = cmdstr.replaceString(command, "%2", phonenumber);
						callAlarm(command);
						Toast.makeText(getApplicationContext(), "Command Sent!", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		
		//set phone number alarm
		btnsetnr = (Button) findViewById(R.id.btnSetAlarmPhoneNumber);
		btnsetnr.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				for (int i=0;i<commandList.size();i++){
					if(commandList.get(i).getName().equals("SetMobilePhoneNumbers")){
						command = commandList.get(i).getCommand();
						final pafap_strings cmdstr = new pafap_strings();
						AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
				        alertDialog.setTitle(commandList.get(i).getModel() + " " + commandList.get(i).getName());
				        final EditText input = new EditText(context);
				        input.setText("");
				        alertDialog.setView(input);
				        alertDialog.setIcon(R.drawable.ic_launcher);
				        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
				        	public void onClick(DialogInterface dialog,int which) {
				        		command = cmdstr.replaceString(command, "$", input.getText().toString());
				        		command = cmdstr.replaceString(command, "%1", password);
				        		sendSMS(phonenumber, command);
				            }
				        });
				        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
				        	public void onClick(DialogInterface dialog, int which) {
				                dialog.cancel();
				            }
				        });
				        alertDialog.show();
				        Toast.makeText(getApplicationContext(), "Command Sent!", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		
		//change alarm zone
		btnalrmzone = (Button) findViewById(R.id.btnChangeZoneAlarm);
		btnalrmzone.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
		//change alarm host password
		btnhostpass = (Button) findViewById(R.id.btnChangePass);
		btnhostpass.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
	}
	
	public void callAlarm(String number)
	{
		try
		{
			Intent intent = new Intent(Intent.ACTION_CALL);
			intent.setData(Uri.parse("tel:" + number));
			startActivity(intent);
		}
		catch (ActivityNotFoundException activityException)
		{
			mbview("Call Failed!", activityException.toString());
		}
	}

	public void bindListCommand(){
		pafap_db dbt = new pafap_db(this);
		try{
	    	Cursor cs = dbt.getTableContents("SELECT id, model, name, command, stcolor FROM alcommands WHERE aid = " + Integer.parseInt(aid) + " AND status = 'ACTIVATED'");
		    if (cs.moveToFirst()){
				do{
					alarmCommand ac = new alarmCommand();
					ac.setId(Integer.parseInt(cs.getString(0)));
					ac.setModel(cs.getString(1));
					ac.setName(cs.getString(2));
					ac.setCommand(cs.getString(3));
					ac.setColor(cs.getString(4));
					commandList.add(ac);
				}while(cs.moveToNext());
			}
		    else mbview("error!", "table empty");
			cs.close();
			dbt.close();
	    }catch (NumberFormatException ex){
	    	mbview("error!", ex.getMessage());
	    }
	}
	
	public void sendSMS(String phoneNumber, String message){
	       SmsManager sms = SmsManager.getDefault();
	       sms.sendTextMessage(phoneNumber, null, message, null, null);
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
