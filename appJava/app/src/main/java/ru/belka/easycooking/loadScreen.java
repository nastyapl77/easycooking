package ru.belka.easycooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

public class loadScreen extends AppCompatActivity {
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_screen);
        sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        if(sp.getLong("userID", 0) != 0) {
            this.finish();
            startActivity(new Intent(loadScreen.this, MainActivity.class));
            return;
        }

        findViewById(R.id.main_text_recipe_login).setOnClickListener(v -> {
            startActivity(new Intent(loadScreen.this, loginActivity.class));
            loadScreen.this.finish();
        });
        findViewById(R.id.main_text_recipe_register).setOnClickListener(v -> {
            startActivity(new Intent(loadScreen.this, registerActivity.class));
            loadScreen.this.finish();
        });
        Toast.makeText(this, ".", Toast.LENGTH_SHORT).show();
    }
}