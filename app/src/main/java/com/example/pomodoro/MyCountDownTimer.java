package com.example.pomodoro;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.pomodoro.utils.Utils;

import java.util.Calendar;

public class MyCountDownTimer extends CountDownTimer {
    private final Context context;
    private final TextView tv;
    private final Button start;
    private int pomodoro_time;
    private final int pomodoro_amount;
    private int short_break_time;
    private int long_break_time;
    private final int time;//1-Pomodoro, 2-Pausa curta, 3-Pausa longa
    private final MediaPlayer mp;
    private MediaPlayer clock;
    private final Spinner sActivity;

    public MyCountDownTimer(Context context,
                            TextView tv,
                            long timeInFuture,
                            long interval,
                            Button start,
                            int time,
                            MediaPlayer mp,
                            int pomodoro_amount,
                            Spinner sActivity) {
        super(timeInFuture, interval);
        this.context = context;
        this.tv = tv;
        this.start = start;
        this.time = time;
        this.mp = mp;
        this.pomodoro_amount = pomodoro_amount;
        this.sActivity = sActivity;
        getSettings();
    }

    public void getSettings() {
        Utils utils = new Utils(context);
        utils.findSettings();
        pomodoro_time = utils.getPomodoroTime();
        short_break_time = utils.getShortBreakTime();
        long_break_time = utils.getLongBreakTime();
        String clock_name = utils.getClockName();
        utils.close();

        int clock_sound = 0;
        switch (clock_name) {
            case "Clock 1":
                clock_sound = R.raw.clock_1;
                break;
            case "Clock 2":
                clock_sound = R.raw.clock_2;
                break;
            case "Clock 3":
                clock_sound = R.raw.clock_3;
                break;
            case "Clock 4":
                clock_sound = R.raw.clock_4;
                break;
            case "Clock 5":
                clock_sound = R.raw.clock_5;
                break;
            case "Clock 6":
                clock_sound = R.raw.clock_6;
                break;
        }

        if (clock_sound != 0) {
            clock = MediaPlayer.create(context, clock_sound);
            clock.setOnCompletionListener(MediaPlayer::release);
            clock.setOnCompletionListener(mp -> {
                // TODO Auto-generated method stub
                clock.start();
            });
            clock.start();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onTick(long millisUntilFinished) {
        Log.i("Script", "Timer: " + millisUntilFinished);
        String time = getCorretcTimer(true, millisUntilFinished) + ":" + getCorretcTimer(false, millisUntilFinished);
        tv.setText(time);
    }

    private void releasePlayer() {
        if (clock != null) {
            clock.stop();
            clock.release();
            clock = null;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onFinish() {
        createBarNotification(); // notificação!
        releasePlayer();
        mp.start();
        if (this.pomodoro_amount == 4) {
            tv.setText(long_break_time + " : 00");
            Toast.makeText(context, "FAÇA A SUA PAUSA!", Toast.LENGTH_SHORT).show();
            start.setEnabled(true);
            start.setText("Iniciar pausa longa");
        } else if (this.time == 1) {
            tv.setText(short_break_time + " : 00");
            Toast.makeText(context, "FAÇA A SUA PAUSA!", Toast.LENGTH_SHORT).show();
            start.setEnabled(true);
            start.setText("Iniciar pausa curta");
        } else if ((this.time == 2) || (this.time == 3)) {
            tv.setText(pomodoro_time + " : 00");
            start.setEnabled(true);
            sActivity.setEnabled(true);
            Toast.makeText(context, "FINISH!", Toast.LENGTH_SHORT).show();
            start.setText("Iniciar Pomodoro");
        }
    }

    private String getCorretcTimer(boolean isMinute, long millisUntilFinished) {
        String aux;
        int constCalendar = isMinute ? Calendar.MINUTE : Calendar.SECOND;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millisUntilFinished);
        aux = c.get(constCalendar) < 10 ? "0" + c.get(constCalendar) : "" + c.get(constCalendar);
        return (aux);
    }

    private void createBarNotification() {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String CHANNEL_ID = "my_channel_01";
            CharSequence name = "my_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            nm.createNotificationChannel(mChannel);
        }

        int icone = R.drawable.ic_baseline_timelapse_24;
        long data = System.currentTimeMillis();
        String text = "Finish";

        Intent i = new Intent(context, PomodoroFragment.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
        NotificationCompat.Builder build =
                new NotificationCompat.Builder(context, "my_channel_01");

        Notification not = build.setContentIntent(pi).setSmallIcon(icone)
                .setAutoCancel(false).setTicker(text).setContentTitle("Pomodoro")
                .setContentText(text).setWhen(data).build();
        not.flags = Notification.FLAG_AUTO_CANCEL;
        not.defaults |= Notification.DEFAULT_VIBRATE;
        not.defaults |= Notification.DEFAULT_LIGHTS;
        not.defaults |= Notification.DEFAULT_SOUND;

        nm.notify(1, not);
    }
}
