package com.example.maalarm.finalalarm;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.security.Provider;
import java.util.Random;


public class RingtonePlayingService extends Service {

    MediaPlayer media_song;
    MediaPlayer mediaPanel = new MediaPlayer();

    int startId;
    boolean isRunning;

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        // fetch the extra string from the alarm on/alarm off values
        String state = intent.getExtras().getString("extra");

        // fetch the ringtone choice integer values
        Integer ringToneChoice = intent.getExtras().getInt("ring_tone_choice");

        Log.e("Ringtone extra is ", state);
        Log.e("Ring tone choice is ", ringToneChoice.toString());

        // set up the notification service
        NotificationManager notify_manager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        // set up an intent that goes to the Main Activity
        Intent intent_main_activity = new Intent(this.getApplicationContext(), MainActivity.class);
        // set up a pending intent
        PendingIntent pending_intent_main_activity = PendingIntent.getActivity(this, 0,
                intent_main_activity, 0);

        // make the notification parameters
        Notification notification_popup = new Notification.Builder(this)
                .setContentTitle("An alarm is going off!")
                .setContentText("Click me!")
                .setSmallIcon(R.drawable.ic_action_call)
                .setContentIntent(pending_intent_main_activity)
                .setAutoCancel(true)
                .build();

        assert state != null;
        switch (state) {
            case "alarm on":
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                Log.e("Start ID is ", state);
                break;
            default:
                startId = 0;
                break;
        }

        // if there is no music playing, and the user pressed "alarm on"
        // music should start playing
        if (!this.isRunning && startId == 1) {
            Log.e("there is no music, ", "and you want start");

            this.isRunning = true;
            this.startId = 0;

            // set up the start command for the notification
            notify_manager.notify(0, notification_popup);

            // if no ringtone is chosen
            if (ringToneChoice == 0) {
                // play a randomly picked audio file

                int minimum_number = 1;
                int maximum_number = 13;

                Random random_number = new Random();
                int ringToneNum = random_number.nextInt(maximum_number + minimum_number);
                Log.e("random number is " , String.valueOf(ringToneNum));


                if (ringToneNum == 1) {
                    media_song = MediaPlayer.create(this, R.raw.beep);
                    media_song.start();
                }
                else if (ringToneNum == 2) {
                    // create an instance of the media player
                    media_song = MediaPlayer.create(this, R.raw.deep);
                    // start the ringtone
                    media_song.start();
                }
                else if (ringToneNum == 3) {
                    media_song = MediaPlayer.create(this, R.raw.ptsd);
                    media_song.start();
                }
                else if (ringToneNum == 4) {
                    media_song = MediaPlayer.create(this, R.raw.motherrussia);
                    media_song.start();
                }
                /*else if (ringToneNum == 5) {
                    media_song = MediaPlayer.create(this, R.raw.cykablyat);
                    media_song.start();
                }*/
            }

            // if a specific ringtone is chosen
            else if (ringToneChoice == 1) {
                // create an instance of the media player
                media_song = MediaPlayer.create(this, R.raw.deep);
                // start the ringtone
                media_song.start();
            }
            else if (ringToneChoice == 2) {
                // create an instance of the media player
                media_song = MediaPlayer.create(this, R.raw.beep);
                // start the ringtone
                media_song.start();
            }
            else if (ringToneChoice == 3) {
                media_song = MediaPlayer.create(this, R.raw.ptsd);
                media_song.start();
            }
            else if (ringToneChoice == 4) {
                media_song = MediaPlayer.create(this, R.raw.motherrussia);
                media_song.start();
            }

            else if (ringToneChoice == 5) {
                // error here - intent to pop class
                startActivity(new Intent(RingtonePlayingService.this, Pop.class));

                // return to MainActivity
//                // 1. create an intent to main activity
//                Intent intentVideo = new Intent(RingtonePlayingService.this, MainActivity.class);
//                // putExtra(name, value)
//                intentVideo.putExtra("openMyVid","openMyVid");

////                // 2. finish() returns to mainactivity
//                Intent toMain = new Intent(getApplicationContext(),
//                        MainActivity.class);
//                toMain.putExtra("openVideo","openVideo");
//                startActivity(toMain);

                // CAN STOP NGAY KHI BAM NUT OPEN VID
            }
        }

        // if there is music playing, and the user pressed "alarm off"
        // music should stop playing
        else if (this.isRunning && startId == 0) {
            Log.e("there is music, ", "and you want end");

            // stop the ringtone
            media_song.stop();
            media_song.reset();

            this.isRunning = false;
            this.startId = 0;
        }

        else if (!this.isRunning && startId == 0) {
            Log.e("there is no music, ", "and you want end");

            this.isRunning = false;
            this.startId = 0;
        }

        // if there is music playing and the user pressed "alarm on"
        // do nothing
        else if (this.isRunning && startId == 1) {
            Log.e("there is music, ", "and you want start");

            this.isRunning = true;
            this.startId = 1;
        }

        else {
            Log.e("else ", "somehow you reached this");
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        // Tell the user we stopped.
        Log.e("on Destroy called", "ta da");

        super.onDestroy();
        this.isRunning = false;
    }
}