package com.team_htbr.a1617proj1bloeddonatie_app;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author bjorn
 */

public class GeofenceService extends IntentService {

	public static final String TAG = "GeofenceService";
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");


	public GeofenceService() {
		super(TAG);

	}

	@Override
	protected void onHandleIntent(Intent intent) {


		GeofencingEvent event = GeofencingEvent.fromIntent(intent);
		if (event.hasError()) {
			Log.d(TAG, "error");
		} else {
			int transition = event.getGeofenceTransition();
			List<Geofence> geofences = event.getTriggeringGeofences();
			Geofence geofence = geofences.get(0);
			String requestId = geofence.getRequestId();

//-----------------------------------------------------------------------------------------

			Date today = new Date();
			String currentDate = sdf.format(today);

			SharedPreferences sharedPreferences = getSharedPreferences("dates", MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			String savedDate = sharedPreferences.getString(requestId, null);

			if (savedDate.equals(null)) {
				editor.putString(requestId, currentDate);
				editor.apply();
				testTranistion(transition,requestId);
			} else {
				try {
					Date previousDate = sdf.parse(savedDate);
					Calendar c1 = Calendar.getInstance();
					Calendar c2 = Calendar.getInstance();
					c1.setTime(today);
					c2.setTime(previousDate);

					if ((c1.getTimeInMillis() - c2.getTimeInMillis()) / (24 * 60 * 60 * 1000) >= 30) {
						editor.putString(requestId, currentDate);
						editor.apply();
						testTranistion(transition,requestId);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}


		}
	}

	private void testTranistion(int transition, String requestId) {
		if (transition == Geofence.GEOFENCE_TRANSITION_ENTER) {
			sendNotification("donatiecentrum " + requestId + " is dichtbij");
		}
	}

	private void sendNotification(String text) {
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
		Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(this)
			.setSmallIcon(R.mipmap.ic_launcher)
			.setContentTitle("Bloeddonatie")
			.setContentText(text)
			.setAutoCancel(true)
			.setSound(notificationSound)
			.setContentIntent(pendingIntent);

		NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(0, notifiBuilder.build());
	}
}
