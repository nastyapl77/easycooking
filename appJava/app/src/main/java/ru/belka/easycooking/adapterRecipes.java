package ru.belka.easycooking;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class adapterRecipes extends RecyclerView.Adapter {
    final private Context context;
    final private LayoutInflater inflater;
    List<dataCategory> data;
    public adapterRecipes(Context context, List<dataCategory> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_recipe, parent,false);
        return new adapterRecipes.MyHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final adapterRecipes.MyHolder myHolder= (adapterRecipes.MyHolder) holder;
        final dataCategory current=data.get(position);

        String urlFrom = "https://kursach.allrenter.ru/webcook/" + "/images/" + current.photo;
        Picasso.with(context).load(urlFrom).into(myHolder.img);

        myHolder.txt.setText(current.name);
        myHolder.txt2.setText(current.time);
        String temp = "Калории: " + current.calories;
        myHolder.txt3.setText(temp);

        myHolder.img.setOnClickListener(v -> openCat(current.ID));
    }
    public void openCat(long ID) {
        context.startActivity(new Intent(context, openRecipe.class).putExtra("cook_id", ID));
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    static class MyHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView txt, txt2, txt3;
        View main;
        public MyHolder(View itemView) {
            super(itemView);
            main = itemView;
            img = itemView.findViewById(R.id.image_to_item_recipe);
            txt = itemView.findViewById(R.id.txt_to_item_recipe);
            txt2 = itemView.findViewById(R.id.txt_to_item_recipe_time);
            txt3 = itemView.findViewById(R.id.txt_to_item_recipe_calories);
        }
    }
}
