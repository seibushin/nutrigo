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
import de.seibushin.nutrigo.request.Serving;

public class ServingAdapter extends RecyclerView.Adapter<ServingAdapter.MyViewHolder> {

    private final View.OnClickListener clickListener;
    private List<Serving> data = new ArrayList<>();
    private LayoutInflater inflater;

    public ServingAdapter(Context context, View.OnClickListener clickListener) {
        inflater = LayoutInflater.from(context);
        this.clickListener = clickListener;
    }

    public void setData(List<Serving> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public Serving getItem(int position) {
        return data.get(position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.serving_row, parent, false);
        view.setOnClickListener(clickListener);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setServing(data.get(position));
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

        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
        }

        public void setServing(Serving serving) {
            name.setText(serving.desc);
        }
    }
}
