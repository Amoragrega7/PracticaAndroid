package com.example.shine.proyectofinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MenuPrincipal extends AppCompatActivity implements View.OnClickListener {

    private Button botonMemo, botonPerf, botonRank, botonMedia, botonCalcu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        botonMemo   = (Button) findViewById(R.id.buttonMemory);
        botonPerf   = (Button) findViewById(R.id.buttonPerfil);
        botonRank   = (Button) findViewById(R.id.buttonRanking);
        botonMedia  = (Button) findViewById(R.id.buttonMediaPlayer);
        botonCalcu  = (Button) findViewById(R.id.buttonCalculadora);

        botonMemo. setOnClickListener(this);
        botonPerf. setOnClickListener(this);
        botonRank. setOnClickListener(this);
        botonMedia.setOnClickListener(this);
        botonCalcu.setOnClickListener(this);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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

    public void onClick(View v) {
        Intent intent;
        Bundle bundle;
        switch (v.getId()) {
            case R.id.buttonMemory:
                intent = new Intent(getApplicationContext(), Memory.class);
                startActivity(intent);
                break;
            case R.id.buttonPerfil:
                intent = new Intent(getApplicationContext(), PerfilRanking.class);
                bundle = new Bundle();
                bundle.putInt("who",0);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.buttonRanking:
                intent = new Intent(getApplicationContext(), PerfilRanking.class);
                bundle = new Bundle();
                bundle.putInt("who",1);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.buttonMediaPlayer:
                intent = new Intent(getApplicationContext(), MediaPlayer.class);
                startActivity(intent);
                break;
            case R.id.buttonCalculadora:
                SharedPreferences firstTime = getSharedPreferences("FirstTime", 0);
                SharedPreferences.Editor editor = firstTime.edit();
                editor.putBoolean("Control", true);
                editor.apply();
                intent = new Intent(getApplicationContext(), Calculadora.class);
                startActivity(intent);
                break;
        }
    }
}
