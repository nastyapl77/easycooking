package ru.belka.easycooking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;

public class my_star extends AppCompatActivity {
    ArrayList<dataCategory> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_star);
        EditText edt = findViewById(R.id.star_search_recipe);
        edt.setEnabled(true);
        edt.addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                if(data == null) { return; }
                search(edt.getText().toString());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        setRecipes();
    }
    public void setRecipes() {
        actionStarBase aSB = new actionStarBase();
        String[] files = aSB.getFiles(this);
        String temp = Arrays.toString(files);
        if(!temp.contains("recipe_")) {
            TextView txt = findViewById(R.id.star_text_for_not_recipes);
            txt.setVisibility(TextView.VISIBLE);
            return;
        }
        RecyclerView recycler = findViewById(R.id.recycler_to_recipes);
        data = new ArrayList<>();
        for (String file : files) {
            if(file == null) continue;
            if (!file.contains("recipe_")) continue;
            try {
                String result = aSB.getRecipe(this, file);
                dataCategory fishData = new dataCategory();
                JSONObject ja = new JSONObject(result);
                fishData.photo = ja.getString("Photo");
                fishData.name = ja.getString("Name");
                fishData.time = ja.getString("Time");
                fishData.ID = ja.getLong("ID");
                fishData.calories = ja.getDouble("Calories");
                data.add(fishData);
            } catch (JSONException e) {
                Toast.makeText(this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        adapterRecipes mAdapter = new adapterRecipes(my_star.this, data);
        recycler.setAdapter(mAdapter);
        recycler.setLayoutManager(new GridLayoutManager(this, 2));
        recycler.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

    }
    public void search(String word) {
        if(word == null) {
            setRecipes();
            return;
        }
        actionStarBase aSB = new actionStarBase();
        String[] files = aSB.getFiles(this);
        String temp = Arrays.toString(files);
        if(!temp.contains("recipe_")) {
            TextView txt = findViewById(R.id.star_text_for_not_recipes);
            txt.setVisibility(TextView.VISIBLE);
            return;
        }
        RecyclerView recycler = findViewById(R.id.recycler_to_recipes);
        data = new ArrayList<>();
        for (String file : files) {
            if(file == null) continue;
            if (!file.contains("recipe_")) continue;
            try {
                String result = aSB.getRecipe(this, file);
                dataCategory fishData = new dataCategory();
                JSONObject ja = new JSONObject(result);
                fishData.photo = ja.getString("Photo");
                fishData.name = ja.getString("Name");
                fishData.time = ja.getString("Time");
                fishData.ID = ja.getLong("ID");
                fishData.calories = ja.getDouble("Calories");
                if(!fishData.name.toLowerCase().contains(word.toLowerCase())) continue;
                data.add(fishData);
            } catch (JSONException e) {
                Toast.makeText(this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        adapterRecipes mAdapter = new adapterRecipes(my_star.this, data);
        recycler.setAdapter(mAdapter);
        recycler.setLayoutManager(new GridLayoutManager(this, 2));
        recycler.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
    }
}