package ru.belka.easycooking;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;

public class openRecipe extends AppCompatActivity {
    long cook_id;
    int star = 0;
    String Name, Text, Photo, mainresult;
    @SuppressLint("Recycle")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_recipe);

        findViewById(R.id.relative_to_profile).setOnClickListener(v -> Toast.makeText(openRecipe.this, "Тут откроется профиль", Toast.LENGTH_SHORT).show());
        findViewById(R.id.relative_to_star).setOnClickListener(v -> startActivity(new Intent(openRecipe.this, my_star.class)));

        actionStarBase aSB = new actionStarBase();

        cook_id = getIntent().getLongExtra("cook_id", 1);

        String result = aSB.getRecipe(this, cook_id);
        if(result != null && result.length() >= 20) {
            setRecipe(result);
            TextView txt = findViewById(R.id.main_text_for_recipe_add_star);
            txt.setText("Удалить из избранного");
            txt.setOnClickListener(v -> {
                if(star == 1) { deleteStar(); }
                else addStar();
            });
        }
        else {
            RequestQueue queue = Volley.newRequestQueue(openRecipe.this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://kursach.allrenter.ru/webcook/" + "/getrecipe.php?id=" + cook_id,
                    this::setRecipe, error -> Toast.makeText(this, "Error:\n\n" + error.toString(), Toast.LENGTH_SHORT).show());
            queue.add(stringRequest);
            TextView txt = findViewById(R.id.main_text_for_recipe_add_star);
            txt.setText("Добавить в избранное");
            txt.setOnClickListener(v -> {
                if(star == 1) { deleteStar(); }
                else addStar();
            });
        }
    }
    private void addStar()  {
        star = 1;
        actionStarBase aSB = new actionStarBase();
        aSB.setRecipe(this, cook_id, mainresult);
        TextView txt = findViewById(R.id.main_text_for_recipe_add_star);
        txt.setText("Удалить из избранного");
    }
    @SuppressLint("Recycle")
    private void deleteStar() {
        star = 0;
        actionStarBase aSB = new actionStarBase();
        aSB.deleteRecipe(this, cook_id);
        TextView txt = findViewById(R.id.main_text_for_recipe_add_star);
        txt.setText("Добавить в избранное");
    }
    private void setText() {
        TextView txt = findViewById(R.id.main_text_for_recipe_name);
        txt.setText(Name);
        txt = findViewById(R.id.main_text_for_recipe_all_recipe);
        txt.setText(Text);
        ImageView img = findViewById(R.id.main_text_for_recipe_photo);
        String urlFrom = "https://kursach.allrenter.ru/webcook/" + "/images/" + Photo;
        Picasso.with(this).load(urlFrom).into(img);
    }
    private void setRecipe(String result) {
        try {
            mainresult = result;
            JSONObject jo = new JSONObject(result);
            Name = jo.getString("Name");
            Text = jo.getString("Text");
            Text = Text.replace("<br>", "\n");
            Photo = jo.getString("Photo");
            setText();
        }
        catch (JSONException e) {
            Toast.makeText(this, "Error:\n\n" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}