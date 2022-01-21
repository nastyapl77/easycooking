package ru.belka.easycooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    JSONArray jArray, bArray = null;
    String cats;
    final int REQUEST_CODE_PERMISSION_READ_EXTERNAL = 2500;
    Uri selectedImage = null;
    TextView button_edit_photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.relative_to_profile).setOnClickListener(v -> Toast.makeText(MainActivity.this, "Тут откроется профиль", Toast.LENGTH_SHORT).show());
        findViewById(R.id.relative_to_star).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, my_star.class)));

        sendReq();

        EditText edt = findViewById(R.id.main_search_recipe);
        edt.setEnabled(true);
        edt.addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                if(jArray == null) { sendReq(); return; }
                search(edt.getText().toString());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        findViewById(R.id.main_add_new_recipe).setOnClickListener(v -> showDialog());

        cacheData cd = new cacheData();
        String temp = cd.getCache("cats.json", this);
        if(!temp.equals("error")) setRecycler(temp);
        temp = cd.getCache("country.json", this);
        if(!temp.equals("error")) setCountry(temp);
    }
    private void sendReq() {
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://kursach.allrenter.ru/webcook/" + "/getcategories.php",
                this::setRecycler, error -> {
                    if(error.toString().contains("NoConnectionError")) {
                        showDialog("Вы не подключены к интернету!", "Ошибка");
                        return;
                    }
                    if(error.toString().contains("TimeoutError")) {
                        showDialog("Ошибка подключения к интернету!", "Ошибка");
                        return;
                    }
                    Toast.makeText(this, "Error:\n\n" + error.toString(), Toast.LENGTH_SHORT).show();
                });
        queue.add(stringRequest);
        queue = Volley.newRequestQueue(MainActivity.this);
        stringRequest = new StringRequest(Request.Method.GET, "https://kursach.allrenter.ru/webcook/" + "/getcountry.php",
                this::setCountry, error -> {
            if(error.toString().contains("NoConnectionError")) {
                showDialog("Вы не подключены к интернету!", "Ошибка");
                return;
            }
            if(error.toString().contains("TimeoutError")) {
                showDialog("Ошибка подключения к интернету!", "Ошибка");
                return;
            }
            Toast.makeText(this, "Error:\n\n" + error.toString(), Toast.LENGTH_SHORT).show();
        });
        queue.add(stringRequest);
    }
    public void setCountry(String result) {
        cacheData cd = new cacheData();
        cd.saveCache("country.json", result, this);
        RecyclerView recycler = findViewById(R.id.recycler_to_countries);
        ArrayList<dataCategory> data=new ArrayList<>();
        try {
            bArray = new JSONArray(result);
            for(int i = 0; i < bArray.length(); i++) {
                JSONObject json_data = bArray.getJSONObject(i);
                dataCategory fishData = new dataCategory();
                fishData.ID = json_data.getLong("ID");
                fishData.name = json_data.getString("Name");
                fishData.photo = json_data.getString("Photo");
                data.add(fishData);
            }
            adapterCountry mAdapter = new adapterCountry(MainActivity.this, data);
            recycler.setAdapter(mAdapter);
            recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
            recycler.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        } catch (JSONException e) {
            Log.e("RecyclerError", "Error: " + e.toString());
        }
    }
    public void showDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_add_category);
        dialog.create();
        button_edit_photo = dialog.findViewById(R.id.edit_for_add_category_photo);
        button_edit_photo.setOnClickListener(v -> {
            int permissionStatus = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),54);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_PERMISSION_READ_EXTERNAL);
            }
        });
        TextView btn = dialog.findViewById(R.id.text_for_upload);
        btn.setOnClickListener(v -> {
            EditText edt = dialog.findViewById(R.id.edit_for_add_category);
            String name = edt.getText().toString();
            if(name.length() <= 3) { showDialog("Неправильно указано поле 'Название'","Ошибка"); return; }
            if(selectedImage == null) { showDialog("Вы должны выбрать фотографию для категории","Ошибка"); return; }
            loadToSite(name);
            dialog.cancel();
        });
        btn = dialog.findViewById(R.id.text_for_cancel);
        btn.setOnClickListener(v -> dialog.cancel());
        dialog.show();
    }
    public void showDialog(String text, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(text)
                .setTitle(title)
                .setCancelable(false)
                .setPositiveButton("Продолжить",
                        (dialog, id) -> dialog.dismiss()).create();
        builder.show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION_READ_EXTERNAL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 54);
            } else {
                Toast.makeText(this, "Для загрузки фотографии мне нужен доступ к Вашим файлам!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_CANCELED) {
            try {
                if (requestCode == 54) {
                    if (intent != null) {
                        selectedImage = intent.getData();
                        String temp = "Выбрано: " + selectedImage.toString();
                        button_edit_photo.setText(temp);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Could not load image",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Throwable e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void loadToSite(String name) {
        assert selectedImage != null;
        String url = "https://kursach.allrenter.ru/webcook/loadimage.php?name=" + name;
        new FilesUploadingTask(getRealPathFromURI(MainActivity.this, selectedImage), url, MainActivity.this).execute();
    }
    public static String getRealPathFromURI(Context context, Uri contentURI) {
        String result = null;
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            if(idx >= 0) {
                result = cursor.getString(idx);
            }
            cursor.close();
        }
        return result;
    }
    public void search(String word) {
        if(word == null) {
            setRecycler(cats);
            return;
        }
        RecyclerView recycler = findViewById(R.id.recycler_to_cats);
        ArrayList<dataCategory> data=new ArrayList<>();
        try {
            for(int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                dataCategory fishData = new dataCategory();
                fishData.ID = json_data.getLong("ID");
                fishData.name = json_data.getString("Name");
                fishData.photo = json_data.getString("Photo");
                if(!fishData.name.toLowerCase().contains(word.toLowerCase())) continue;
                data.add(fishData);
            }
            adapterCategory mAdapter = new adapterCategory(MainActivity.this, data);
            recycler.setAdapter(mAdapter);
            recycler.setLayoutManager(new GridLayoutManager(this, 2));
            recycler.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        } catch (JSONException e) {
            Log.e("RecyclerError", "Error: " + e.toString());
        }
    }

    public void setRecycler(String result) {
        cacheData cd = new cacheData();
        cd.saveCache("cats.json", result, this);
        cats = result;
        RecyclerView recycler = findViewById(R.id.recycler_to_cats);
        ArrayList<dataCategory> data=new ArrayList<>();
        try {
            jArray = new JSONArray(result);
            for(int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                dataCategory fishData = new dataCategory();
                fishData.ID = json_data.getLong("ID");
                fishData.name = json_data.getString("Name");
                fishData.photo = json_data.getString("Photo");
                data.add(fishData);
            }
            adapterCategory mAdapter = new adapterCategory(MainActivity.this, data);
            recycler.setAdapter(mAdapter);
            recycler.setLayoutManager(new GridLayoutManager(this, 2));
            recycler.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        } catch (JSONException e) {
            Log.e("RecyclerError", "Error: " + e.toString());
        }
    }
}