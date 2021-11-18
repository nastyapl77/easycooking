package ru.belka.easycooking;

import android.content.*;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class adapterCategory extends RecyclerView.Adapter {
    final private Context context;
    final private LayoutInflater inflater;
    List<dataCategory> data;
    public adapterCategory(Context context, List<dataCategory> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_category, parent,false);
        return new adapterCategory.MyHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final adapterCategory.MyHolder myHolder= (adapterCategory.MyHolder) holder;
        final dataCategory current=data.get(position);

        String urlFrom = "https://kursach.allrenter.ru/webcook/" + "/images/" + current.photo;
        Picasso.with(context).load(urlFrom).into(myHolder.img);

        myHolder.img.setOnClickListener(v -> openCat(current.ID));
    }
    public void openCat(long ID) {
        context.startActivity(new Intent(context, openCategory.class).putExtra("category_id", ID));
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    static class MyHolder extends RecyclerView.ViewHolder{
        ImageView img;
        public MyHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.image_to_item_category);
        }
    }
}
