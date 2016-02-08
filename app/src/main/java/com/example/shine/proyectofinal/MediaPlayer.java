package com.example.shine.proyectofinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MediaPlayer extends AppCompatActivity implements View.OnClickListener {

    private Button botonPlay, botonStop, botonAnterior, botonSiguiente, botonRaw;
    private TextView textSongname;
    private android.media.MediaPlayer mediaPlayer;
    private File song;
    private int songnumber = 0;
    private int maxsongs = 0;
    private boolean modeplay = false;
    private String[] sl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediaplayer);

        textSongname = (TextView) findViewById(R.id.textViewSong);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        botonPlay = (Button) findViewById(R.id.buttonPlay);
        botonStop = (Button) findViewById(R.id.buttonStop);
        botonAnterior = (Button) findViewById(R.id.buttonAnterior);
        botonSiguiente = (Button) findViewById(R.id.buttonSiguiente);
        botonRaw = (Button) findViewById(R.id.button);

        botonPlay.setOnClickListener(this);
        botonStop.setOnClickListener(this);
        botonAnterior.setOnClickListener(this);
        botonSiguiente.setOnClickListener(this);
        botonRaw.setOnClickListener(this);

        mediaPlayer = new android.media.MediaPlayer();
        setSong();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu4, menu);
        return true;
    }

    private void cargaSong() {//arreglar un poco
        File sdCard = Environment.getExternalStorageDirectory();
        File songs = new File(sdCard.getAbsolutePath() + "/Music/");
        sl = songs.list();
        maxsongs = sl.length;
        song = new File(sdCard.getAbsolutePath() + "/Music/" + sl[songnumber]);
        String s = song.getName().toString();
        textSongname.setText(s.substring(0, s.length() - 4));
    }

    private void setSong() {
        cargaSong();
        try {
            mediaPlayer.setDataSource(song.getAbsolutePath());
            mediaPlayer.prepare();
            android.media.MediaPlayer.OnCompletionListener li = new android.media.MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(android.media.MediaPlayer mp) {
                    ++songnumber;
                    mediaPlayer.reset();
                    setSong();
                }
            };
            mediaPlayer.setOnCompletionListener(li);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (modeplay) mediaPlayer.start();
    }

    private void play() {
        mediaPlayer.start();
        modeplay = true;
        botonPlay.setBackgroundResource(R.drawable.pause);
    }

    private void pause() {
        mediaPlayer.pause();
        modeplay = false;
        botonPlay.setBackgroundResource(R.drawable.play);
    }
    private void stop() {
        mediaPlayer.stop();
        mediaPlayer.reset();
        modeplay = false;
        setSong();
        botonPlay.setBackgroundResource(R.drawable.play);
    }

//TODO: inconsistencias con wallapop! (si no sale, notification y ale)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonPlay:
                if (modeplay) pause();
                else play();
                break;
            case R.id.buttonStop:
                stop();
                break;
            case R.id.buttonSiguiente:
                if (maxsongs-1 > songnumber) ++songnumber;
                else songnumber = 0;
                mediaPlayer.stop();
                mediaPlayer.reset();
                setSong();
                break;
            case R.id.buttonAnterior:
                if (0 < songnumber) --songnumber;
                else songnumber = maxsongs-1;
                mediaPlayer.stop();
                mediaPlayer.reset();
                setSong();
                break;
            case R.id.button:
                if (modeplay) {
                    mediaPlayer.pause();
                    botonRaw.setText("Play raw memory audio");
                    modeplay = false;
                }
                else {
                    mediaPlayer = android.media.MediaPlayer.create(this, R.raw.wallapop);
                    mediaPlayer.start();
                    botonRaw.setText("Pause raw memory audio");
                    modeplay = true;
                }
                break;
        }
    }

    private void songlist() {
        ArrayList<String> lc = new ArrayList<String>(Arrays.asList(sl));
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("tosend1",lc);
        Intent intent = new Intent(getApplicationContext(), Songlist.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mlist:
                songlist();
                return true;
            case R.id.mlogout:
                SharedPreferences settings = getSharedPreferences("Usuario", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("Username", "");
                editor.putString("Password", "");
                editor.putInt("BestPoints", -1);
                editor.putInt("Notimode", 1);
                editor.putString("Avatar", "-1");
                editor.apply();
                Intent intent = new Intent(this, SigninLogin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
