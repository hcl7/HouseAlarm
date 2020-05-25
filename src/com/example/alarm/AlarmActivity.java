package com.example.alarm;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
//import android.widget.Toast;

public class AlarmActivity extends Activity {
	final Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_main);
		
		
        new Handler().postDelayed(new Runnable() {
            // Using handler with postDelayed called runnable run method
            @Override
            public void run() {
                Intent i = new Intent(AlarmActivity.this, AlarmLogin.class);
                startActivity(i);
                //close this activity
                finish();
            }
        }, 3*1000); // wait for 3 seconds
        
	}
	
	@Override
    protected void onDestroy() {
         
        super.onDestroy();
         
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alarm, menu);
		return true;
	}

}
