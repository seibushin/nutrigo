package de.seibushin.nutrigo.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import de.seibushin.nutrigo.model.nutrition.NutritionType;
import de.seibushin.nutrigo.view.fragments.FragmentDay;
import de.seibushin.nutrigo.Helper;
import de.seibushin.nutrigo.R;
import de.seibushin.nutrigo.model.nutrition.NutritionUnit;

/**
 * {@link RecyclerView.Adapter} that can display a {@link NutritionUnit} and makes a call to the
 * specified {@link FragmentDay.OnListFragmentInteractionListener}.
 */
public class NutritionAdapter extends RecyclerView.Adapter<NutritionAdapter.ViewHolder> {
    private final List<NutritionUnit> mValues;
//    private final OnListFragmentInteractionListener mListener;

//    public MyItemRecyclerViewAdapter(List<DummyItem> items, OnListFragmentInteractionListener listener) {
//        mValues = items;
//        mListener = listener;
//    }

    public NutritionAdapter() {
        mValues = new ArrayList<>();
    }

    public NutritionAdapter(List<NutritionUnit> items) {
        mValues = items;
    }

    /**
     * Update the items list
     *
     * @param items
     */
    public synchronized void setItems(List<NutritionUnit> items) {
        this.mValues.clear();
        this.mValues.addAll(items);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nutrition_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.setNutri(mValues.get(position));

//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.onListFragmentInteraction(holder.mItem);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private NutritionUnit item;

        private final TextView name;
        private final TextView kcal;
        private final TextView fat;
        private final TextView carbs;
        private final TextView sugar;
        private final TextView protein;
        private final TextView weight;
        private final TextView portion;

        /**
         * Constructor
         *
         * @param view view
         */
        ViewHolder(View view) {
            super(view);
            mView = view;
            name = view.findViewById(R.id.tv_name);
            kcal = view.findViewById(R.id.tv_kcal);
            fat = view.findViewById(R.id.tv_fat);
            carbs = view.findViewById(R.id.tv_carbs);
            sugar = view.findViewById(R.id.tv_sugar);
            protein = view.findViewById(R.id.tv_protein);
            weight = view.findViewById(R.id.tv_weight);
            portion = view.findViewById(R.id.tv_portion);
        }

        /**
         * Set the nutrition unit
         *
         * @param item nutrition unit
         */
        void setNutri(NutritionUnit item) {
            this.item = item;
            name.setText(item.getName());
            kcal.setText(kcal.getContext().getString(R.string.kcal_unit, item.getKcal()));
            fat.setText(Helper.formatDecimal(item.getFat()));
            carbs.setText(Helper.formatDecimal(item.getCarbs()));
            sugar.setText(Helper.formatDecimal(item.getSugar()));
            protein.setText(Helper.formatDecimal(item.getProtein()));
            weight.setText(weight.getContext().getString(R.string.weight_unit, item.getWeight()));
            if (item.getType() == NutritionType.FOOD) {
                portion.setText(portion.getContext().getString(R.string.weight_unit, item.getPortion()));
            } else {
                portion.setText(String.format("%s", item.getPortion()));
            }
        }
    }
}
