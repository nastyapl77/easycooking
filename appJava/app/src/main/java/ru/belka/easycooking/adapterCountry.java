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

public class adapterCountry extends RecyclerView.Adapter {
    final private Context context;
    final private LayoutInflater inflater;
    List<dataCategory> data;
    public adapterCountry(Context context, List<dataCategory> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_country, parent,false);
        return new adapterCountry.MyHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final adapterCountry.MyHolder myHolder= (adapterCountry.MyHolder) holder;
        final dataCategory current=data.get(position);

        String urlFrom = "https://kursach.allrenter.ru/webcook/" + "/images/" + current.photo;
        Picasso.with(context).load(urlFrom).into(myHolder.img);

        myHolder.txt.setText(current.name);

        myHolder.main.setOnClickListener(v -> openCat(current.ID));
    }
    public void openCat(long id) {
        context.startActivity(new Intent(context, openCountry.class).putExtra("country_id", id));
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    static class MyHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView txt;
        View main;
        public MyHolder(View itemView) {
            super(itemView);
            main = itemView;
            img = itemView.findViewById(R.id.item_country_image);
            txt = itemView.findViewById(R.id.item_country_text);
        }

    }
}