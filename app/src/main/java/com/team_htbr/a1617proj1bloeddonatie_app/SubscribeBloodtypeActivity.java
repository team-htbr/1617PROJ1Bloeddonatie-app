package com.team_htbr.a1617proj1bloeddonatie_app;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

public class SubscribeBloodtypeActivity extends AppCompatActivity {

	private Button TypeAp;
	private Button TypeBp;
	private Button TypeABp;
	private Button TypeOp;
	private Button TypeAn;
	private Button TypeBn;
	private Button TypeABn;
	private Button TypeOn;

	private String bloodType;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_subscribe_bloodtype);
		setTitle("Kies bloed type");

		//get buttons
		TypeAp = (Button)findViewById(R.id.buttonAp);
		TypeBp = (Button)findViewById(R.id.buttonBp);
		TypeABp = (Button)findViewById(R.id.buttonABp);
		TypeOp = (Button)findViewById(R.id.buttonOp);
		TypeAn = (Button)findViewById(R.id.buttonAn);
		TypeBn = (Button)findViewById(R.id.buttonBn);
		TypeABn = (Button)findViewById(R.id.buttonABn);
		TypeOn = (Button)findViewById(R.id.buttonOn);

		getInfo();

		switch (bloodType) {
			case "Ap": TypeAp.setBackgroundColor(getResources().getColor(R.color.button_selected));
				break;
			case "Bp": TypeBp.setBackgroundColor(getResources().getColor(R.color.button_selected));
				break;
			case "ABp": TypeABp.setBackgroundColor(getResources().getColor(R.color.button_selected));
				break;
			case "Op": TypeOp.setBackgroundColor(getResources().getColor(R.color.button_selected));
				break;
			case "An": TypeAn.setBackgroundColor(getResources().getColor(R.color.button_selected));
				break;
			case "Bn": TypeBn.setBackgroundColor(getResources().getColor(R.color.button_selected));
				break;
			case "ABn": TypeABn.setBackgroundColor(getResources().getColor(R.color.button_selected));
				break;
			case "On": TypeOn.setBackgroundColor(getResources().getColor(R.color.button_selected));
				break;
			default: break;
		}

		TypeAp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handlings("Ap", v);
			}
		});

		TypeBp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handlings("Bp", v);
			}
		});

		TypeABp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handlings("ABp", v);
			}
		});

		TypeOp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handlings("Op", v);
			}
		});

		TypeAn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handlings("An", v);
			}
		});

		TypeBn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handlings("Bn", v);
			}
		});

		TypeABn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handlings("ABn", v);
			}
		});

		TypeOn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handlings("On", v);
			}
		});

	}

	private void handlings(String type, View view) {
		if (! bloodType.equals(type)) {
			clearButtons();
			view.setBackgroundColor(getResources().getColor(R.color.button_selected));

			saveInfo(type);
			getInfo();

			unsubscribe();
			FirebaseMessaging.getInstance().subscribeToTopic("blood-" + type);
		} else if(bloodType.equals(type)) {
			clearButtons();
			unsubscribe();
			saveInfo("");
			bloodType = "";
		}
	}

	private void clearButtons() {
		TypeAp.setBackgroundColor(getResources().getColor(R.color.button_default));
		TypeBp.setBackgroundColor(getResources().getColor(R.color.button_default));
		TypeABp.setBackgroundColor(getResources().getColor(R.color.button_default));
		TypeOp.setBackgroundColor(getResources().getColor(R.color.button_default));
		TypeAn.setBackgroundColor(getResources().getColor(R.color.button_default));
		TypeBn.setBackgroundColor(getResources().getColor(R.color.button_default));
		TypeABn.setBackgroundColor(getResources().getColor(R.color.button_default));
		TypeOn.setBackgroundColor(getResources().getColor(R.color.button_default));
	}

	private void unsubscribe() {
		FirebaseMessaging.getInstance().unsubscribeFromTopic("blood-Ap");
		FirebaseMessaging.getInstance().unsubscribeFromTopic("blood-Bp");
		FirebaseMessaging.getInstance().unsubscribeFromTopic("blood-ABp");
		FirebaseMessaging.getInstance().unsubscribeFromTopic("blood-Op");
		FirebaseMessaging.getInstance().unsubscribeFromTopic("blood-An");
		FirebaseMessaging.getInstance().unsubscribeFromTopic("blood-Bn");
		FirebaseMessaging.getInstance().unsubscribeFromTopic("blood-ABn");
		FirebaseMessaging.getInstance().unsubscribeFromTopic("blood-On");
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
	}
}
