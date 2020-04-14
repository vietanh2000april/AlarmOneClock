package com.example.maalarm.finalalarm;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.VideoView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

//    // dialog
//    Dialog mVideoDialog ;
//    VideoView mVideoFullScreen;
//    MediaController controller;

//    // youtube player view
//    YouTubePlayerView youTubePlayerView;
//    Button button;
//    YouTubePlayer.OnInitializedListener onInitializedListener;

    // to make our alarm manager
    AlarmManager alarmManager;
    TimePicker alarmTimePicker;
    TextView updateText;
    Context context;
    PendingIntent pendingIntent;
    int chooseRingTone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // set the view to the activiti_main.xml > content_main.xml
        setContentView(R.layout.activity_main);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.context = this;

        // alarmManager
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmTimePicker = (TimePicker) findViewById(R.id.timePicker);
        updateText = (TextView) findViewById(R.id.update_text);
        final Calendar calendar = Calendar.getInstance();
        final Intent myIntent = new Intent(this.context, Alarm_Receiver.class);

        // spinner/ drop-down list
        Spinner spinner = (Spinner) findViewById(R.id.richard_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.alarm_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        // alarm on button
        Button alarm_on = (Button) findViewById(R.id.alarm_on);
        alarm_on.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getHour());
                calendar.set(Calendar.MINUTE, alarmTimePicker.getMinute());

                int hour = alarmTimePicker.getHour();
                int minute = alarmTimePicker.getMinute();

                String hour_string = String.valueOf(hour);
                String minute_string = String.valueOf(minute);

                if (hour > 12) {
                    hour_string = String.valueOf(hour - 12);
                }

                if (minute < 10) {
                    //10:7 --> 10:07
                    minute_string = "0" + String.valueOf(minute);
                }

                set_alarm_text("Alarm set to: " + hour_string + ":" + minute_string);
                myIntent.putExtra("extra", "alarm on");
                myIntent.putExtra("ring_tone_choice", chooseRingTone);

                Log.e("The ringtone id is" , String.valueOf(chooseRingTone));
                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0,
                        myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        pendingIntent);
            }
        });

        // alarm off button
        Button alarm_off = (Button) findViewById(R.id.alarm_off);
        alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // method that changes the update text Textbox
                set_alarm_text("Alarm off!");

                // cancel the alarm
                alarmManager.cancel(pendingIntent);

                // put extra string into myIntent
                // tells the clock that you pressed the "alarm off" button
                myIntent.putExtra("extra", "alarm off");
                myIntent.putExtra("ring_tone_choice", chooseRingTone);

                // stop the ringtone
                sendBroadcast(myIntent);
            }
        });
    }

    // status bar
    private void set_alarm_text(String output) {
        updateText.setText(output);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        chooseRingTone = (int) id;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback

    }



}