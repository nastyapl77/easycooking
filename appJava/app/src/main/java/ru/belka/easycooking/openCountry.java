package ru.belka.easycooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
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

public class openCountry extends AppCompatActivity {
    long country_id;
    JSONArray jArray;
    TextView button_edit_photo;
    int REQUEST_CODE_PERMISSION_READ_EXTERNAL = 2500;
    Uri selectedImage = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_country);
        country_id = getIntent().getLongExtra("country_id", 1);

        RequestQueue queue = Volley.newRequestQueue(openCountry.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getString(R.string.site) + "/getc.php?id=" + country_id,
                this::setCountry, error -> Toast.makeText(this, "Error:\n\n" + error.toString(), Toast.LENGTH_SHORT).show());
        queue.add(stringRequest);
        queue = Volley.newRequestQueue(openCountry.this);
        stringRequest = new StringRequest(Request.Method.GET, getString(R.string.site) + "/getcr.php?id=" + country_id,
                this::setRecipes, error -> Toast.makeText(this, "Error:\n\n" + error.toString(), Toast.LENGTH_SHORT).show());
        queue.add(stringRequest);

        cacheData cd = new cacheData();
        String temp = cd.getCache("country" + country_id + ".json", this);
        if(!temp.equals("error")) setCountry(temp);
        temp = cd.getCache("countryr" + country_id + ".json", this);
        if(!temp.equals("error")) setRecipes(temp);
        findViewById(R.id.main_add_new_recipe).setOnClickListener(v -> showDialog());


        /*temp = cd.getCache("recc" + category_id + ".json", this);
        if(!temp.equals("error")) setRecipes(temp);*/
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
    public void loadToSite(String name, String text, String time) {
        assert selectedImage != null;
        String url = "https://kursach.allrenter.ru/webcook/create_recipe.php?name=" + name + "&text=" + text + "&time=" + time + "&coun=" + country_id;
        new FilesUploadingTask(getRealPathFromURI(openCountry.this, selectedImage), url, openCountry.this).execute();
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
    public void showDialog() {
        final Dialog dialog = new Dialog(openCountry.this);
        dialog.setContentView(R.layout.dialog_add_recipe);
        dialog.create();
        button_edit_photo = dialog.findViewById(R.id.edit_for_add_recipe_photo);
        button_edit_photo.setOnClickListener(v -> {
            int permissionStatus = ContextCompat.checkSelfPermission(openCountry.this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),54);
            } else {
                ActivityCompat.requestPermissions(openCountry.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_PERMISSION_READ_EXTERNAL);
            }
        });
        TextView btn = dialog.findViewById(R.id.text_for_upload);
        btn.setOnClickListener(v -> {
            EditText edt = dialog.findViewById(R.id.edit_for_add_recipe);
            String name = edt.getText().toString();
            edt = dialog.findViewById(R.id.edit_for_add_recipe_stage);
            String text = edt.getText().toString();
            edt = dialog.findViewById(R.id.edit_for_add_recipe_time);
            String time = edt.getText().toString();
            if(name.length() <= 3) { showDialog("Неправильно указано поле 'Название'","Ошибка"); return; }
            if(text.length() <= 50) { showDialog("Невозможно опубликовать рецепт\nПричина: Слишком короткий текст","Ошибка"); return; }
            if(time.length() <= 3) { showDialog("Неправильно указано поле 'Время приготовления'","Ошибка"); return; }
            if(selectedImage == null) { showDialog("Вы должны выбрать фотографию для категории","Ошибка"); return; }
            loadToSite(name, text.replace("\n", "<br>"), time);
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
    private void setRecipes(String result) {
        cacheData cd = new cacheData();
        cd.saveCache("countryr" + country_id + ".json", result, this);
        RecyclerView recycler = findViewById(R.id.recycler_to_recipes);
        ArrayList<dataCategory> data=new ArrayList<>();
        try {
            jArray = new JSONArray(result);
            for(int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                dataCategory fishData = new dataCategory();
                fishData.ID = json_data.getLong("ID");
                fishData.name = json_data.getString("Name");
                fishData.photo = json_data.getString("Photo");
                fishData.time = json_data.getString("Time");
                fishData.calories = json_data.getDouble("Calories");
                data.add(fishData);
            }
            adapterRecipes mAdapter = new adapterRecipes(openCountry.this, data);
            recycler.setAdapter(mAdapter);
            recycler.setLayoutManager(new GridLayoutManager(this, 2));
            recycler.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        } catch (JSONException e) {
            Log.e("RecyclerError", "Error: " + e.toString());
        }
    }
    public void setCountry(String result) {
        TextView txt = findViewById(R.id.main_text_for_category);
        cacheData cd = new cacheData();
        cd.saveCache("country" + country_id + ".json", result, this);
        try {
            JSONObject jo = new JSONObject(result);
            String name = jo.getString("Name");
            txt.setText(name);
        }
        catch (JSONException e) {
            Toast.makeText(this, "Error:\n\n" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}