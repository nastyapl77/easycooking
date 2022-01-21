package ru.belka.easycooking;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class loginActivity extends AppCompatActivity {
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        if(sp.getLong("userID", 0) != 0) {
            this.finish();
            startActivity(new Intent(loginActivity.this, MainActivity.class));
            return;
        }
        findViewById(R.id.login_txt_click_login).setOnClickListener(v -> {
            EditText edt = findViewById(R.id.login_edt_reg_phone);
            String phone = edt.getText().toString();
            edt = findViewById(R.id.login_edt_reg_pass);
            String pass = edt.getText().toString();

            if(phone.length() == 0) {
                showDialog("Неправильно заполнено поле 'Телефон'", "Ошибка");
                return;
            }
            if(phone.charAt(0) == '8') {
                phone = phone.substring(1);
            }
            if(phone.startsWith("+7") || phone.startsWith("+8")) {
                phone = phone.substring(2);
            }
            if(parse(phone) == -1) {
                showDialog("Неправильно заполнено поле 'Телефон'", "Ошибка");
                return;
            }
            if(pass.length() <= 5 || pass.length() >= 33) {
                showDialog("Неправильно заполнено поле 'Пароль'", "Ошибка");
                return;
            }
            request(phone, pass);
        });
    }
    public void showDialog(String text, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(text)
                .setTitle(title)
                .setCancelable(false)
                .setPositiveButton("Продолжить",
                        (dialog, id) -> {
                            dialog.dismiss();
                            if(title.equals("Результат")) {
                                loginActivity.this.finish();
                                startActivity(new Intent(loginActivity.this, MainActivity.class));
                            }
                        });
        builder.create();
        builder.show();
    }
    public void request(String phone, String pass) {
        String url = getString(R.string.site) + "/login.php?phone=" + phone + "&pass=" + pass;

        RequestQueue queue = Volley.newRequestQueue(loginActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    if(response.contains("Ошибка")) {
                        showDialog(response, "Ошибка");
                        return;
                    }
                    try {
                        JSONObject ja = new JSONObject(response);
                        long ID = ja.getLong("ID");
                        long online = ja.getLong("LastOnline");
                        @SuppressLint("SimpleDateFormat")
                        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(online * 1000));
                        String temp = "Вы успешно зашли в аккаунт.\n\nВаш последний онлайн: " + date;
                        showDialog(temp, "Результат");
                        SharedPreferences.Editor ed = sp.edit();
                        ed.putLong("userID", ID);
                        ed.apply();
                    }
                    catch(JSONException e) {
                        showDialog(e.toString(), "Ошибка");
                    }
                }, error -> showDialog(error.toString(), "Ошибка"));
        queue.add(stringRequest);
    }
    public long parse(String phone) {
        long num = Long.parseLong("0"+phone.replaceAll("[^0-9]",""));
        return 9000000000L<=num&&num<10000000000L?num:-1;
    }
}