package com.team_htbr.a1617proj1bloeddonatie_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.messaging.FirebaseMessaging;

public class SubscribeBloodtypeActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_subscribe_bloodtype);
		setTitle("Kies bloed type");
	}

	private void Unsubscribe() {
		FirebaseMessaging.getInstance().unsubscribeFromTopic("blood-A");
		FirebaseMessaging.getInstance().unsubscribeFromTopic("blood-B");
		FirebaseMessaging.getInstance().unsubscribeFromTopic("blood-AB");
		FirebaseMessaging.getInstance().unsubscribeFromTopic("blood-O");
	}
}
