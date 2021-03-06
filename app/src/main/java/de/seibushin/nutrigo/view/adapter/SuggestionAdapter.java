package de.seibushin.nutrigo.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.seibushin.nutrigo.R;
import de.seibushin.nutrigo.request.FoodLink;

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.MyViewHolder> {

    private final View.OnClickListener clickListener;
    private List<FoodLink> data = new ArrayList<>();
    private LayoutInflater inflater;

    public SuggestionAdapter(Context context, View.OnClickListener clickListener) {
        inflater = LayoutInflater.from(context);
        this.clickListener = clickListener;
    }

    public void setData(List<FoodLink> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public FoodLink getItem(int position) {
        return data.get(position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.suggestion_row, parent, false);
        view.setOnClickListener(clickListener);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setFoodLink(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            image = itemView.findViewById(R.id.food_img);
        }

        public void setFoodLink(FoodLink link) {
            name.setText(link.getName());
            image.setImageResource(R.color.colorAccent);

            if (link.getImg().matches("https://fddb.info.*")) {
                AsyncTask.execute(() -> {
                    try {
                        URL url = new URL(link.getImg());
                        Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                        image.post(() -> image.setImageBitmap(bmp));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }
}
