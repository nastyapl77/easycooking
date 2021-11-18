package ru.belka.easycooking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class registerActivity extends AppCompatActivity {
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        if(sp.getLong("userID", 0) != 0) {
            this.finish();
            startActivity(new Intent(registerActivity.this, MainActivity.class));
            return;
        }

        findViewById(R.id.register_txt_click_register).setOnClickListener(v -> {
            EditText edt = findViewById(R.id.register_edt_reg_name);
            String name = edt.getText().toString();
            edt = findViewById(R.id.register_edt_reg_family);
            String family = edt.getText().toString();
            edt = findViewById(R.id.register_edt_reg_phone);
            String phone = edt.getText().toString();
            edt = findViewById(R.id.register_edt_reg_pass);
            String pass = edt.getText().toString();

            if(name.length() <= 2) {
                showDialog("Неправильно заполнено поле 'Имя'", "Ошибка");
                return;
            }
            if(family.length() <= 2) {
                showDialog("Неправильно заполнено поле 'Фамилия'", "Ошибка");
                return;
            }
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
            request(name, family, phone, pass);
        });
    }
    public void showDialog(String text, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(text)
                .setTitle(title)
                .setCancelable(false)
                .setPositiveButton("Продолжить",
                        (dialog, id) -> dialog.dismiss());
        builder.create();
        builder.show();
    }
    public void request(String name, String family, String phone, String pass) {
        String url = "https://kursach.allrenter.ru/webcook/" + "/register.php?name=" + name + "&family=" + family + "&phone=" + phone + "&pass=" + pass;

        RequestQueue queue = Volley.newRequestQueue(registerActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    if(response.equals("Ошибка")) {
                        showDialog(response, "Ошибка");
                        return;
                    }
                    showDialog(response, "Результат");
                }, error -> showDialog(error.toString(), "Ошибка"));
        queue.add(stringRequest);
    }
    public long parse(String phone) {
        long num = Long.parseLong("0"+phone.replaceAll("[^0-9]",""));
        return 9000000000L<=num&&num<10000000000L?num:-1;
    }
}