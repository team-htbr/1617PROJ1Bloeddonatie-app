package com.team_htbr.a1617proj1bloeddonatie_app;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

public class SubscribeBloodtypeActivity extends AppCompatActivity {

	Button TypeA;
	Button TypeB;
	Button TypeAB;
	Button TypeO;

	TextView test;

	String bloodType;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_subscribe_bloodtype);
		setTitle("Kies bloed type");

		//get buttons
		TypeA = (Button)findViewById(R.id.buttonA);
		TypeB = (Button)findViewById(R.id.buttonB);
		TypeAB = (Button)findViewById(R.id.buttonAB);
		TypeO = (Button)findViewById(R.id.buttonO);

		test = (TextView) findViewById(R.id.testID);

		getInfo();

		switch (bloodType) {
			case "A": TypeA.setBackgroundColor(getResources().getColor(R.color.button_selected));
				break;
			case "B": TypeB.setBackgroundColor(getResources().getColor(R.color.button_selected));
				break;
			case "AB": TypeAB.setBackgroundColor(getResources().getColor(R.color.button_selected));
				break;
			case "O": TypeO.setBackgroundColor(getResources().getColor(R.color.button_selected));
				break;
			default: break;
		}


		TypeA.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clearButtons();
				v.setBackgroundColor(getResources().getColor(R.color.button_selected));
				saveInfo("A");
			}
		});

		TypeB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clearButtons();
				v.setBackgroundColor(getResources().getColor(R.color.button_selected));
				saveInfo("B");
			}
		});

		TypeAB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clearButtons();
				v.setBackgroundColor(getResources().getColor(R.color.button_selected));
				saveInfo("AB");
			}
		});

		TypeO.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clearButtons();
				v.setBackgroundColor(getResources().getColor(R.color.button_selected));
				saveInfo("O");
			}
		});

	}

	private void clearButtons() {
		TypeA.setBackgroundColor(getResources().getColor(R.color.button_default));
		TypeB.setBackgroundColor(getResources().getColor(R.color.button_default));
		TypeAB.setBackgroundColor(getResources().getColor(R.color.button_default));
		TypeO.setBackgroundColor(getResources().getColor(R.color.button_default));
	}

	private void unsubscribe() {
		FirebaseMessaging.getInstance().unsubscribeFromTopic("blood-A");
		FirebaseMessaging.getInstance().unsubscribeFromTopic("blood-B");
		FirebaseMessaging.getInstance().unsubscribeFromTopic("blood-AB");
		FirebaseMessaging.getInstance().unsubscribeFromTopic("blood-O");
	}

	private void saveInfo(String type) {
		SharedPreferences sharedPref = getSharedPreferences("userInfo", MODE_PRIVATE);

		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString("bloodtype", type);
		editor.apply();

		Toast.makeText(this, "saved", Toast.LENGTH_LONG).show();
	}

	private void getInfo() {
		SharedPreferences sharedPref = getSharedPreferences("userInfo", MODE_PRIVATE);

		bloodType = sharedPref.getString("bloodtype", "");

		test.setText(bloodType);
	}
}
