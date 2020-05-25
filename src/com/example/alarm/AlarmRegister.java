package com.example.alarm;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AlarmRegister extends Activity {
	Button btnreg;
	final Context context = this;
	
	@Override
	public void onBackPressed() {
		this.finish();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_register);
		
		final pafap_db db = new pafap_db(this);
		btnreg = (Button) findViewById(R.id.btnRegister);
		btnreg.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try{
					EditText user = (EditText) findViewById(R.id.txtusr);
					EditText usrpass = (EditText) findViewById(R.id.txtpass);
					EditText usrpassconf = (EditText) findViewById(R.id.txtconfirmpass);
					pafap_strings str = new pafap_strings();
					if(!user.getText().toString().equals("") && !usrpass.getText().toString().equals("") && !usrpassconf.getText().toString().equals("")){
						if(usrpass.getText().toString().equals(usrpassconf.getText().toString())){
							ArrayList<usrAlarm> usreg = new ArrayList<usrAlarm>();
							usreg.add(new usrAlarm(user.getText().toString(), str.md5(usrpass.getText().toString())));
							db.addUsrContents(usreg);
							mbview("Register!", "User Registered!");
							startActivity(new Intent(AlarmRegister.this, AlarmLogin.class));
							AlarmRegister.this.finish();
						}
						else mbview("Error Confirm", "Confirm password not the same!");
					}
					else mbview("Form", "Fill form!");
				}catch (Exception ex){
					mbview("register", ex.getMessage());
				}
			}
		});
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
