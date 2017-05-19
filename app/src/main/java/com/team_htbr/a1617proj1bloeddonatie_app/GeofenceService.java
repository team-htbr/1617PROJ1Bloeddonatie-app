package com.team_htbr.a1617proj1bloeddonatie_app;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import java.util.List;

/**
 * @author bjorn
 */

public class GeofenceService extends IntentService {

	public static final String TAG = "GeofenceService";


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

			if (transition == Geofence.GEOFENCE_TRANSITION_ENTER) {
				Log.d(TAG, "entering geofence - " + geofence.getRequestId());
				sendNotification("je bent in de buurt van donatiecentrum " + requestId + " klik hier voor address ");
			}
		}
	}

	private void sendNotification(String bla) {
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
		Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(this)
			.setSmallIcon(R.mipmap.ic_launcher)
			.setContentTitle("Bloeddonatie")
			.setContentText(bla)
			.setAutoCancel(true)
			.setSound(notificationSound)
			.setContentIntent(pendingIntent);

		NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(0, notifiBuilder.build());
	}
}
