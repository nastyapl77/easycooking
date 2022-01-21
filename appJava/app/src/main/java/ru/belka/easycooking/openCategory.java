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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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

public class openCategory extends AppCompatActivity {
    long category_id;
    String resipec;
    JSONArray jArray = null;
    TextView button_edit_photo;
    final int REQUEST_CODE_PERMISSION_READ_EXTERNAL = 2500;
    Uri selectedImage = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_category);

        EditText edt = findViewById(R.id.main_search_recipe);
        edt.setEnabled(true);
        edt.addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                if(jArray == null) { return; }
                search(edt.getText().toString());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        findViewById(R.id.relative_to_profile_cat).setOnClickListener(v -> Toast.makeText(openCategory.this, "Тут откроется профиль", Toast.LENGTH_SHORT).show());
        findViewById(R.id.relative_to_star).setOnClickListener(v -> startActivity(new Intent(openCategory.this, my_star.class)));
        category_id = getIntent().getLongExtra("category_id", 1);
        RequestQueue queue = Volley.newRequestQueue(openCategory.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://kursach.allrenter.ru/webcook/" + "/getcategory.php?id=" + category_id,
                this::setCat, error -> Toast.makeText(this, "Error:\n\n" + error.toString(), Toast.LENGTH_SHORT).show());
        queue.add(stringRequest);
        queue = Volley.newRequestQueue(openCategory.this);
        stringRequest = new StringRequest(Request.Method.GET, "https://kursach.allrenter.ru/webcook/" + "/getrecipes.php?id=" + category_id,
                this::setRecipes, error -> Toast.makeText(this, "Error:\n\n" + error.toString(), Toast.LENGTH_SHORT).show());
        queue.add(stringRequest);
        findViewById(R.id.main_add_new_recipe).setOnClickListener(v -> showDialog());
        cacheData cd = new cacheData();
        String temp = cd.getCache("cat" + category_id + ".json", this);
        if(!temp.equals("error")) setCat(temp);
        temp = cd.getCache("recc" + category_id + ".json", this);
        if(!temp.equals("error")) setRecipes(temp);

        String[] menu = {"Без сортировки", "Калории - по убыванию", "Калории - по возрастанию"};
        Spinner sp = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, menu);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = sp.getSelectedItem().toString();
                if(selected.equals(menu[0])) {
                    RequestQueue queue = Volley.newRequestQueue(openCategory.this);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://kursach.allrenter.ru/webcook/" + "/getrecipes.php?id=" + category_id,
                            response -> setRecipes(response), error -> Toast.makeText(openCategory.this, "Error:\n\n" + error.toString(), Toast.LENGTH_SHORT).show());
                    queue.add(stringRequest);
                    return;
                }
                if(selected.equals(menu[1])) {
                    RequestQueue queue = Volley.newRequestQueue(openCategory.this);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://kursach.allrenter.ru/webcook/" + "/getrecipes.php?sort=1&id=" + category_id,
                            response -> setRecipes(response), error -> Toast.makeText(openCategory.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show());
                    queue.add(stringRequest);
                    return;
                }
                if(selected.equals(menu[2])) {
                    RequestQueue queue = Volley.newRequestQueue(openCategory.this);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://kursach.allrenter.ru/webcook/" + "/getrecipes.php?sort=2&id=" + category_id,
                            response -> setRecipes(response), error -> Toast.makeText(openCategory.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show());
                    queue.add(stringRequest);
                    return;
                }
                Toast.makeText(openCategory.this, selected, Toast.LENGTH_SHORT).show();


            }@Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void search(String word) {
        if(word == null) {
            setRecipes(resipec);
            return;
        }
        RecyclerView recycler = findViewById(R.id.recycler_to_recipes);
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
            adapterRecipes mAdapter = new adapterRecipes(openCategory.this, data);
            recycler.setAdapter(mAdapter);
            recycler.setLayoutManager(new GridLayoutManager(this, 2));
            recycler.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        } catch (JSONException e) {
            Log.e("RecyclerError", "Error: " + e.toString());
        }
    }

    public void showDialog() {
        final Dialog dialog = new Dialog(openCategory.this);
        dialog.setContentView(R.layout.dialog_add_recipe);
        dialog.create();
        button_edit_photo = dialog.findViewById(R.id.edit_for_add_recipe_photo);
        button_edit_photo.setOnClickListener(v -> {
            int permissionStatus = ContextCompat.checkSelfPermission(openCategory.this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),54);
            } else {
                ActivityCompat.requestPermissions(openCategory.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
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
        String url = "https://kursach.allrenter.ru/webcook/create_recipe.php?name=" + name + "&text=" + text + "&time=" + time + "&cat=" + category_id;
        new FilesUploadingTask(getRealPathFromURI(openCategory.this, selectedImage), url, openCategory.this).execute();
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
        cd.saveCache("recc" + category_id + ".json", result, this);
        resipec = result;
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
            adapterRecipes mAdapter = new adapterRecipes(openCategory.this, data);
            recycler.setAdapter(mAdapter);
            recycler.setLayoutManager(new GridLayoutManager(this, 2));
            recycler.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        } catch (JSONException e) {
            Log.e("RecyclerError", "Error: " + e.toString());
        }
    }
    private void setCat(String result) {
        cacheData cd = new cacheData();
        cd.saveCache("cat" + category_id + ".json", result, this);
        try {
            JSONObject jo = new JSONObject(result);
            String name = jo.getString("Name");
            TextView txt = findViewById(R.id.main_text_for_category);
            txt.setText(name);
        }
        catch (JSONException e) {
            Toast.makeText(this, "Error:\n\n" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}