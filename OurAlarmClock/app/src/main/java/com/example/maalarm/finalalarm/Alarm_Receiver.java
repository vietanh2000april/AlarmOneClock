package com.example.maalarm.finalalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class Alarm_Receiver  extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("We are in the receiver.", "Hoozah!");
        String get_your_string = intent.getExtras().getString("extra");
        Log.e("What is the key? ", get_your_string);

        Integer getMyRingTone = intent.getExtras().getInt("ring_tone_choice");

        Log.e("The choice is ", getMyRingTone.toString());

        // create an intent to the ringtone service
        Intent service_intent = new Intent(context, RingtonePlayingService.class);

        // pass the extra string from Receiver to the Ringtone Playing Service
        service_intent.putExtra("extra", get_your_string);
        // pass the extra integer from the Receiver to the Ringtone Playing Service
        service_intent.putExtra("ring_tone_choice", getMyRingTone);

        // start the ringtone service
        context.startService(service_intent);
    }

}